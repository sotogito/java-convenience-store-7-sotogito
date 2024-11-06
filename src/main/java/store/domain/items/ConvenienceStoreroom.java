package store.domain.items;

import java.io.IOException;
import store.domain.reader.ProductReader;
import store.domain.reader.PromotionReader;
import store.enums.ResourcePath;

public class ConvenienceStoreroom {
    private final Products products;
    private final Promotions promotions;

    public ConvenienceStoreroom(PromotionReader promotionReader, ProductReader productReader) throws IOException {
        this.promotions = new Promotions(promotionReader.read(ResourcePath.PROMOTION.getPath()));
        this.products = new Products(productReader.read(ResourcePath.PRODUCT.getPath(), promotions));
    }

    @Override
    public String toString() {
        return products.toString();
    }

}
