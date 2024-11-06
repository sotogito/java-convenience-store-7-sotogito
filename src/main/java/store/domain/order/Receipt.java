package store.domain.order;

import java.util.ArrayList;
import java.util.List;

/**
 * 구매한 상품과 할인 금액으로 최종 금액을 정한다.
 */
public class Receipt {
    private final List<Order> orders;
    private int finalAmount;
    private boolean hasPromotion;
    private boolean isMembership;

    public Receipt() {
        this.orders = new ArrayList<>();

    }


    public boolean hasPromotionProduct() {
        for (Order order : orders) {
            return order.isPromotionItem();
        }
        return false;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void calculateFinalAmount() {
        for (Order order : orders) {
            this.finalAmount += order.getTotalAmount();
        }
    }

    public int getFinalAmount() {
        return finalAmount;
    }


}
