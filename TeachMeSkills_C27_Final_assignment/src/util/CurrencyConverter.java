package util;

import consts.ExchangeRates;
import logger.Logger;

import java.math.BigDecimal;

public class CurrencyConverter {
    public static BigDecimal convertCurrency(BigDecimal sum, String currency) {
        BigDecimal result = sum;
        if (!currency.equals("USD")) {
            switch (currency) {
                case "EUR":
                    result = sum.multiply(ExchangeRates.EUR);
                    break;
            }
            Logger.writeExecuteLog("value '" + sum + " " + currency + "' has been converted to '" + result + " USD'");
        }
        return result;
    }
}
