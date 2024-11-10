package store.domain.items.item;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("콜라", 1000, 3);
    }

    @DisplayName("프로모션 총 상품과 실제 수량 차이 반환")
    @Test
    void calculateQuantityDeduction() {
        int expected = 5;
        int actual = product.calculateQuantityDeduction(8);

        assertEquals(expected, actual);
    }

    @DisplayName("구매한 수량만큼 재고 차감")
    @Test
    void decreaseQuantity() {
        product.decreaseQuantity(1);
        int expected = 2;
        int actual = product.getQuantity();

        assertEquals(expected, actual);
    }

    @DisplayName("재고가 부족한 경우 예외 처리")
    @Test
    void decreaseQuantity_예외_처리() {
        assertThatThrownBy(() -> product.decreaseQuantity(100))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void isOutOfStock() {
        product.decreaseQuantity(product.getQuantity());
        assertTrue(product.isOutOfStock());
    }

    @Test
    void isSufficientStock() {
        assertTrue(product.isSufficientStock(2));
    }

    @Test
    void isSameName() {
        assertTrue(product.isSameName("콜라"));
    }
    
}