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

    //note 현재 콜라 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
    public int getNoPromotionQuantity() {
        if (product instanceof PromotionProduct) {
            //int correctQuantity = ((PromotionProduct) product).getCorrectQuantity(purchaseQuantity);
            return product.calculateQuantityDeduction(purchaseQuantity);
        }
        return 0;
    }

    public boolean isValidDate(LocalDateTime nowTime) {
        if (product instanceof PromotionProduct) {
            return ((PromotionProduct) product).isValidDate(nowTime);
        }
        return false;
    }

/**
 public int getInsufficientQuantity() { //note 출력과 재 업데이트에 필요
 if (product instanceof PromotionProduct promotionProduct) {
 int correctQuantity = promotionProduct.getCorrectQuantity(purchaseQuantity);
 int remainingQuantity = purchaseQuantity - correctQuantity;


 /**
 * remainingQuantity 수량이 프로모션의 최소 buy에 미치는가?
 * 안미치면 일반상품으로 변경
 * 미치면 여부 물어보기
 *
 *
 */
    //구매 가능 개수 수량만큼 프로모션 재고가 존재하는지

    /**
     * return ((PromotionProduct) product).getInsufficientQuantity(purchaseQuantity); } return 0; }
     **/

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

    //todo 구매하면 그냥 수량만큼 -해주면 되네 자체적으로
    public void updateBuyQuantity() {
        product.decreaseQuantity(purchaseQuantity);
    }


}

