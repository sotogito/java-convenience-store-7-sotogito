package store.domain.calculators;

import store.domain.order.Cart;

public interface DiscountCalculator {
    int calculate(Cart cart);

}
