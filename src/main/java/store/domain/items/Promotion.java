package store.domain.items;

import camp.nextstep.edu.missionutils.DateTimes;
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

    public boolean isValidDate() {
        if (endDate.isAfter(DateTimes.now()) && startDate.isBefore(DateTimes.now())) {
            return true;
        } else if (startDate.equals(DateTimes.now()) || endDate.equals(DateTimes.now())) {
            return true;
        }
        return false;
    }

    public int calculateTotalPromotionProductQuantity(int totalPurchaseQuantity) {
        return calculatePromotionProductQuantity(totalPurchaseQuantity) * getPromotionOneBundle();
    }

    public int calculatePromotionProductQuantity(int totalPurchaseQuantity) {
        return totalPurchaseQuantity / getPromotionOneBundle();
    }


    public boolean isSameName(String name) {
        return this.name.equals(name);
    }

    public boolean isOverMin(int noAppliedQuantity) {
        return noAppliedQuantity >= buy;
    }

    private int getPromotionOneBundle() {
        return buy + get;
    }


    public int getGetQuantity() {
        return get;
    }


    @Override
    public String toString() {
        return name;
    }

}
