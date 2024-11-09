package store.view;

import java.util.Map;
import store.domain.items.item.Product;
import store.domain.order.Receipt;
import store.enums.ReceiptPrintout;

public class ReceiptPrinter {

    private final static StringBuilder result = new StringBuilder();

    public static String getPrintout(Receipt receipt) {
        addHead();
        addPurchaseProductQuantityAndAmount(receipt.getAllOrder());
        getPromotionProductQuantity(receipt.getPromotionProduct());
        getDiscountAndFinalPurchaseAmount(receipt);

        return result.toString();
    }

    private static void addPurchaseProductQuantityAndAmount(Map<Product, Integer> allOrder) {
        result.append(ReceiptPrintout.PURCHASE_PRODUCT_HEAD.getPrintout());
        for (Map.Entry<Product, Integer> entry : allOrder.entrySet()) {
            String productName = entry.getKey().getName();
            int purchaseQuantity = entry.getValue();
            int price = entry.getKey().getPrice() * purchaseQuantity;
            result.append(String.format(ReceiptPrintout.PURCHASE_PRODUCT_FORMAT.getPrintout(),
                    productName, purchaseQuantity, price));
        }
    }


    private static void getPromotionProductQuantity(Map<Product, Integer> promotionOrders) {
        result.append(String.format(ReceiptPrintout.CATEGORY_DIVIDER_FORMAT.getPrintout(),
                ReceiptPrintout.PROMOTION_PRODUCT.getPrintout()));
        for (Map.Entry<Product, Integer> entry : promotionOrders.entrySet()) {
            String productName = entry.getKey().getName();
            int promotionQuantity = entry.getValue();
            result.append(String.format(ReceiptPrintout.PROMOTION_PRODUCT_FORMAT.getPrintout(),
                    productName, promotionQuantity));
        }
    }


    private static void getDiscountAndFinalPurchaseAmount(Receipt receipt) {
        result.append(ReceiptPrintout.CATEGORY_DIVIDER.getPrintout());
        result.append(String.format(ReceiptPrintout.TOTAL_PURCHASE_AMOUNT_FORMAT.getPrintout()
                , receipt.getTotalPurchaseCount(), receipt.getTotalAmountBeforeDiscount()));
        result.append(String.format(ReceiptPrintout.PROMOTION_DISCOUNT_AMOUNT_FORMAT.getPrintout(),
                receipt.getPromotionDiscount()));
        result.append(String.format(ReceiptPrintout.MEMBERSHIP_DISCOUNT_AMOUNT_FORMAT.getPrintout(),
                receipt.getMembershipDiscount()));
        result.append(String.format(ReceiptPrintout.FINAL_AMOUNT_FORMAT.getPrintout(), receipt.getFinalAmount()));
    }

    private static void addHead() {
        result.append(String.format(
                ReceiptPrintout.CATEGORY_DIVIDER_FORMAT.getPrintout(),
                ReceiptPrintout.STORE_NAME.getPrintout()));
    }

}
