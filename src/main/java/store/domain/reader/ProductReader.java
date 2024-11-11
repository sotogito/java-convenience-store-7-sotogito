package store.domain.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import store.constants.OrderInputForm;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;
import store.domain.policies.Promotion;
import store.domain.policies.Promotions;
import store.domain.reader.constants.ProductReaderValue;

public class ProductReader {
    private final static String NON_PROMOTION = "null";

    public List<Product> read(String path, Promotions promotions) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        return addProducts(promotions, br);
    }

    private List<Product> addProducts(Promotions promotions, BufferedReader br) throws IOException {
        try (br) {
            return br.lines()
                    .skip(ProductReaderValue.SKIP_LINE.get())
                    .map(line -> createProduct(promotions, line.split(OrderInputForm.ORDER_DELIMITER.get())))
                    .collect(Collectors.toList());
        }
    }

    private Product createProduct(Promotions promotions, String[] splitLine) {
        String name = splitLine[ProductReaderValue.NAME.get()];
        int price = Integer.parseInt(splitLine[ProductReaderValue.PRICE.get()]);
        int quantity = Integer.parseInt(splitLine[ProductReaderValue.QUANTITY.get()]);
        String promotionName = splitLine[ProductReaderValue.PROMOTION.get()];

        return decideProductKind(promotions, promotionName, name, price, quantity);
    }

    private Product decideProductKind(Promotions promotions, String promotionName,
                                      String name, int price, int quantity) {
        if (!promotionName.equals(NON_PROMOTION)) {
            Promotion promotion = promotions.getPromotionByName(promotionName);
            return new PromotionProduct(name, price, quantity, promotion);
        }
        return new Product(name, price, quantity);
    }

}
