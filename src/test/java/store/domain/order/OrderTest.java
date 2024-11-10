package store.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import camp.nextstep.edu.missionutils.DateTimes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;
import store.domain.policies.Promotion;

class OrderTest {
    private Order general;
    private Order promotion;

    @BeforeEach
    void setUp() {
        general = new Order(new Product("콜라", 1000, 10), 5);
        promotion = new Order(new PromotionProduct("콜라", 1000, 7,
                new Promotion("탄산2+1", 2, 1, DateTimes.now(), DateTimes.now().plusDays(2))), 5);
    }


    @DisplayName("프로모션 상품 추가 가능 증정품 개수")
    @Test
    void getNeedAddQuantity() {
        assertEquals(1, promotion.getNeedAddQuantity());
    }

    @DisplayName("주문한 프로모션이 적용되지 않은 수량 개수")
    @Test
    void getNonAppliedPromotionQuantity() {
        assertEquals(2, promotion.getNonAppliedPromotionQuantity());
    }

    @Test
    void getNonApplicablePromotionProductQuantity() {
        assertEquals(2, promotion.getNonAppliedPromotionQuantity());
    }

    @DisplayName("주문보다 프로모션 구매 상품이 부족한지 확인")
    @Test
    void isShortageStockPromotionProductThanPurchaseQuantity() {
        assertFalse(promotion.isShortageStockPromotionProductThanPurchaseQuantity());
    }

    @Test
    void isOverPromotionMinBuyQuantity() {
        assertFalse(promotion.isOverPromotionMinBuyQuantity(1));
    }

    @Test
    void isOrderPromotionOutIfStock() {
        assertFalse(promotion.isOrderPromotionOutIfStock());
    }

    @Test
    void updatePromotionProductQuantity() {
        promotion.updatePromotionProductQuantity(2);
        assertEquals(7, promotion.getPurchaseQuantity());
    }

    @Test
    void deleteQuantity() {
        promotion.deleteQuantity(2);
        assertEquals(3, promotion.getPurchaseQuantity());
    }

    @DisplayName("이미 존재하는 상품일 경우 수량만 업데이트")
    @Test
    void updateQuantity() {
        general.updateQuantity(general);
        assertEquals(10, general.getPurchaseQuantity());
    }

    @Test
    void getSufficientStockAfterGetPromotionProduct() {
    }

    @Test
    void calculateTotalAmount() {
    }

    @Test
    void getPromotionOrderAmount() {
    }

    @Test
    void isPromotionProduct() {
    }

    @Test
    void isSameProduct() {
    }

    @Test
    void createOrder() {
    }

    @Test
    void getProductName() {
    }

    @Test
    void getPurchaseQuantity() {
    }

    @Test
    void updateAllProductQuantity() {
    }

    @Test
    void updateOnlyPromotionProductAndQuantity() {
    }
}