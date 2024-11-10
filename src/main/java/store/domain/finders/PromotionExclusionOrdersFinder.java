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
                //더 피요한거같은데.. 위에는 그냥 구매 프로모션 > 재고 ㅡㅍㄹ로모션일때고
                result.add(orderProduct);
            }
        }
        return result;
    }

}
