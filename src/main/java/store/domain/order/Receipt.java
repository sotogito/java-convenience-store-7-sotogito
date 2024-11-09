package store.domain.order;

import java.util.HashMap;
import java.util.Map;
import store.domain.items.item.Product;
import store.enums.AnswerWhether;
import store.enums.MembershipDiscount;

/**
 * 구매한 상품과 할인 금액으로 최종 금액을 정한다. discount는 인터페이스로 선언한다.
 */
public class Receipt {
    private final Cart cart;
    private final Map<Product, Integer> promotionProduct; //note 프로모션에서 증정품-수량만 가져와서 저장
    private int totalPurchaseCount;
    private int totalAmountBeforeDiscount; //note 증정품도 다 더함
    private int promotionDiscount; //note 증정품 가격
    private int membershipDiscount; //note 일반 상품 총가격에 30% - 최대 8000원
    private int finalAmount; //note 최종 금액

    public Receipt(Cart cart) {
        this.cart = cart;
        this.promotionProduct = new HashMap<>();
    }

    public void process(AnswerWhether answer) {
        setFinalAmount();
        updatePromotionProduct();
        calculatePromotionDiscountAmount();
        calculateMembershipDiscountAmount(answer);
        calculateTotalPurchaseCount();
        calculateFinalAmount();
    }


    private void calculateTotalPurchaseCount() {
        totalPurchaseCount = cart.getTotalPurchaseCount();
    }

    private void calculateFinalAmount() {
        finalAmount = totalAmountBeforeDiscount - (membershipDiscount + promotionDiscount);
    }

    private void calculateMembershipDiscountAmount(AnswerWhether answer) {
        if (AnswerWhether.findMeaningByAnswer(answer)) {
            int generalPurchaseAmount = cart.getGeneralProductPurchaseTotalAmount();
            membershipDiscount = (int) (generalPurchaseAmount * MembershipDiscount.DISCOUNT_PERCENT.getDiscountValue());
            checkMaxDiscountAmount();
            return;
        }
        membershipDiscount = MembershipDiscount.NONE.getValue();
    }

    private void checkMaxDiscountAmount() {
        int max = MembershipDiscount.MAX_DISCOUNT_AMOUNT.getValue();
        if (membershipDiscount > max) {
            membershipDiscount = max;
        }
    }

    private void calculatePromotionDiscountAmount() {
        for (Map.Entry<Product, Integer> entry : promotionProduct.entrySet()) {
            int productPrice = entry.getKey().getPrice();
            int count = entry.getValue();
            promotionDiscount += (productPrice * count);
        }
    }

    private void updatePromotionProduct() {
        promotionProduct.putAll(cart.getOrderPromotionProducts());
    }

    private void setFinalAmount() {
        totalAmountBeforeDiscount = cart.getTotalPrice();
    }


    public Map<Product, Integer> getAllOrder() {
        return cart.getAllOrderProductQuantity();
    }

    public Map<Product, Integer> getPromotionProduct() {
        return promotionProduct;
    }

    public int getTotalPurchaseCount() {
        return totalPurchaseCount;
    }

    public int getTotalAmountBeforeDiscount() {
        return totalAmountBeforeDiscount;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public void clearReceipt() {
        promotionProduct.clear();
        totalAmountBeforeDiscount = 0;
        promotionDiscount = 0;
        membershipDiscount = 0;
        finalAmount = 0;
    }

}
