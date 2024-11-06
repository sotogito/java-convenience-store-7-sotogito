package store.domain.items;

import java.util.List;

public class Promotions implements Item {
    private List<Promotion> promotions;

    //fixme 스스로 생성하게해야되나?
    public Promotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }


    public Promotion getPromotionByName(String name) {
        for (Promotion promotion : promotions) {
            if (promotion.isSameName(name)) {
                return promotion;
            }
        }
        throw new IllegalStateException("존재하지 않는 프로모션");
    }
}
