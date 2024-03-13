package consts;

public interface OrderContentPattern {
    String NAME_LINE = "^order #p[0-9]{8,9}$";
    String AMOUNT_LINE = "^order( ){0,1}total [0-9,*]+(.[0-9]{2})*$";
}
