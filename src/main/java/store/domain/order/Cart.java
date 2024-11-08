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

    /**
     * 이름이 같은 애를 찾아서 총 구매 수량을 ... 그아므에 sotreromm에서 차감
     * <p>
     * 일반ㅅㅇ품은 그냥 차감
     */
    public void decreaseProductQuantity(ConvenienceStoreroom storeroom) {
        //fixme [감자칩-1],[초코바-6]
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
        for (Order order : generalProducts) { //todo 프로모션 가격 조정해야됨
            totalPrice += order.calculateTotalAmount();
        }
        for (Order order : promotionProducts) {
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
