package store.domain.calculators;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.util.Map;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;
import store.domain.order.Cart;

public class PromotionDiscountCalculator implements DiscountCalculator {

    @Override
    public int calculate(Cart cart) {
        LocalDateTime nowDate = DateTimes.now();
        int totalPromotions = 0;
        for (Map.Entry<Product, Integer> entry : cart.getOrderPromotionProducts().entrySet()) {
            PromotionProduct product = (PromotionProduct) entry.getKey();
            if (product.isValidDate(nowDate)) {
                int productPrice = entry.getKey().getPrice();
                int count = entry.getValue();
                totalPromotions += calculateAmountOfOne(productPrice, count);
            }
        }
        return totalPromotions;
    }

    private int calculateAmountOfOne(int productPrice, int count) {
        return productPrice * count;
    }

}
