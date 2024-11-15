package store.domain;

import java.util.List;
import java.util.Map;
import store.constants.messages.ErrorMessage;
import store.domain.items.Products;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;

public class StockManager {

    public void decreaseStock(Products products, Map<Product, Integer> productQuantity) {
        for (Map.Entry<Product, Integer> entry : productQuantity.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            validateStock(products, product, quantity);
            precessDecrease(products, product, quantity);
        }
    }

    private void validateStock(Products products, Product orderProduct, int orderQuantity) {
        int totalStick = 0;
        List<Product> sameNameProduct = products.getSameNameProducts(orderProduct.getName());
        for (Product product : sameNameProduct) {
            totalStick += product.getQuantity();
        }
        if (totalStick < orderQuantity) {
            throw new IllegalArgumentException(ErrorMessage.INPUT_INSUFFICIENT_STOCK_ORDER.get());
        }
    }

    private void precessDecrease(Products products, Product product, Integer quantity) {
        if (product instanceof PromotionProduct promotionProduct) {
            promotionProductDecrease(products, product, quantity, promotionProduct);
            return;
        }
        generalProductDecrease(product, quantity);
    }

    private void generalProductDecrease(Product product, Integer quantity) {
        product.decreaseQuantity(quantity);
    }

    private void promotionProductDecrease(Products products, Product product, Integer quantity,
                                          PromotionProduct promotionProduct) {
        for (int i = 0; i < quantity; i++) {
            if (changeOutOfStockProductToGeneral(products, product, promotionProduct)) {
                continue;
            }
            promotionProduct.decreaseQuantitySingly();
        }
    }

    private boolean changeOutOfStockProductToGeneral(Products products, Product product,
                                                     PromotionProduct promotionProduct) {
        if (promotionProduct.isOutOfStock()) {
            Product generalProduct = findGeneralProductByName(products, product.getName());
            generalProduct.decreaseQuantitySingly();
            return true;
        }
        return false;
    }

    private Product findGeneralProductByName(Products products, String name) {
        return products.getGeneralProductByName(name);
    }

}
