package store.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import camp.nextstep.edu.missionutils.DateTimes;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.ConvenienceStoreroom;
import store.domain.finders.AddablePromotionOrdersFinder;
import store.domain.finders.PromotionExclusionOrdersFinder;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;
import store.domain.policies.Promotion;
import store.domain.reader.ProductReader;
import store.domain.reader.PromotionReader;

class CartTest {
    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @DisplayName("일반 상품 일반 상품으로 추가")
    @Test
    void addProduct_일반상품() {
        Order order = new Order(new Product("콜라", 1000, 10), 5);
        cart.addProduct(order);

        Map<Product, Integer> promotions = cart.getOrderPromotionProducts();
        Map<Product, Integer> all = cart.getAllOrderProductQuantity();

        assertEquals(0, promotions.size());
        assertEquals(1, all.size());
    }

    @DisplayName("프로모션 상품 프로모션 상품으로 추가")
    @Test
    void addProduct_프로모션상품() {
        Order order = new Order(new PromotionProduct("콜라", 1000, 7,
                new Promotion("탄산2+1", 2, 1, DateTimes.now(), DateTimes.now().plusDays(2))), 5);
        cart.addProduct(order);

        Map<Product, Integer> promotions = cart.getOrderPromotionProducts();

        assertEquals(1, promotions.size());
    }

    @DisplayName("주문이 프로모션 상품의 재고보다 많은 시 일반 상품으로 계산해야되는 주문 반환")
    @Test
    void getNonApplicablePromotionOrders() {
        Order order = new Order(new PromotionProduct("콜라", 1000, 7,
                new Promotion("탄산2+1", 2, 1, DateTimes.now(), DateTimes.now().plusDays(2))), 8);
        cart.addProduct(order);

        List<Order> result = cart.getNonApplicablePromotionOrders(new PromotionExclusionOrdersFinder());

        assertTrue(result.contains(order));
    }

    @DisplayName("프로모션으로 상품이 추가 가능한 상품 반환")
    @Test
    void getAddablePromotionProductOrders() throws IOException {
        Order order = new Order(new PromotionProduct("콜라", 1000, 7,
                new Promotion("탄산2+1", 2, 1, DateTimes.now(), DateTimes.now().plusDays(2))), 2);
        cart.addProduct(order);

        List<Order> result = cart.getAddablePromotionProductOrders(new AddablePromotionOrdersFinder(),
                new ConvenienceStoreroom(new PromotionReader(), new ProductReader()));

        assertTrue(result.contains(order));
    }

    @Test
    void getTotalPurchaseCount() {
        Order order1 = new Order(new PromotionProduct("콜라", 1000, 7,
                new Promotion("탄산2+1", 2, 1, DateTimes.now(), DateTimes.now().plusDays(2))), 3);
        Order order2 = new Order(new Product("콜라", 500, 7), 2);
        cart.addProduct(order1);
        cart.addProduct(order2);

        int expected = 5;
        int actual = cart.getTotalPurchaseCount();
        assertEquals(expected, actual);
    }

    @Test
    void getTotalPrice() {
        Order order1 = new Order(new PromotionProduct("콜라", 1000, 7,
                new Promotion("탄산2+1", 2, 1, DateTimes.now(), DateTimes.now().plusDays(2))), 3);
        Order order2 = new Order(new Product("콜라", 500, 7), 2);
        cart.addProduct(order1);
        cart.addProduct(order2);

        int expected = 4000;
        int actual = cart.getTotalPrice();
        assertEquals(expected, actual);
    }

    @Test
    void getPromotionOrderAmount() {
        Order order1 = new Order(new PromotionProduct("콜라", 1000, 7,
                new Promotion("탄산2+1", 2, 1, DateTimes.now(), DateTimes.now().plusDays(2))), 3);
        Order order2 = new Order(new Product("콜라", 500, 7), 2);
        cart.addProduct(order1);
        cart.addProduct(order2);

        int expected = 3000;
        int actual = cart.getPromotionOrderAmount();
        assertEquals(expected, actual);
    }

    @Test
    void clearCart() {
        Order order = new Order(new Product("콜라", 1000, 10), 5);
        cart.addProduct(order);

        cart.clearCart();
        assertEquals(0, cart.getAllOrderProductQuantity().size());
    }
    
}