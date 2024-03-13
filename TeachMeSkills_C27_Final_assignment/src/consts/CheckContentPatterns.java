package consts;

public interface CheckContentPatterns {
    String NAME_LINE = "^check # [0-9]+$";
    String DATE_LINE = "^date: [0-9]{2}.[0-9]{2}.2023$";
    String AMOUNT_LINE = "^bill total amount euro [0-9]+,[0-9]{2}$";
}
