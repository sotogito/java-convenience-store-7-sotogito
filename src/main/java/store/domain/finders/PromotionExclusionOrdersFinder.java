package store.domain.finders;

import java.util.ArrayList;
import java.util.List;
import store.domain.order.Order;

public class PromotionExclusionOrdersFinder {

    public List<Order> find(List<Order> promotionOrders) {
        List<Order> result = new ArrayList<>();
        for (Order orderProduct : promotionOrders) {
            if (orderProduct.isOrderPromotionOutIfStock()) {
                continue;
            }
            checkAddable(orderProduct, result);
        }
        return result;
    }

    private void checkAddable(Order orderProduct, List<Order> result) {
        if (orderProduct.isShortageStockPromotionProductThanPurchaseQuantity()) {
            result.add(orderProduct);
        }
    }

}
