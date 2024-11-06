package store.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<Order> generalProducts;
    private final List<Order> promotionProducts;

    public Cart() {
        this.generalProducts = new ArrayList<>();
        this.promotionProducts = new ArrayList<>();
    }

    public List<Order> getLackQuantityPromotionOrders() {
        List<Order> result = new ArrayList<>();

        for (Order product : promotionProducts) {
            if (product.getInsufficientQuantity() > 0) {
                result.add(product);
            }
        }
        return result;
    }


    public boolean validDatePromotion(LocalDateTime nowTime) {
        for (Order order : promotionProducts) {
            if (!order.isValidDate(nowTime)) {
                return false;
            }
        }
        return true;
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
