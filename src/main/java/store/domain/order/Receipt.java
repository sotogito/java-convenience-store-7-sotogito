package store.domain.order;

/**
 * 구매한 상품과 할인 금액으로 최종 금액을 정한다.
 */
public class Receipt {
    private final Cart cart;
    private int finalAmount;

    public Receipt(Cart cart) {
        this.cart = cart;
    }


    public boolean hasPromotionProduct() {
        return cart.hasPromotionProduct();
    }

    public void addOrder(Order order) {
        cart.addProduct(order);
    }

    public void calculateFinalAmount() {
        finalAmount = cart.getTotalPrice();
    }

    public int getFinalAmount() {
        return finalAmount;
    }


}
