package store.domain;

import store.domain.items.item.Product;

public interface OrderProductFinder {

    Product findProductByNameAndQuantity(String name, int quantity);

    Product findGeneralProductByNameAndQuantity(String name, int quantity);

}
