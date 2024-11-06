package store.enums;

public enum ResourcePath {
    PROMOTION("src/main/resources/promotions", ".md"),
    PRODUCT("src/main/resources/products", ".md");

    private final String filePath;
    private final String extenstion;

    ResourcePath(String filePath, String extenstion) {
        this.extenstion = extenstion;
        this.filePath = filePath;
    }

    public String getPath() {
        return filePath + extenstion;
    }

}

