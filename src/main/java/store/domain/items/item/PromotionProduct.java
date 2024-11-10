package store.domain.items.item;

import store.domain.items.Promotion;

public class PromotionProduct extends Product {
    private final Promotion promotion;
    private boolean isValidDate;

    public PromotionProduct(String name, int price, int quantity, Promotion promotion) {
        super(name, price, quantity);
        this.promotion = promotion;
        this.isValidDate = false;
    }

    public int getTotalPromotionProductQuantity(int totalPurchaseQuantity) {
        return promotion.calculateTotalPromotionProductQuantity(totalPurchaseQuantity);
    }

    public int getPromotionProductQuantity(int totalPurchaseQuantity) {
        return promotion.calculatePromotionProductQuantity(totalPurchaseQuantity);
    }


    public boolean isOverPromotionMinBuyQuantity(int noAppliedQuantity) {
        return promotion.isOverMin(noAppliedQuantity);
    }

    public boolean isValidDate() {
        isValidDate = promotion.isValidDate();
        return isValidDate;
    }


    public int getGetQuantity() {
        return promotion.getGetQuantity();
    }


    @Override
    public String toString() {
        return super.toString() + " " + promotion;
    }

}
