package store.domain.order;

import java.util.HashMap;
import java.util.Map;
import store.domain.items.item.Product;
import store.enums.AnswerWhether;

/**
 * 구매한 상품과 할인 금액으로 최종 금액을 정한다. discount는 인터페이스로 선언한다.
 */
public class Receipt {
    private final Cart cart;
    private final Map<Product, Integer> promotionProduct; //note 프로모션에서 증정품-수량만 가져와서 저장

    private int totalAmountBeforeDiscount; //note 증정품도 다 더함
    private int promotionDiscount; //note 증정품 가격
    private int membershipDiscount; //note 일반 상품 총가격에 30% - 최대 8000원
    private int finalAmount; //note 최종 금액

    /**
     * 1. 프로모션 할인 여부 확인 2. 일반 상품의 할인 적용 - membershipDiscount
     * <p>
     * 2. 증정품 상품과 수량 가져오기, 증정품 가격 저장 - promotionProduct,promotionDiscount 3. 전체 가격 구하기 - totalAmountBeforeDiscount 4. 최종
     * 금액 구하기 - finalAmount
     */
    public Receipt(Cart cart) {
        this.cart = cart;
        this.promotionProduct = new HashMap<>();
    }

    public void process(AnswerWhether answer) {
        calculateFinalAmount();
        System.out.println(totalAmountBeforeDiscount);
    }


    private void calculateFinalAmount() {
        totalAmountBeforeDiscount = cart.getTotalPrice();
    }

    public Map<Product, Integer> getAllOrder() {
        return cart.getProductQuantityMap();
    }

}
