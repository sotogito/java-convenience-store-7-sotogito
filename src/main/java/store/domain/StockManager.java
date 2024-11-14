package store.domain;

import java.util.Map;
import store.domain.items.Products;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;

public class StockManager {

    public void decreaseStock(Products products, Map<Product, Integer> productQuantity) {
        for (Map.Entry<Product, Integer> entry : productQuantity.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();

            handePromotionType(products, product, quantity);
        }
    }


    private void handePromotionType(Products products, Product product, int quantity) {
        if (product instanceof PromotionProduct) {
            decreasePromotionStock(products, product, quantity);
            return;
        }
        decreaseGeneralStock(products, product, quantity);
    }

    private void decreaseGeneralStock(Products products, Product product, int quantity) {
        int remainingQuantity = 0;

        for (int i = 0; i < quantity; i++) {
            if (!product.isOutOfStock()) {
                product.decreaseQuantitySingly();
                continue;
            }
            remainingQuantity = quantity - i;
            break;
        }
        if (remainingQuantity == 0) {
            return;
        }
        Product promotionProduct = products.findPromotionProductByName(product.getName());
        promotionProduct.decreaseQuantity(remainingQuantity);
    }

    private void decreasePromotionStock(Products products, Product product, int quantity) {
        int remainingQuantity = 0;

        for (int i = 0; i < quantity; i++) {
            if (!product.isOutOfStock()) {
                product.decreaseQuantity(quantity);
                continue;
            }
            remainingQuantity = quantity - i;
            break;
        }
        if (remainingQuantity == 0) {
            return;
        }
        Product generalProduct = products.findGeneralProductByName(product.getName());
        generalProduct.decreaseQuantity(remainingQuantity);
    }
}
