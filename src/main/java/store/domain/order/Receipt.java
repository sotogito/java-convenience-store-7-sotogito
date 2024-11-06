package store.domain.order;

import java.util.ArrayList;
import java.util.List;

/**
 * 구매한 상품과 할인 금액으로 최종 금액을 정한다.
 */
public class Receipt {
    private final List<Order> orders;
    private int finalAmount;

    public Receipt() {
        this.orders = new ArrayList<>();
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
