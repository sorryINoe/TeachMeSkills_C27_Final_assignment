package consts;

public enum FileNamePattern {
    INVOICE_NAME_PATTERN ("^invoice_[0-9]*_2023\\.txt$"),
    CHECK_NAME_PATTERN ("^2023_electric_bill_[0-9]*\\.txt$"),
    ORDER_NAME_PATTERN ("^2023_order_[0-9]*\\.txt$");

    private String pattern;
    FileNamePattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
