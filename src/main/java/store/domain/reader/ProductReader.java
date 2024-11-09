package store.domain.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import store.domain.items.Promotion;
import store.domain.items.Promotions;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;
import store.domain.reader.constants.ProductReaderValue;

public class ProductReader {
    private final static String VALUE_DELIMITER = ",";
    private final static String NON_PROMOTION = "null";

    public List<Product> read(String path, Promotions promotions) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        return addProducts(promotions, br);
    }

    private List<Product> addProducts(Promotions promotions, BufferedReader br) throws IOException {
        try (br) {
            return br.lines()
                    .skip(ProductReaderValue.SKIP_LINE.getValue())
                    .map(line -> createProduct(promotions, line.split(VALUE_DELIMITER)))
                    .collect(Collectors.toList());
        }
    }

    private Product createProduct(Promotions promotions, String[] splitLine) {
        String name = splitLine[ProductReaderValue.NAME.getValue()];
        int price = Integer.parseInt(splitLine[ProductReaderValue.PRICE.getValue()]);
        int quantity = Integer.parseInt(splitLine[ProductReaderValue.QUANTITY.getValue()]);
        String promotionName = splitLine[ProductReaderValue.PROMOTION.getValue()];

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
