package store.domain.policies;

import java.util.List;
import store.constants.messages.ErrorMessage;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public Promotion getPromotionByName(String name) {
        for (Promotion promotion : promotions) {
            if (promotion.isSameName(name)) {
                return promotion;
            }
        }
        throw new IllegalStateException(ErrorMessage.NOT_EXIST_PROMOTION.get());
    }

}
