package store.service;

import java.util.List;
import store.domain.order.Cart;
import store.domain.order.Order;
import store.enums.AnswerWhether;

public class PromotionService {
    private final Cart cart;

    public PromotionService(Cart cart) {
        this.cart = cart;
    }

    //note @return 현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
    public List<Order> getShortagePromotionalStock() {
        return cart.getShortageStockPromotionOrders();
    }


    public void processShortagePromotionalStock(AnswerWhether answer, Order promotionOrder, int shortageQuantity) {
        if (AnswerWhether.findMeaningByAnswer(answer)) {
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

}
