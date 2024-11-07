package store.domain.items.item;

import java.time.LocalDateTime;
import store.domain.items.Promotion;

public class PromotionProduct extends Product {
    private final Promotion promotion;
    private boolean isValidDate;

    public PromotionProduct(String name, int price, int quantity, Promotion promotion) {
        super(name, price, quantity);
        this.promotion = promotion;
        this.isValidDate = false;
    }

    public int getCorrectQuantity(int totalPurchaseQuantity) {
        return promotion.calculateCorrectQuantity(totalPurchaseQuantity);
    }


    public boolean isOverPromotionMinBuyQuantity(int noAppliedQuantity) {
        return promotion.isOverMin(noAppliedQuantity);
    }

    public boolean isValidDate(LocalDateTime nowTime) {
        isValidDate = promotion.isValidDate(nowTime);
        return isValidDate;
    }


    public int getGetQuantity() {
        return promotion.getGetQuantity();
    }

    public boolean getIsValidDate() {
        return isValidDate;
    }


    @Override
    public String toString() {
        return super.toString() + " " + promotion;
    }


}
