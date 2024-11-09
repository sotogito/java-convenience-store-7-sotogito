package store.domain.calculators;

import store.domain.order.Cart;
import store.enums.MembershipDiscount;

public class MembershipDiscountCalculator implements DiscountCalculator {

    @Override
    public int calculate(Cart cart) {
        int generalPurchaseAmount = cart.getGeneralProductPurchaseTotalAmount();
        int membershipDiscount = (int) (generalPurchaseAmount * MembershipDiscount.DISCOUNT_PERCENT.getDiscountValue());
        return checkMaxDiscountAmount(membershipDiscount);
    }

    private int checkMaxDiscountAmount(int membershipDiscount) {
        int max = MembershipDiscount.MAX_DISCOUNT_AMOUNT.get();
        if (membershipDiscount > max) {
            return max;
        }
        return membershipDiscount;
    }

}
