package store.service;

import java.util.ArrayList;
import java.util.List;
import store.domain.ConvenienceStoreroom;
import store.domain.order.Order;

public class AddablePromotionOrdersFinder {

    public List<Order> find(List<Order> promotionOrders, ConvenienceStoreroom storeroom) {
        List<Order> result = new ArrayList<>();
        for (Order orderProduct : promotionOrders) {
            int nonAppliedQuantity = orderProduct.getNonAppliedPromotionQuantity();
            if (canAddPromotionProduct(storeroom, orderProduct, nonAppliedQuantity)) {
                result.add(orderProduct);
            }
        }
        return result;
    }

    private boolean canAddPromotionProduct(ConvenienceStoreroom storeroom, Order orderProduct, int noAppliedQuantity) {
        return noAppliedQuantity > 0 && orderProduct.isOverPromotionMinBuyQuantity(noAppliedQuantity)
                && isSufficientPromotionStockAfterGetPromotionProduct(storeroom, orderProduct);
    }

    private boolean isSufficientPromotionStockAfterGetPromotionProduct(
            ConvenienceStoreroom storeroom, Order orderProduct) {
        int afterPromotionQuantity = orderProduct.getSufficientStockAfterGetPromotionProduct();
        String productName = orderProduct.getProductName();
        return storeroom.isSufficientStockAfterGetPromotionProduct(productName, afterPromotionQuantity);
    }

}
