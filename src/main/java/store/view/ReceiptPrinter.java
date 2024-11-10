package store.view;

import java.util.Map;
import store.domain.Receipt;
import store.domain.items.item.Product;
import store.constants.printouts.ReceiptPrintout;

public class ReceiptPrinter {

    private final static StringBuilder result = new StringBuilder();

    public static String getPrintout(Receipt receipt) {
        result.delete(0, result.length());
        addHead();
        addPurchaseProductQuantityAndAmount(receipt.getAllOrder());
        getPromotionProductQuantity(receipt.getPromotionProduct());
        getDiscountAndFinalPurchaseAmount(receipt);

        return result.toString();
    }

    private static void addHead() {
        result.append("\n");
        result.append(String.format(
                ReceiptPrintout.CATEGORY_DIVIDER_FORMAT.get(),
                ReceiptPrintout.STORE_NAME.get()));
    }

    private static void addPurchaseProductQuantityAndAmount(Map<Product, Integer> allOrder) {
        result.append(ReceiptPrintout.PURCHASE_PRODUCT_HEAD.get());
        for (Map.Entry<Product, Integer> entry : allOrder.entrySet()) {
            String productName = entry.getKey().getName();
            int purchaseQuantity = entry.getValue();
            int price = entry.getKey().getPrice() * purchaseQuantity;
            result.append(String.format(ReceiptPrintout.PURCHASE_PRODUCT_FORMAT.get(),
                    productName, purchaseQuantity, price));
        }
    }

    private static void getPromotionProductQuantity(Map<Product, Integer> promotionOrders) {
        result.append(String.format(ReceiptPrintout.CATEGORY_DIVIDER_FORMAT.get(),
                ReceiptPrintout.PROMOTION_PRODUCT.get()));
        for (Map.Entry<Product, Integer> entry : promotionOrders.entrySet()) {
            String productName = entry.getKey().getName();
            int promotionQuantity = entry.getValue();
            result.append(String.format(ReceiptPrintout.PROMOTION_PRODUCT_FORMAT.get(),
                    productName, promotionQuantity));
        }
    }

    private static void getDiscountAndFinalPurchaseAmount(Receipt receipt) {
        result.append(ReceiptPrintout.CATEGORY_DIVIDER.get());
        result.append(String.format(ReceiptPrintout.TOTAL_PURCHASE_AMOUNT_FORMAT.get()
                , receipt.getTotalPurchaseCount(), receipt.getTotalAmountBeforeDiscount()));
        result.append(String.format(ReceiptPrintout.PROMOTION_DISCOUNT_AMOUNT_FORMAT.get(),
                receipt.getPromotionDiscount()));
        result.append(String.format(ReceiptPrintout.MEMBERSHIP_DISCOUNT_AMOUNT_FORMAT.get(),
                receipt.getMembershipDiscount()));
        result.append(String.format(ReceiptPrintout.FINAL_AMOUNT_FORMAT.get(), receipt.getFinalAmount()));
    }

}
