package store.domain.stock;

import java.util.Map;
import store.domain.items.Products;
import store.domain.items.item.Product;

public interface StockManager {
    void process(Products products, Map<Product, Integer> productQuantity);
}
