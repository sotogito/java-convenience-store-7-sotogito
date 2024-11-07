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


    public int getQuantityNoPromotionApplied() {
        if (product instanceof PromotionProduct promotionProduct) {
            int correctQuantity = promotionProduct.getCorrectQuantity(purchaseQuantity);
            return purchaseQuantity - correctQuantity;
        }
        return 0;
    }

    public boolean isOverPromotionMinBuyQuantity(int noAppliedQuantity) {
        if (product instanceof PromotionProduct promotionProduct) {
            return promotionProduct.isOverPromotionMinBuyQuantity(noAppliedQuantity);
        }
        return false;
    }

    public int getNeedAddQuantity() {
        if (product instanceof PromotionProduct promotionProduct) {
            return promotionProduct.getGetQuantity();
        }
        return purchaseQuantity;
    }


    public int getShortageQuantity() {
        if (product instanceof PromotionProduct promotionProduct) {
            //int correctQuantity = ((PromotionProduct) product).getCorrectQuantity(purchaseQuantity);
            return promotionProduct.calculateQuantityDeduction(purchaseQuantity);
            //note 그냥 재고가 전체 구매 수보다 부족한지만 판단
        }
        return 0;
    }

    public int getNoPromotionQuantity() {
        if (product instanceof PromotionProduct promotionProduct) {
            int correctQuantity = promotionProduct.getCorrectQuantity(purchaseQuantity);
            int alreadyNotPromotion = purchaseQuantity - correctQuantity;
            int shortageQuantity = promotionProduct.calculateShortageQuantity(purchaseQuantity);

            return (alreadyNotPromotion + shortageQuantity);
        }
        return 0;
    }


    public void deleteQuantity(int quantity) {
        if (product instanceof PromotionProduct) {
            purchaseQuantity -= quantity;
        }
    }

    public void updatePromotionProductQuantity(int quantity) {
        if (product instanceof PromotionProduct) {
            this.purchaseQuantity += quantity;
        }
    }


    public boolean isValidDate(LocalDateTime nowTime) {
        if (product instanceof PromotionProduct) {
            return ((PromotionProduct) product).isValidDate(nowTime);
        }
        return false;
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

    public String getProductName() {
        return product.getName();
    }


}

