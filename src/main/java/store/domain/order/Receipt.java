package store.domain.order;

/**
 * 구매한 상품과 할인 금액으로 최종 금액을 정한다.
 */
public class Receipt {
    private final Cart cart;
    private int finalAmount;
    private int promotionDiscount;
    private int membershipDiscount;

    public Receipt(Cart cart) {
        this.cart = cart;
    }


    public void calculateFinalAmount() {
        finalAmount = cart.getTotalPrice();
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    /**
     * 상품에 따른 가격 계산만
     */


}
