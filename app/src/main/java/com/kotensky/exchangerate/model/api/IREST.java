package com.kotensky.exchangerate.model.api;

import com.kotensky.exchangerate.model.data.Currency;

import java.util.List;


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Stas on 12.02.2017.
 */

public interface IREST {

    @GET ("/NBUStatService/v1/statdirectory/exchange")
    Observable<List<Currency>> getCurrencyList (@Query("date") String date, @Query("valcode") String currencyCode, @Query("json") boolean json);

    @GET ("/NBUStatService/v1/statdirectory/exchange")
    Observable<List<Currency>> getCurrencyListToday (@Query("json") boolean json);

}
