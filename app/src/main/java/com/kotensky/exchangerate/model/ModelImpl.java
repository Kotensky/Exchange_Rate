package com.kotensky.exchangerate.model;

import com.kotensky.exchangerate.model.api.ApiModule;
import com.kotensky.exchangerate.model.api.IREST;
import com.kotensky.exchangerate.model.data.Currency;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.R.attr.name;

/**
 * Created by Stas on 12.02.2017.
 */

public class ModelImpl implements IModel {

    private IREST iRest = ApiModule.getApiInterface();

    @Override
    public Observable<List<Currency>> getCurrencyOfDate(String date, String currencyCode) {
        return iRest.getCurrencyList(date, currencyCode, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Currency>> getCurrencyListToday() {
        return iRest.getCurrencyListToday(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    @Override
    public List<Date> getDaysBetweenDates(Date startdate, Date enddate) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }
}
