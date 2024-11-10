package store.domain.items;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import camp.nextstep.edu.missionutils.DateTimes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;
import store.domain.policies.Promotion;

class ProductsTest {
    private Products products;

    ProductsTest() throws IOException {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("콜라", 1000, 10));
        productList.add(new Product("사이다", 1500, 8));
        productList.add(new PromotionProduct("콜라", 1000, 7,
                new Promotion("탄산2+1", 2, 1, DateTimes.now(), DateTimes.now().plusDays(2))));

        products = new Products(productList);
    }


    @DisplayName("주문한 제품과 수량이 있을 경우 상품 반환")
    @Test
    void getProductByNameAndQuantity() {
        Product product = products.getProductByNameAndQuantity("콜라", 1);

        String expected = "콜라";
        String actual = product.getName();

        assertEquals(expected, actual);
    }

    @DisplayName("주문한 제품이 존재하지 않은 경우 예외 처리")
    @Test
    void getProductByNameAndQuantity_제품_없음_예외_처리() {
        assertThatThrownBy(() -> products.getProductByNameAndQuantity("펩시", 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문한 제품 수량이 부족한 경우  예외 처리")
    @Test
    void getProductByNameAndQuantity_수량_부족_예외_처리() {
        assertThatThrownBy(() -> products.getProductByNameAndQuantity("콜라", 100))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름이 같은 상품 받아오기")
    @Test
    void getSameNameProducts() {
        List<Product> sameNameProducts = products.getSameNameProducts("콜라");

        assertEquals(2, sameNameProducts.size());
    }

    @DisplayName("이름과 개수로 일반 상품 받아오기")
    @Test
    void getGeneralProductByNameAndQuantity() {
        Product product = products.getGeneralProductByNameAndQuantity("콜라", 1);

        String expected = "콜라";
        String actual = product.getName();

        assertEquals(expected, actual);
    }


    @DisplayName("이름과 개수 프로모션 상품이 재고가 있는지 확인")
    @Test
    void isSufficientPromotionStockByNameAndQuantity() {
        assertTrue(products.isSufficientPromotionStockByNameAndQuantity(
                "콜라", 1));

        assertFalse(products.isSufficientPromotionStockByNameAndQuantity(
                "콜라", 100));
    }
    
}