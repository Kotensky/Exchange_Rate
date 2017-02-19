package com.kotensky.exchangerate.presenter;

/**
 * Created by Stas on 12.02.2017.
 */

public interface IPresenter {

    void loadData();

    void loadTodayList();

    void onStop();
}
