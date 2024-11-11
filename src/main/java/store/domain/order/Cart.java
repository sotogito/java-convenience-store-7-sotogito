package store.domain.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.ConvenienceStoreroom;
import store.domain.finders.AddablePromotionOrdersFinder;
import store.domain.finders.PromotionExclusionOrdersFinder;
import store.domain.items.item.Product;

public class Cart {
    private final List<Order> generalOrders;
    private final List<Order> promotionOrders;

    public Cart() {
        this.generalOrders = new ArrayList<>();
        this.promotionOrders = new ArrayList<>();
    }

    public void addProduct(Order order) {
        if (order.isPromotionProduct()) {
            promotionOrders.add(order);
            return;
        }
        generalOrders.add(order);
    }

    public List<Order> getNonApplicablePromotionOrders(PromotionExclusionOrdersFinder finder) {
        return finder.find(promotionOrders);
    }

    public List<Order> getAddablePromotionProductOrders(AddablePromotionOrdersFinder finder,
                                                        ConvenienceStoreroom storeroom) {
        return finder.find(this, promotionOrders, storeroom);
    }

    public void changePromotionToGeneralAsShortage(Order promotionOrder, int shortageQuantity) {
        generalOrders.add(promotionOrder.createOrder(shortageQuantity));
    }

    public void decreasePurchasedProductQuantity(ConvenienceStoreroom storeroom) {
        storeroom.decreaseStock(getAllOrderProductQuantity());
    }


    public int getTotalPurchaseCount() {
        int totalCount = 0;
        for (Order order : getAllOrders()) {
            totalCount += order.getPurchaseQuantity();
        }
        return totalCount;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (Order order : getAllOrders()) {
            totalPrice += order.calculateTotalAmount();
        }
        return totalPrice;
    }

    public int getPromotionOrderAmount() {
        int totalAmount = 0;
        for (Order order : promotionOrders) {
            totalAmount += order.getPromotionOrderAmount();
        }
        return totalAmount;
    }


    public Map<Product, Integer> getOrderPromotionProducts() {
        Map<Product, Integer> result = new HashMap<>();
        for (Order order : promotionOrders) {
            order.updateOnlyPromotionProductAndQuantity(result);
        }
        return result;
    }

    public Map<Product, Integer> getAllOrderProductQuantity() {
        Map<Product, Integer> result = new HashMap<>();

        for (Order order : getAllOrders()) {
            order.updateAllProductQuantity(result);
        }
        return result;
    }

    private List<Order> getAllOrders() {
        List<Order> result = new ArrayList<>();
        result.addAll(generalOrders);
        result.addAll(promotionOrders);
        return result;
    }


    public void clearCart() {
        generalOrders.clear();
        promotionOrders.clear();
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Order order : generalOrders) {
            builder.append("일반 상품\n");
            builder.append(order);
        }
        for (Order order : promotionOrders) {
            builder.append("프로모션 상품\n");
            builder.append(order);
        }
        return builder.toString();
    }

}
