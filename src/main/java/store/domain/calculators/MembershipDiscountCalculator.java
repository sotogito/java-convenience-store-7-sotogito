package store.domain.calculators;

import store.domain.order.Cart;
import store.domain.policies.MembershipDiscount;

public class MembershipDiscountCalculator implements DiscountCalculator {

    @Override
    public int calculate(Cart cart) {
        int totalAmount = cart.getTotalPrice();
        int promotionProductAmount = cart.getPromotionOrderAmount();
        int membershipDiscount = (int)
                ((totalAmount - promotionProductAmount) * MembershipDiscount.DISCOUNT_PERCENT.getDiscountValue());
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
