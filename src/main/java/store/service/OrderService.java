package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import store.domain.ConvenienceStoreroom;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;
import store.domain.order.Cart;
import store.domain.order.Order;
import store.domain.order.Receipt;
import store.domain.record.OrderForm;
import store.enums.AnswerWhether;

public class OrderService {
    /**
     * 사용자가 [상품-개수]를 입력하면 Order를 생성하고 수량을 체크 - 기본
     */

    private final ConvenienceStoreroom convenienceStoreroom; //재고확인
    private final Receipt receipt; //추가 저장
    private final Cart cart;
    private boolean isPurchase;


    public OrderService(ConvenienceStoreroom convenienceStoreroom, Receipt receipt, Cart cart) {
        this.convenienceStoreroom = convenienceStoreroom;
        this.receipt = receipt;
        this.cart = cart;
        isPurchase = true;
    }

    public boolean isPurchase() {
        return isPurchase;
    }

    public void stopPurchase() {
        isPurchase = false;
    }

    public void clearOrderList() {
        cart.clearCart();
    }

    public boolean isContainPromotionProduct() {
        return cart.hasPromotionProduct();
    }

    public boolean isPromotionDate(LocalDateTime nowTime) {
        return cart.canDatePromotion(nowTime);
    }


    //note @return 현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
    public List<Order> getShortagePromotionalStock() {
        return cart.getShortageStockPromotionOrders();
    }


    public void processShortagePromotionalStock(AnswerWhether answer, Order promotionOrder, int shortageQuantity) {
        if (AnswerWhether.findMeaningByAnswer(answer)) {
            //todo 프로모션 -> 일반으로 추가하는 로직 -> 그래야 멤버십 할인 가능함
            cart.changePromotionToGeneral(promotionOrder, shortageQuantity);
            return;
        }
        promotionOrder.deleteQuantity(shortageQuantity);
    }


    //note 현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
    public List<Order> getLackQuantityPromotionOrders() {
        return cart.getLackQuantityPromotionOrders();
    }

    public void processLackQuantityPromotionOrders(AnswerWhether answer, Order promotionOrder, int needAddQuantity) {
        if (AnswerWhether.findMeaningByAnswer(answer)) {
            promotionOrder.updatePromotionProductQuantity(needAddQuantity);
        }
    }


    public void handlePurchaseProgress(String inputAnswer) {
        //this.isPurchase = AnswerWhether.findMeaningByAnswer();
    }

    public void handleMembership(String inputAnswer) {
        //boolean answer = AnswerWhether.findMeaningByAnswer();
        //멤버십 계산 및 적용
        // 아니면 바로 출력
    }

    public void decreaseStockInConvenienceStore() {
        //프로모션 기간일 경우
        //프로모션 상품 재고 먼저 삭제
        cart.decreaseProductQuantity(convenienceStoreroom);
    }


    public void buy(List<OrderForm> orders) { //todo try-catch 다시 입력받기. 여기서 예외처리 다해야됨
        List<Order> orderResult = changeToOrders(orders);
        for (Order order : orderResult) {
            cart.addProduct(order);
        }
    }


    private List<Order> changeToOrders(List<OrderForm> orders) {
        List<Order> orderList = new ArrayList<>();

        //fixme 프로모션 기간이 유효한지를 먼저 판단하고, 일반상품or 프로모션 상품
        for (OrderForm order : orders) {
            int quantity = order.quantity();
            Product product = convenienceStoreroom.findProductByName(order.name(), quantity);

            if (product instanceof PromotionProduct promotionProduct) {
                if (!promotionProduct.isValidDate(DateTimes.now())) {
                    product = convenienceStoreroom.getGeneralProduct(order.name(), quantity);
                }
            }

            if (alreadyAddProduct(orderList, product, quantity)) {
                continue;
            }
            orderList.add(new Order(product, quantity));

        }
        return orderList;
    }

    private boolean alreadyAddProduct(List<Order> orderList, Product product, int quantity) {
        for (Order ordered : orderList) {
            if (ordered.isSameProduct(product)) {
                ordered.updateQuantity(new Order(product, quantity));
                return true;
            }
        }
        return false;
    }


    public void printCart() {
        System.out.println(cart);
    }

}
