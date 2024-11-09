package store.domain.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.ConvenienceStoreroom;
import store.domain.items.item.Product;

//todo OrderProducts, OrderPromotionProducts, - Order 인터페이스로 묶기
public class Cart {
    private final List<Order> generalProducts;
    private final List<Order> promotionProducts;

    public Cart() {
        this.generalProducts = new ArrayList<>();
        this.promotionProducts = new ArrayList<>();
    }


    public void addProduct(Order order) {
        if (order.isPromotionProduct()) {
            promotionProducts.add(order);
            return;
        }
        generalProducts.add(order);
    }

    //note @return 현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
    public List<Order> getNonApplicablePromotionOrders() {
        List<Order> result = new ArrayList<>();

        for (Order orderProduct : promotionProducts) {
            if (orderProduct.getNonApplicablePromotionQuantity() > 0) {
                result.add(orderProduct);
            }
        }
        return result;
    }


    //note 현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
    public List<Order> getCanAddPromotionProductOrders(ConvenienceStoreroom storeroom) {
        List<Order> result = new ArrayList<>();
        for (Order orderProduct : promotionProducts) {
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
        int afterQuantityGetPromotionProduct = orderProduct.getSufficientStockAfterGetPromotionProduct();
        String productName = orderProduct.getProductName();
        return storeroom.isSufficientStockAfterGetPromotionProduct(productName, afterQuantityGetPromotionProduct);
    }

    public void changePromotionToGeneral(Order promotionOrder, int shortageQuantity) {
        promotionOrder.deleteQuantity(shortageQuantity);
        generalProducts.add(promotionOrder.createOrder(shortageQuantity));
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

    public int getGeneralProductPurchaseTotalAmount() {
        int totalAmount = 0;
        for (Order order : generalProducts) {
            totalAmount += order.calculateTotalAmount();
        }
        return totalAmount;
    }


    public Map<Product, Integer> getOrderPromotionProducts() {
        Map<Product, Integer> result = new HashMap<>();
        for (Order order : promotionProducts) {
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
        result.addAll(generalProducts);
        result.addAll(promotionProducts);
        return result;
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
