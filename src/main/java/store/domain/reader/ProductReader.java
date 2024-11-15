package store.domain.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.constants.OrderInputForm;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;
import store.domain.policies.Promotion;
import store.domain.policies.Promotions;
import store.domain.reader.constants.ProductReaderValue;

public class ProductReader {
    private final static String NON_PROMOTION = "null";
    private boolean havePromotionProduct = false;

    public List<Product> read(String path, Promotions promotions) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        return addProducts(promotions, br);
    }

    /**
     * 프로모션 상품이 있는데 일반 상품이 없을 경우 일반 상품은 재고 없음으로 표시한다. 1. 프로모션 상품인지 추가한다.havePromotionProduct = true; 2. 프로모션 상품과 이름이 같은
     * 일반 상품을 추가한다, haveGeneralProduct = true; 3. 만약 true, false일 경우 재고가 0일 일반상품을 추가한다.
     */


    private List<Product> addProducts(Promotions promotions, BufferedReader br) throws IOException {
        List<Product> result = new ArrayList<>();

        String line;
        boolean isFirstLine = true;
        while ((line = br.readLine()) != null) {
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }

            String[] splitLine = line.split(OrderInputForm.ORDER_DELIMITER.get());

            String name = splitLine[ProductReaderValue.NAME.get()];
            int price = Integer.parseInt(splitLine[ProductReaderValue.PRICE.get()]);
            int quantity = Integer.parseInt(splitLine[ProductReaderValue.QUANTITY.get()]);
            String promotionName = splitLine[ProductReaderValue.PROMOTION.get()];

            if (havePromotionProduct) {
                Product promotionProduct = result.getLast();
                String promotionProductName = promotionProduct.getName();
                if (!name.equals(promotionProductName)) {
                    int generalProductPrice = promotionProduct.getPrice();
                    Product generalProduct = new Product(promotionProductName, generalProductPrice, 0);
                    result.add(generalProduct);
                }
                havePromotionProduct = false;
            }

            Product product = decideProductKind(promotions, promotionName, name, price, quantity);
            result.add(product);


        }
        return result;
    }

    private void addProduct() {

    }


    private Product decideProductKind(Promotions promotions, String promotionName,
                                      String name, int price, int quantity) {
        if (!promotionName.equals(NON_PROMOTION)) {
            Promotion promotion = promotions.getPromotionByName(promotionName);
            havePromotionProduct = true;
            return new PromotionProduct(name, price, quantity, promotion);
        }
        return new Product(name, price, quantity);
    }

}
