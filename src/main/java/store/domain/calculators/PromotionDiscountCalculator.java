package store.domain.calculators;

import java.util.Map;
import store.domain.items.item.Product;
import store.domain.order.Cart;

public class PromotionDiscountCalculator implements DiscountCalculator {
    
    @Override
    public int calculate(Cart cart) {
        int totalPromotions = 0;
        for (Map.Entry<Product, Integer> entry : cart.getOrderPromotionProducts().entrySet()) {
            int productPrice = entry.getKey().getPrice();
            int count = entry.getValue();
            totalPromotions += (productPrice * count);
        }
        return totalPromotions;
    }

}
