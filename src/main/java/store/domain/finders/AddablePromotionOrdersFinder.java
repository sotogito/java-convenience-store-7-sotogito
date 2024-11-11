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
            //if (!orderProduct.isSatisfiedGetPromotion()) {
            //    System.out.println("akswhrgkwl dkdkamd");
            //     changePromotionToGeneral(cart, orderProduct, nonAppliedQuantity);

            // }
            //적용이 된경우는 안됨여기 넘어오는건 프로모션 수량을 잘 지켰을대도 해당됨
            //changePromotionToGeneral(cart, orderProduct, nonAppliedQuantity);
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
