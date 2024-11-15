package store.domain;

import java.util.HashMap;
import java.util.Map;
import store.domain.calculators.DiscountCalculator;
import store.domain.calculators.MembershipDiscountCalculator;
import store.domain.calculators.PromotionDiscountCalculator;
import store.domain.items.item.Product;
import store.domain.order.Cart;
import store.domain.policies.MembershipDiscount;


public class Receipt {
    private final Cart cart;
    private final Map<Product, Integer> promotionProduct;
    private int totalPurchaseCount;
    private int totalAmountBeforeDiscount;
    private int promotionDiscount;
    private int membershipDiscount;
    private int finalAmount;

    private final DiscountCalculator membershipCalculator;
    private final DiscountCalculator promotionCalculator;

    public Receipt(Cart cart) {
        this.cart = cart;
        this.promotionProduct = new HashMap<>();
        resetAmount();
        membershipCalculator = new MembershipDiscountCalculator();
        promotionCalculator = new PromotionDiscountCalculator();
    }

    public void process(Boolean applyMembershipDiscount) {
        setFinalAmount();
        updatePromotionProduct();
        calculatePromotionDiscountAmount();
        calculateMembershipDiscountAmount(applyMembershipDiscount);
        calculateTotalPurchaseCount();
        calculateFinalAmount();
    }

    private void setFinalAmount() {
        totalAmountBeforeDiscount = cart.getTotalPrice();
    }

    private void updatePromotionProduct() {
        promotionProduct.putAll(cart.getOrderPromotionProducts());
    }

    private void calculatePromotionDiscountAmount() {
        promotionDiscount = promotionCalculator.calculate(cart);
    }

    private void calculateMembershipDiscountAmount(Boolean applyMembershipDiscount) {
        if (applyMembershipDiscount) {
            membershipDiscount = membershipCalculator.calculate(cart);
            return;
        }
        membershipDiscount = MembershipDiscount.NONE.get();
    }

    private void calculateTotalPurchaseCount() {
        totalPurchaseCount = cart.getTotalPurchaseCount();
    }

    private void calculateFinalAmount() {
        finalAmount = totalAmountBeforeDiscount - (membershipDiscount + promotionDiscount);
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
        resetAmount();
    }

    private void resetAmount() {
        totalPurchaseCount = 0;
        totalAmountBeforeDiscount = 0;
        promotionDiscount = 0;
        membershipDiscount = 0;
        finalAmount = 0;
    }

}
