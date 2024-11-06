package store.domain.order;

import java.time.LocalDateTime;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;

public class Order {
    private final Product product;
    private int purchaseQuantity;

    public Order(Product product, int purchaseQuantity) {
        this.product = product;
        this.purchaseQuantity = purchaseQuantity;
    }

    public String getProductName() {
        return product.getName();
    }

    public boolean isValidDate(LocalDateTime nowTime) {
        if (product instanceof PromotionProduct) {
            return ((PromotionProduct) product).isValidDate(nowTime);
        }
        return false;
    }

    public int getInsufficientQuantity() { //note 출력과 재 업데이트에 필요
        if (product instanceof PromotionProduct) {
            return ((PromotionProduct) product).getInsufficientQuantity(purchaseQuantity);
        }
        return 0;
    }

    public void updatePromotionProductQuantity(int quantity) {
        if (product instanceof PromotionProduct) {
            this.purchaseQuantity += quantity;
        }
    }

    public boolean isPromotionProduct() {
        return product instanceof PromotionProduct;
    }

    public int calculateTotalAmount() {
        return purchaseQuantity * product.getPrice(); //fixme 상품 내부로 넘겨?
    }


}

