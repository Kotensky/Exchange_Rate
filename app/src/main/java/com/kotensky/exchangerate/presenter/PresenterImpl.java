package com.kotensky.exchangerate.presenter;


import android.content.Intent;
import android.util.Log;

import com.kotensky.exchangerate.model.IModel;
import com.kotensky.exchangerate.model.ModelImpl;
import com.kotensky.exchangerate.model.data.Currency;
import com.kotensky.exchangerate.view.IView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import static android.content.ContentValues.TAG;

public class PresenterImpl implements IPresenter {

    private static final String TAG = "PresenterImpl";

    private IModel model = new ModelImpl();

    private IView view;

    private Subscription subscription = Subscriptions.empty();

    private List<Date> dates;

    private List<Currency> currencyList;

    private SimpleDateFormat dateFormat;

    public PresenterImpl(IView view) {
        this.view = view;
    }

    @Override
    public void loadData() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        currencyList = new ArrayList<>();

        dates = model.getDaysBetweenDates(view.getFirstDate(), view.getLastDate());
        dateFormat = new SimpleDateFormat("yyyyMMdd");
        getItem(0, dates.size() - 1);
    }

    @Override
    public void loadTodayList() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        subscription = model.getCurrencyListToday()
                .subscribe(new Observer<List<Currency>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Currency> currencies) {
                        view.showCurrencyList(currencies);
                    }
                });
    }

    @Override
    public void onStop() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private void getItem(final int current, final int last) {
        subscription = model.getCurrencyOfDate(dateFormat.format(dates.get(current)), view.getCurrencyCode())
                .subscribe(new Observer<List<Currency>>() {
                    @Override
                    public void onCompleted() {
                        if (current < last) {
                            getItem(current + 1, last);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Currency> currencies) {
                        currencyList.add(currencies.get(0));
                        view.showData(currencyList);

                    }
                });

    }
}
