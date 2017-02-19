package com.kotensky.exchangerate.view;

import com.kotensky.exchangerate.model.data.Currency;

import java.util.Date;
import java.util.List;

/**
 * Created by Stas on 12.02.2017.
 */

public interface IView {

    void showError (String s);

    void showData (List<Currency> currencyList);

    Date getFirstDate();

    Date getLastDate();

    String getCurrencyCode();

}
