package store.service;

import java.util.List;
import store.domain.ConvenienceStoreroom;
import store.domain.finders.AddablePromotionOrdersFinder;
import store.domain.finders.PromotionExclusionOrdersFinder;
import store.domain.order.Cart;
import store.domain.order.Order;
import store.enums.AnswerWhether;

public class PromotionService {
    private final PromotionExclusionOrdersFinder promotionExclusionOrdersFinder;
    private final AddablePromotionOrdersFinder addablePromotionOrdersFinder;
    private final ConvenienceStoreroom convenienceStoreroom;
    private final Cart cart;

    public PromotionService(ConvenienceStoreroom convenienceStoreroom, Cart cart) {
        this.convenienceStoreroom = convenienceStoreroom;
        this.cart = cart;
        promotionExclusionOrdersFinder = new PromotionExclusionOrdersFinder();
        addablePromotionOrdersFinder = new AddablePromotionOrdersFinder();
    }


    //note @return 현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
    public List<Order> getNonApplicablePromotionOrders() {
        return cart.getNonApplicablePromotionOrders(promotionExclusionOrdersFinder);
    }

    public void handleNonApplicablePromotionOrder(AnswerWhether answer, Order promotionOrder, int shortageQuantity) {
        if (AnswerWhether.isYes(answer)) {
            promotionOrder.deleteQuantity(shortageQuantity);
            cart.changePromotionToGeneralAsShortage(promotionOrder, shortageQuantity);
            return;
        }
        promotionOrder.deleteQuantity(shortageQuantity);
    }


    //note 현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
    public List<Order> getAddablePromotionProductOrders() {
        return cart.getAddablePromotionProductOrders(addablePromotionOrdersFinder, convenienceStoreroom);
    }

    public void handleCanAddPromotionProductOrder(AnswerWhether answer, Order promotionOrder, int needAddQuantity) {
        if (AnswerWhether.isYes(answer)) {
            promotionOrder.updatePromotionProductQuantity(needAddQuantity);
        }
    }

}
