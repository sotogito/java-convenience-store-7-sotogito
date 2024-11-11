package store.domain.finders;

import java.util.ArrayList;
import java.util.List;
import store.domain.ConvenienceStoreroom;
import store.domain.order.Cart;
import store.domain.order.Order;

public class AddablePromotionOrdersFinder {
    private final static int NON_APPLY_PROMOTION_MIN_QUANTITY = 1;

    public List<Order> find(Cart cart, List<Order> promotionOrders, ConvenienceStoreroom storeroom) {
        List<Order> result = new ArrayList<>();
        for (Order orderProduct : promotionOrders) {
            int nonAppliedQuantity = orderProduct.getNonAppliedPromotionQuantity();
            if (canAddPromotionProduct(storeroom, orderProduct, nonAppliedQuantity)) {
                result.add(orderProduct);
                continue;
            }
            //일반 상푸믕로 ㅂ녁
            changePromotionToGeneral(cart, orderProduct, nonAppliedQuantity);
        }
        return result;
    }

    private void changePromotionToGeneral(Cart cart, Order promotionOrder, int nonAppliedQuantity) {
        promotionOrder.deleteQuantity(nonAppliedQuantity);
        cart.changePromotionToGeneralAsShortage(promotionOrder, nonAppliedQuantity);

    }

    private boolean canAddPromotionProduct(ConvenienceStoreroom storeroom, Order orderProduct, int nonAppliedQuantity) {
        return isHaveNotYetApplyPromotion(nonAppliedQuantity)
                && orderProduct.isOverPromotionMinBuyQuantity(nonAppliedQuantity)
                && isSufficientPromotionStockAfterGetPromotionProduct(storeroom, orderProduct);
    }

    private boolean isHaveNotYetApplyPromotion(int nonAppliedQuantity) {
        return nonAppliedQuantity >= NON_APPLY_PROMOTION_MIN_QUANTITY;
    }

    private boolean isSufficientPromotionStockAfterGetPromotionProduct(
            ConvenienceStoreroom storeroom, Order orderProduct) {
        int afterPromotionQuantity = orderProduct.getSufficientStockAfterGetPromotionProduct();
        String productName = orderProduct.getProductName();
        return storeroom.isSufficientStockAfterGetPromotionProduct(productName, afterPromotionQuantity);
    }

}
