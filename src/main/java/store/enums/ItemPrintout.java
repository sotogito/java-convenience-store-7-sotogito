package store.enums;

public enum ItemPrintout {
    HEAD("- "),
    GENERAL("%s %,d원 %d개"),
    PROMOTION("%s %,d원 %d개 %s");

    private String printout;

    ItemPrintout(String printout) {
        this.printout = printout;
    }

    public String getPrintout() {
        return HEAD.printout + printout;
    }

}
