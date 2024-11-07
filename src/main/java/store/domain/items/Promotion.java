package store.domain.items;

import java.time.LocalDateTime;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Promotion(String name, int buy, int get,
                     LocalDateTime startDate, LocalDateTime endDate) {
        this.buy = buy;
        this.name = name;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isValidDate(LocalDateTime nowTime) {
        if (endDate.isAfter(nowTime) && startDate.isBefore(nowTime)) {
            return true;
        } else if (startDate.equals(nowTime) || endDate.equals(nowTime)) {
            return true;
        }
        return false;
    }

    public int calculateCorrectQuantity(int totalPurchaseQuantity) {
        int bundle = totalPurchaseQuantity / (buy + get);
        return bundle * (buy + get);
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }

    public boolean isOverMin(int noAppliedQuantity) {
        return noAppliedQuantity >= buy;
    }

    public int getGetQuantity() {
        return get;
    }

    @Override
    public String toString() {
        return name;
    }
}
