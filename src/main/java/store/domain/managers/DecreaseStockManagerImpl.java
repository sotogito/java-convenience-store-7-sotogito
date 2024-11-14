package store.domain.managers;

import java.util.Map;
import store.domain.items.Products;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;

public class DecreaseStockManagerImpl implements DecreaseStockManager {

    @Override
    public void process(Products products, Map<Product, Integer> productQuantity) {
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


    @Override
    public void decreasePromotionStock(Products products, Product product, int quantity) {
        int nowDecreaseStockQuantity = getNowDecreasedStockQuantity(product, quantity);
        int remainingQuantity = calculateRemainingQuantity(quantity, nowDecreaseStockQuantity);

        if (isKeepDecreaseStock(remainingQuantity)) {
            decreaseChangingPromotionProductToGeneral(products, product, remainingQuantity);
        }
    }

    private static void decreaseChangingPromotionProductToGeneral(
            Products products, Product product, int remainingQuantity) {
        Product generalProduct = products.findGeneralProductByName(product.getName());
        generalProduct.decreaseQuantity(remainingQuantity);
    }


    @Override
    public void decreaseGeneralStock(Products products, Product product, int quantity) {
        int nowDecreaseStockQuantity = getNowDecreasedStockQuantity(product, quantity);
        int remainingQuantity = calculateRemainingQuantity(quantity, nowDecreaseStockQuantity);

        if (isKeepDecreaseStock(remainingQuantity)) {
            decreaseChangingGeneralProductToPromotion(products, product, nowDecreaseStockQuantity);
        }

    }

    private void decreaseChangingGeneralProductToPromotion(
            Products products, Product product, int remainingQuantity) {
        Product promotionProduct = products.findPromotionProductByName(product.getName());
        promotionProduct.decreaseQuantity(remainingQuantity);
    }


    private int getNowDecreasedStockQuantity(Product product, int quantity) {
        for (int i = 0; i < quantity; i++) {
            if (!product.isOutOfStock()) {
                product.decreaseQuantitySingly();
                continue;
            }
            return i;
        }
        return quantity;
    }

    private boolean isKeepDecreaseStock(int remainingQuantity) {
        return remainingQuantity != 0;
    }

    private int calculateRemainingQuantity(int purchaseQuantity, int nowStockDecreasedQuantity) {
        return purchaseQuantity - nowStockDecreasedQuantity;
    }

}

