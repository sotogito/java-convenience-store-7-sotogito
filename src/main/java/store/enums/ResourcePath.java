package store.enums;

public enum ResourcePath {
    PROMOTION("src/main/resources/promotions", ".md"),
    PRODUCT("src/main/resources/products", ".md");

    private final String filePath;
    private final String extension;

    ResourcePath(String filePath, String extension) {
        this.extension = extension;
        this.filePath = filePath;
    }

    public String get() {
        return filePath + extension;
    }

}

