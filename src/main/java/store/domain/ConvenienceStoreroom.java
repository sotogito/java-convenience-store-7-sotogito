package store.domain;

import java.io.IOException;
import java.util.Map;
import store.domain.items.Products;
import store.domain.items.Promotions;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;
import store.domain.reader.ProductReader;
import store.domain.reader.PromotionReader;
import store.enums.ResourcePath;


public class ConvenienceStoreroom implements OrderProductFinder {
    private final Products products;
    private final Promotions promotions;

    public ConvenienceStoreroom(PromotionReader promotionReader, ProductReader productReader) throws IOException {
        this.promotions = new Promotions(promotionReader.read(ResourcePath.PROMOTION.getPath()));
        this.products = new Products(productReader.read(ResourcePath.PRODUCT.getPath(), promotions));
    }

    @Override
    public Product findProductByNameAndQuantity(String name, int quantity) {
        return products.getProductByNameAndQuantity(name, quantity);
    }

    @Override
    public Product findGeneralProductByNameAndQuantity(String name, int quantity) {
        return products.getGeneralProductByNameAndQuantity(name, quantity);
    }

    private Product findGeneralProductByName(String name) {
        return products.getGeneralProductByName(name);
    }

    public void decreaseStock(Map<Product, Integer> productQuantity) {
        for (Map.Entry<Product, Integer> entry : productQuantity.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();

            precessDecrease(product, quantity);
        }
    }

    private void precessDecrease(Product product, Integer quantity) {
        if (product instanceof PromotionProduct promotionProduct) {
            for (int i = 0; i < quantity; i++) {
                changeOutOfStockProductToGeneral(product, promotionProduct);
                promotionProduct.decreaseQuantitySingly();
            }
            return;
        }
        product.decreaseQuantity(quantity);
    }

    private void changeOutOfStockProductToGeneral(Product product, PromotionProduct promotionProduct) {
        if (promotionProduct.isOutOfStock()) {
            Product generalProduct = findGeneralProductByName(product.getName());
            generalProduct.decreaseQuantitySingly();
        }
    }

    public boolean isSufficientStockAfterGetPromotionProduct(String name, int afterGetPromotionQuantity) {
        return products.isSufficientStockByNameAndQuantity(name, afterGetPromotionQuantity);
    }


    @Override
    public String toString() {
        return products.toString();
    }

}
