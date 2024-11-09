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


    public int getNeedAddQuantity() {
        if (product instanceof PromotionProduct promotionProduct) {
            return promotionProduct.getGetQuantity();
        }
        return purchaseQuantity;
    }

    public int getNonAppliedPromotionQuantity() {
        if (product instanceof PromotionProduct promotionProduct) {
            int correctQuantity = promotionProduct.getTotalPromotionProductQuantity(purchaseQuantity);
            return purchaseQuantity - correctQuantity;
        }
        return 0;
    }

    public int getNonApplicablePromotionProductQuantity() {
        if (product instanceof PromotionProduct promotionProduct) {
            int canPromotionInStock = promotionProduct.getTotalPromotionProductQuantity(product.getQuantity());
            return (purchaseQuantity - canPromotionInStock);
        }
        return 0;
    }


    public boolean isShortageStockPromotionProductThanPurchaseQuantity() {
        if (product instanceof PromotionProduct promotionProduct) {
            return promotionProduct.calculateQuantityDeduction(purchaseQuantity) > 0;
        }
        return false;
    }

    public boolean isOverPromotionMinBuyQuantity(int noAppliedQuantity) {
        if (product instanceof PromotionProduct promotionProduct) {
            return promotionProduct.isOverPromotionMinBuyQuantity(noAppliedQuantity);
        }
        return false;
    }


    private int getPromotionProductQuantity() {
        if (product instanceof PromotionProduct promotionProduct) {
            return promotionProduct.getPromotionProductQuantity(purchaseQuantity);
        }
        return 0;
    }

    public void updatePromotionProductQuantity(int quantity) {
        if (product instanceof PromotionProduct) {
            this.purchaseQuantity += quantity;
        }
    }

    public void deleteQuantity(int quantity) {
        if (product instanceof PromotionProduct) {
            purchaseQuantity -= quantity;
        }
    }


    public void updateQuantity(Order anotherOrder) {
        if (this.isSameProduct(anotherOrder.product)) {
            this.purchaseQuantity += anotherOrder.purchaseQuantity;
        }
    }

    public int getSufficientStockAfterGetPromotionProduct() {
        return purchaseQuantity + getNeedAddQuantity();
    }

    public int calculateTotalAmount() {
        return purchaseQuantity * product.getPrice();
    }


    public boolean isPromotionProduct() {
        return product instanceof PromotionProduct;
    }

    public boolean isSameProduct(Product product) {
        return this.product.equals(product);
    }


    public Order createOrder(int shortageQuantity) {
        return new Order(product, shortageQuantity);
    }

    public String getProductName() {
        return product.getName();
    }

    public int getPurchaseQuantity() {
        return purchaseQuantity;
    }


    public void updateAllProductQuantity(Map<Product, Integer> productQuantityMap) {
        productQuantityMap.merge(product, purchaseQuantity, Integer::sum);
    }

    public void updateOnlyPromotionProductAndQuantity(Map<Product, Integer> productQuantityMap) {
        productQuantityMap.merge(product, getPromotionProductQuantity(), Integer::sum);
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

