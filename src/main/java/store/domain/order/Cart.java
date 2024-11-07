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


    public List<Order> getShortageStockPromotionOrders() {
        List<Order> result = new ArrayList<>();

        for (Order orderProduct : promotionProducts) {
            if (orderProduct.getShortageQuantity() > 0) { //note 값이 더 크면 구매 수량이 더 큰것이다.
                result.add(orderProduct);
            }
        }
        return result;
    }

    public List<Order> getLackQuantityPromotionOrders() {
        List<Order> result = new ArrayList<>();

        for (Order orderProduct : promotionProducts) {
            int noAppliedQuantity = orderProduct.getQuantityNoPromotionApplied();
            if (noAppliedQuantity > 0 && orderProduct.isOverPromotionMinBuyQuantity(noAppliedQuantity)) {
                result.add(orderProduct);
            }
        }
        return result;
    }


    public void changePromotionToGeneral(Order promotionOrder, int shortageQuantity) {

    }


    public boolean canDatePromotion(LocalDateTime nowTime) {
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
        for (Order order : generalProducts) { //todo 프로모션 가격 조정해야됨
            totalPrice += order.calculateTotalAmount();
        }
        for (Order order : promotionProducts) {
            totalPrice += order.calculateTotalAmount();
        }
        return totalPrice;
    }

}
