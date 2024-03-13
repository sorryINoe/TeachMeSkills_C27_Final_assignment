package consts;

public interface InvoiceContentPatterns {
    String NAME_LINE = "^invoice";
    String DATE_LINE = "^invoice date [0-9]{2}\\/[0-9]{2}\\/2023$";
    String AMOUNT_LINE = "^total (amount )*\\$*[0-9]+((\\.|,)[0-9]{2})*\\$*$";
}
