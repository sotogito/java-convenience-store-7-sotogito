package store.domain.order;

import java.time.LocalDateTime;
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
        List<Order> orders = new ArrayList<>();
        orders.addAll(generalProducts);
        orders.addAll(promotionProducts);

        Map<Product, Integer> productQuantity = new HashMap<>();
        for (Order order : orders) {
            productQuantity = order.updateProductQuantityMap(productQuantity);
        }
        storeroom.decreaseStock(productQuantity);
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

        /**
         * 만약3_1 인데 4개일겨우...
         * [콜라-4]
         * 프로모션 상품
         * Order [product=콜라, purchaseQuantity=4]
         *
         */
        for (Order orderProduct : promotionProducts) {
            int noAppliedQuantity = orderProduct.getQuantityNoPromotionApplied();
            if (noAppliedQuantity > 0 && orderProduct.isOverPromotionMinBuyQuantity(noAppliedQuantity)) {
                result.add(orderProduct);
            }
        }
        return result;
    }


    public void changePromotionToGeneral(Order promotionOrder, int shortageQuantity) {
        //해당 프로모션 상품을 수량만ㅋ틈 삭제하고 수량만큼 일반으로 추가

        promotionOrder.deleteQuantity(shortageQuantity);
        generalProducts.add(new Order(promotionOrder.getProduct(), shortageQuantity));

    }


    //fixme 프로모션 데이 안들어가는거 따로 관리해애됨
    public boolean canDatePromotion(LocalDateTime nowTime) {
        for (Order order : promotionProducts) {
            if (!order.isValidDate(nowTime)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasPromotionProduct() {
        return !promotionProducts.isEmpty();
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
