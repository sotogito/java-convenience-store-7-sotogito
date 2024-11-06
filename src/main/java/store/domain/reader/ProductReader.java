package store.domain.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.domain.items.Product;
import store.domain.items.Promotion;
import store.domain.items.PromotionProduct;
import store.domain.items.Promotions;

public class ProductReader {

    public List<Product> read(String path, Promotions promotions) throws IOException {
        List<Product> result = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));

        String line;
        boolean isFirstFieldNameLine = true;

        while ((line = br.readLine()) != null) {
            if (isFirstFieldNameLine) {
                isFirstFieldNameLine = false;
                continue;
            }
            String[] splitLine = line.split(",");

            String name = splitLine[0];
            int price = Integer.parseInt(splitLine[1]);
            int quantity = Integer.parseInt(splitLine[2]);

            String promotionName = splitLine[3];
            if (!promotionName.equals("null")) {
                Promotion promotion = promotions.getPromotionByName(promotionName);
                result.add(new PromotionProduct(name, price, quantity, promotion));
                continue;
            }

            result.add(new Product(name, price, quantity));
        }
        br.close();
        return result;
    }
}
