package store.domain.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.ConvenienceStoreroom;
import store.domain.items.item.Product;

public class Cart {
    private final List<Order> generalProducts;
    private final List<Order> promotionProducts;

    public Cart() {
        this.generalProducts = new ArrayList<>();
        this.promotionProducts = new ArrayList<>();
    }

    public Map<Product, Integer> getPromotionProducts() {
        Map<Product, Integer> result = new HashMap<>();
        for (Order order : promotionProducts) {
            int promotionCount = order.getPromotionProductQuantity();
            if (promotionCount > 0) {
                result.put(order.getProduct(), promotionCount);
            }
        }
        return result;

    }

    public void decreaseProductQuantity(ConvenienceStoreroom storeroom) {
        storeroom.decreaseStock(getProductQuantityMap());
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
        promotionOrder.deleteQuantity(shortageQuantity);
        generalProducts.add(new Order(promotionOrder.getProduct(), shortageQuantity));

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
        for (Order order : getAllOrders()) {
            totalPrice += order.calculateTotalAmount();
        }
        return totalPrice;
    }

    public void clearCart() {
        generalProducts.clear();
        promotionProducts.clear();
    }

    public Map<Product, Integer> getProductQuantityMap() {
        Map<Product, Integer> result = new HashMap<>();

        for (Order order : getAllOrders()) {
            order.updateProductQuantityMap(result);
        }
        return result;
    }

    private List<Order> getAllOrders() {
        List<Order> result = new ArrayList<>();
        result.addAll(generalProducts);
        result.addAll(promotionProducts);
        return result;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Order order : generalProducts) {
            builder.append("일반 상품\n");
            builder.append(order);
        }
        for (Order order : promotionProducts) {
            builder.append("프로모션 상품\n");
            builder.append(order);
        }
        return builder.toString();
    }

}
