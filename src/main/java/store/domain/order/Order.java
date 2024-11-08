package store.domain.order;

import java.util.Map;
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
            return promotionProduct.calculateQuantityDeduction(purchaseQuantity);
            //note 그냥 재고가 전체 구매 수보다 부족한지만 판단
        }
        return 0;
    }

    public int getNoPromotionQuantity() {
        if (product instanceof PromotionProduct promotionProduct) {
            int canPromotionInStock = promotionProduct.getCorrectQuantity(product.getQuantity());
            return (purchaseQuantity - canPromotionInStock);
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

    public int getPromotionProductQuantity() {
        if (product instanceof PromotionProduct promotionProduct) {
            return promotionProduct.getPromotionProductQuantity(purchaseQuantity);
        }
        return 0;
    }

    //todo 이미 있는 상품은 수량만 업데이트
    public void updateQuantity(Order anotherOrder) {
        if (this.isSameProduct(anotherOrder.getProduct())) {
            this.purchaseQuantity += anotherOrder.purchaseQuantity;
        }
    }


    public boolean isPromotionProduct() {
        return product instanceof PromotionProduct;
    }

    public boolean isSameProduct(Product product) {
        return this.product.equals(product);
    }

    public int calculateTotalAmount() {
        return purchaseQuantity * product.getPrice(); //fixme 상품 내부로 넘겨?
    }


    public String getProductName() {
        return product.getName();
    }

    public Product getProduct() {
        return product;
    }

    public int getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public Map<Product, Integer> updateProductQuantityMap(Map<Product, Integer> productQuantityMap) {
        productQuantityMap.merge(product, purchaseQuantity, Integer::sum);
        return productQuantityMap;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Order [product=");
        builder.append(product.getName());
        builder.append(", purchaseQuantity=");
        builder.append(purchaseQuantity);
        builder.append("]");
        builder.append("\n");
        return builder.toString();
    }


}

