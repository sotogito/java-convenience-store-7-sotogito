package store.domain.items.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.policies.Promotion;

class PromotionProductTest {
    private PromotionProduct promotionProduct;

    @BeforeEach
    void setUp() {
        promotionProduct = new PromotionProduct("콜라", 1000, 100
                , new Promotion("탄산2+1", 2, 1, LocalDateTime.now(), LocalDateTime.now().plusDays(2)));
    }

    @DisplayName("구매 수량에서 프로모션으로 얻을 수 있는 수량 구하기")
    @Test
    void getTotalPromotionProductQuantity() {
        int expected = 6;
        int actual = promotionProduct.getTotalPromotionProductQuantity(7);

        assertEquals(expected, actual);
    }

    @DisplayName("구매 수량에서 프로모션 증정 상품의 수량 구하기")
    @Test
    void getPromotionProductQuantity() {
        int expected = 2;
        int actual = promotionProduct.getPromotionProductQuantity(6);

        assertEquals(expected, actual);
    }

    @DisplayName("프로모션 증정품을 받기 위한 최소한의 수량")
    @Test
    void isOverPromotionMinBuyQuantity() {
        assertTrue(promotionProduct.isOverPromotionMinBuyQuantity(2));
    }

    @DisplayName("프로모션 기간내")
    @Test
    void isValidDate() {
        assertTrue(promotionProduct.isValidDate(DateTimes.now()));
    }

    @DisplayName("프로모션 기간 밖")
    @Test
    void isValidDate_false() {
        assertFalse(promotionProduct.isValidDate(DateTimes.now().minusDays(5)));
    }

}