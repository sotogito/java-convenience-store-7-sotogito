package store.domain.managers;

import store.domain.items.Products;
import store.domain.items.item.Product;

public interface DecreaseStockManager extends StockManager {

    void decreasePromotionStock(Products products, Product product, int quantity);

    void decreaseGeneralStock(Products products, Product product, int quantity);
}
