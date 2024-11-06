package store.domain.order;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<Order> generalProducts;
    private final List<Order> promotionProducts;

    public Cart() {
        this.generalProducts = new ArrayList<>();
        this.promotionProducts = new ArrayList<>();
    }

    public boolean hasPromotionProduct() {
        return !promotionProducts.isEmpty();
    }

    public void addProduct(Order order) {
        if (order.isPromotionProduct()) {
            promotionProducts.add(order);
            return;
        }
        generalProducts.add(order);
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (Order order : generalProducts) {
            totalPrice += order.calculateTotalAmount();
        }
        for (Order order : promotionProducts) {
            totalPrice += order.calculateTotalAmount();
        }
        return totalPrice;
    }

}
