package store.domain.items.item;

import java.time.LocalDateTime;
import store.domain.policies.Promotion;

public class PromotionProduct extends Product {
    private final Promotion promotion;

    public PromotionProduct(String name, int price, int quantity, Promotion promotion) {
        super(name, price, quantity);
        this.promotion = promotion;
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

    public boolean isValidDate(LocalDateTime nowDate) {
        return promotion.isValidDate(nowDate);
    }


    public int getGetQuantity() {
        return promotion.getGetQuantity();
    }


    @Override
    public String toString() {
        return super.toString() + " " + promotion;
    }

}
