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
            if (orderProduct.isShortageStockPromotionProductThanPurchaseQuantity()) {
                result.add(orderProduct);
            }
            /**
             * 8-8
             * 지금 현재 재고가 프로모션 확정 수량을 ㄷ뺴고 프로모션을 받아야하는 상황일때
             * [구매수량-전체수량]이 0보다 클때
             */
        }
        return result;
    }

}
