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

    public int calculateInsufficientQuantity(int totalPurchaseQuantity) {
        return (buy + get) - totalPurchaseQuantity;
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }

    /**
     * LocalDateTime now = LocalDateTime.now(); System.out.println("getYear() = " + now.getYear());
     * System.out.println("getMonth() = " + now.getMonth()); System.out.println("getDayOfMonth() = " +
     * now.getDayOfMonth()); System.out.println("getDayOfWeek() = " + now.getDayOfWeek());
     * System.out.println("getDayOfYear() = " + now.getDayOfYear());
     */

    @Override
    public String toString() {
        return name;
    }
}
