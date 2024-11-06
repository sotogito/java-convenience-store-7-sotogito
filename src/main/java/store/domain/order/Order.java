package store.domain.order;

import store.domain.items.Product;

public class Order {
    private final Product product;
    private final int purchaseQuantity;

    public Order(Product product, int purchaseQuantity) {
        this.product = product;
        this.purchaseQuantity = purchaseQuantity;
    }

    public int getTotalAmount() {
        return purchaseQuantity * product.getPrice(); //fixme 상품 내부로 넘겨?
    }

}

