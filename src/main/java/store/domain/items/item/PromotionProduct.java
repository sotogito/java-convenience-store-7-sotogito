package store.domain.items.item;

import java.time.LocalDateTime;
import store.domain.items.Promotion;

public class PromotionProduct extends Product {
    private final Promotion promotion;

    public PromotionProduct(String name, int price, int quantity, Promotion promotion) {
        super(name, price, quantity);
        this.promotion = promotion;
    }

    public boolean isValidDate(LocalDateTime nowTime) {
        return promotion.isValidDate(nowTime);
    }

    public int getInsufficientQuantity(int totalPurchaseQuantity) {
        return promotion.calculateInsufficientQuantity(totalPurchaseQuantity);
    }

    @Override
    public String toString() {
        return super.toString() + " " + promotion;
    }


}