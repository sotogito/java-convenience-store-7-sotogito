package store.domain.order;

import java.util.HashMap;
import java.util.Map;
import store.domain.items.item.Product;
import store.enums.AnswerWhether;

/**
 * 구매한 상품과 할인 금액으로 최종 금액을 정한다. discount는 인터페이스로 선언한다.
 */
public class Receipt {
    private final Cart cart;
    private final Map<Product, Integer> promotionProduct; //note 프로모션에서 증정품-수량만 가져와서 저장

    private int totalPurchaseCount;
    private int totalAmountBeforeDiscount; //note 증정품도 다 더함
    private int promotionDiscount; //note 증정품 가격
    private int membershipDiscount; //note 일반 상품 총가격에 30% - 최대 8000원
    private int finalAmount; //note 최종 금액

    /**
     * 1. 프로모션 할인 여부 확인 2. 일반 상품의 할인 적용 - membershipDiscount
     * <p>
     * 2. 증정품 상품과 수량 가져오기, 증정품 가격 저장 - promotionProduct,promotionDiscount 3. 전체 가격 구하기 - totalAmountBeforeDiscount 4. 최종
     * 금액 구하기 - finalAmount
     */
    public Receipt(Cart cart) {
        this.cart = cart;
        this.promotionProduct = new HashMap<>();
    }

    public void process(AnswerWhether answer) {
        setFinalAmount();
        updatePromotionProduct();
        calculatePromotionDiscountAmount();
        calculateMembershipDiscountAmount(answer);
        calculateTotalPurchaseCount();
        calculateFinalAmount();
    }

    private void calculateTotalPurchaseCount() {
        int promotionProductCount = 0;
        for (Map.Entry<Product, Integer> entry : promotionProduct.entrySet()) {
            promotionProductCount += entry.getValue();
        }
        totalPurchaseCount = (cart.getTotalPurchaseCount() - promotionProductCount);
    }


    private void calculateFinalAmount() {
        finalAmount = totalAmountBeforeDiscount - (membershipDiscount - promotionDiscount);
    }

    private void calculateMembershipDiscountAmount(AnswerWhether answer) {
        if (AnswerWhether.findMeaningByAnswer(answer)) {
            int generalPurchaseAmount = cart.getGeneralProductPurchaseTotalAmount();
            membershipDiscount = (int) (generalPurchaseAmount * 0.3);
            if (membershipDiscount > 8000) {
                membershipDiscount = 8000;
            }
            return;
        }
        membershipDiscount = 0;
    }

    private void calculatePromotionDiscountAmount() {
        for (Map.Entry<Product, Integer> entry : promotionProduct.entrySet()) {
            int productPrice = entry.getKey().getPrice();
            int count = entry.getValue();
            promotionDiscount += (productPrice * count);
        }
    }

    private void updatePromotionProduct() {
        promotionProduct.putAll(cart.getPromotionProducts());
    }

    private void setFinalAmount() {
        totalAmountBeforeDiscount = cart.getTotalPrice();
    }

    public Map<Product, Integer> getAllOrder() {
        return cart.getProductQuantityMap();
    }

    public void clearReceipt() {
        promotionProduct.clear();
        totalAmountBeforeDiscount = 0;
        promotionDiscount = 0;
        membershipDiscount = 0;
        finalAmount = 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("===========W 편의점=============");
        builder.append("상품명\t\t수량\t금액");
        for (Map.Entry<Product, Integer> entry : getAllOrder().entrySet()) {
            String productName = entry.getKey().getName();
            int purchaseQuantity = entry.getValue();
            int price = entry.getKey().getPrice();
            builder.append(String.format("%s\t\t%,d\t%,d", productName, purchaseQuantity, price));
        }
        builder.append("===========증\t정=============");
        for (Map.Entry<Product, Integer> entry : promotionProduct.entrySet()) {
            String productName = entry.getKey().getName();
            int promotionQuantity = entry.getValue();
            builder.append(String.format("%s\t\t%,d", productName, purchaseQuantity, promotionQuantity));
        }
        builder.append("==============================");
        builder.append("총구매액\t\t%,d\t%,d");

    }

}
