package store.domain;

import java.io.IOException;
import java.util.Map;
import store.domain.finders.OrderProductFinder;
import store.domain.items.Products;
import store.domain.items.Promotions;
import store.domain.items.item.Product;
import store.domain.reader.ProductReader;
import store.domain.reader.PromotionReader;
import store.enums.ResourcePath;


public class ConvenienceStoreroom implements OrderProductFinder {
    private final StockManager stockManager;
    private final Products products;
    private final Promotions promotions;

    public ConvenienceStoreroom(PromotionReader promotionReader, ProductReader productReader) throws IOException {
        this.promotions = new Promotions(promotionReader.read(ResourcePath.PROMOTION.get()));
        this.products = new Products(productReader.read(ResourcePath.PRODUCT.get(), promotions));
        this.stockManager = new StockManager();
    }

    @Override
    public Product findProductByNameAndQuantity(String name, int quantity) {
        return products.getProductByNameAndQuantity(name, quantity);
    }

    @Override
    public Product findGeneralProductByNameAndQuantity(String name, int quantity) {
        return products.getGeneralProductByNameAndQuantity(name, quantity);
    }

    public void decreaseStock(Map<Product, Integer> productQuantity) {
        stockManager.decreaseStock(products, productQuantity);
    }

    public boolean isSufficientStockAfterGetPromotionProduct(String name, int afterGetPromotionQuantity) {
        return products.isSufficientPromotionStockByNameAndQuantity(name, afterGetPromotionQuantity);
    }


    @Override
    public String toString() {
        return products.toString();
    }

}
