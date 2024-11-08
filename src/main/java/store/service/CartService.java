package store.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import store.domain.ConvenienceStoreroom;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;
import store.domain.order.Cart;
import store.domain.order.Order;
import store.domain.record.OrderForm;

public class CartService {
    private final ConvenienceStoreroom convenienceStoreroom;
    private final Cart cart;

    public CartService(ConvenienceStoreroom convenienceStoreroom, Cart cart) {
        this.convenienceStoreroom = convenienceStoreroom;
        this.cart = cart;
    }

    public void buy(List<OrderForm> orders, LocalDateTime nowDate) {
        List<Order> orderResult = changeToOrders(orders, nowDate);
        for (Order order : orderResult) {
            cart.addProduct(order);
        }
    }

    
    private List<Order> changeToOrders(List<OrderForm> orders, LocalDateTime nowDate) {
        List<Order> orderList = new ArrayList<>();

        for (OrderForm order : orders) {
            int quantity = order.quantity();
            Product product = getProduct(nowDate, order, quantity);

            addOrderList(orderList, product, quantity);
        }
        return orderList;
    }


    private Product getProduct(LocalDateTime nowDate, OrderForm order, int quantity) {
        Product product = convenienceStoreroom.findProductByName(order.name(), quantity);

        if (product instanceof PromotionProduct promotionProduct) {
            if (!promotionProduct.isValidDate(nowDate)) {
                product = convenienceStoreroom.getGeneralProduct(order.name(), quantity);
            }
        }
        return product;
    }

    private void addOrderList(List<Order> orderList, Product product, int quantity) {
        if (alreadyAddProduct(orderList, product, quantity)) {
            return;
        }
        orderList.add(new Order(product, quantity));
    }

    private boolean alreadyAddProduct(List<Order> orderList, Product product, int quantity) {
        for (Order ordered : orderList) {
            if (ordered.isSameProduct(product)) {
                ordered.updateQuantity(new Order(product, quantity));
                return true;
            }
        }
        return false;
    }

}
