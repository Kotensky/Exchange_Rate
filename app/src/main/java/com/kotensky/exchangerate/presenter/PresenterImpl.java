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

public class PresenterImpl implements IPresenter{


    private IModel model = new ModelImpl();

    private IView view;

    private Subscription subscription = Subscriptions.empty();


    public PresenterImpl (IView view){
        this.view = view;
    }

    @Override
    public void loadData() {
        if (!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        final List<Currency> currencyList = new ArrayList<>();

        List<Date> dates = model.getDaysBetweenDates(view.getFirstDate(), view.getLastDate());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        for (int i = 0; i<dates.size(); i++) {
            subscription = model.getCurrencyOfDate(dateFormat.format(dates.get(i)), view.getCurrencyCode())
                    .subscribe(new Observer<List<Currency>>() {
                        @Override
                        public void onCompleted() {
                            view.showData(currencyList);
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showError(e.getMessage());
                        }

                        @Override
                        public void onNext(List<Currency> currencies) {
                            currencyList.add(currencies.get(0));
                        }
                    });
        }
    }

    @Override
    public void onStop() {
        if (!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }
}
