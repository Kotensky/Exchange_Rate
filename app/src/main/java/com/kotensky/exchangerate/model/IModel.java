package com.kotensky.exchangerate.model;

import com.kotensky.exchangerate.model.data.Currency;

import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by Stas on 12.02.2017.
 */

public interface IModel {

    Observable<List<Currency>> getCurrencyOfDate (String date, String currencyCode);

    Observable<List<Currency>> getCurrencyListToday ();

    List<Date> getDaysBetweenDates(Date startdate, Date enddate);
}
