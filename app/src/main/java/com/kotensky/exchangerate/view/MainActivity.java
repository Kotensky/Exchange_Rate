package com.kotensky.exchangerate.view;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.kotensky.exchangerate.R;
import com.kotensky.exchangerate.model.data.Currency;
import com.kotensky.exchangerate.presenter.IPresenter;
import com.kotensky.exchangerate.presenter.PresenterImpl;
import com.kotensky.exchangerate.view.adapter.RecyclerViewAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.kotensky.exchangerate.R.string.currency;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, IView {


    @Bind(R.id.buttonStartDate)
    Button buttonStartDate;

    @Bind(R.id.buttonEndDate)
    Button buttonEndDate;

    @Bind(R.id.buttonCurrency)
    Button buttonCurrency;

    @Bind(R.id.buttonAnalysis)
    Button buttonAnalysis;

    @Bind(R.id.chart)
    LineChart lineChart;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private IPresenter presenter;

    private long currentDate;

    private Date startDate, endDate;

    private int mYear, mMonth, mDay;

    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new PresenterImpl(this);
        buttonAnalysis.setOnClickListener(this);
        buttonCurrency.setOnClickListener(this);
        buttonStartDate.setOnClickListener(this);
        buttonEndDate.setOnClickListener(this);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        currentDate = c.getTimeInMillis();
        startDate = c.getTime();
        endDate = c.getTime();
        adapter = new RecyclerViewAdapter();
        LinearLayoutManager llm = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        onClickSetDate(true);
    }

    private void onClickSetDate(final boolean startDateBool) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar c = Calendar.getInstance();
                        if (startDateBool) {
                            buttonStartDate.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                            c.set(year, monthOfYear, dayOfMonth);
                            startDate = c.getTime();
                            if (startDate.after(endDate) || buttonEndDate.getText().toString().matches("")) {
                                onClickSetDate(false);
                            }
                        } else {
                            buttonEndDate.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                            c.set(year, monthOfYear, dayOfMonth);
                            endDate = c.getTime();
                        }
                    }
                }, mYear, mMonth, mDay);
        if (!startDateBool)
            datePickerDialog.getDatePicker().setMinDate(startDate.getTime());
        datePickerDialog.getDatePicker().setMaxDate(currentDate);
        datePickerDialog.show();
    }

    private void selectCurrency(String currencyNames[], final String currencyCodes[]) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Виберіть валюту");
        builder.setItems(currencyNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                buttonCurrency.setText(currencyCodes[which]);
            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStartDate:
                onClickSetDate(true);
                break;
            case R.id.buttonEndDate:
                onClickSetDate(false);
                break;
            case R.id.buttonCurrency:
                presenter.loadTodayList();
                break;
            case R.id.buttonAnalysis:
                if (!buttonStartDate.getText().toString().matches("") &&
                        !buttonEndDate.getText().toString().matches("") &&
                        !buttonCurrency.getText().toString().matches(""))
                    presenter.loadData();
                else
                    Toast.makeText(getApplicationContext(), "Заповніть усі поля", Toast.LENGTH_LONG).show();
                break;
        }
    }


    @Override
    public void showError(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showData(List<Currency> currencyList) {
        adapter.setCurrencyList(currencyList);
        List<Entry> entries = new ArrayList<Entry>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        for (Currency data : currencyList) {
            Date date = null;
            try {
                date = dateFormat.parse(data.getExchangedate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            entries.add(new Entry(date.getTime(), data.getRate()));
        }
        LineDataSet dataSet = new LineDataSet(entries, getCurrencyCode());
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    @Override
    public void showCurrencyList(List<Currency> currencyListToday) {
        String currencyNames[] = new String[currencyListToday.size()];
        String currencyCodes[] = new String[currencyListToday.size()];
        for (int i = 0; i < currencyListToday.size(); i++) {
            currencyCodes[i] = currencyListToday.get(i).getCc();
            currencyNames[i] = currencyListToday.get(i).getTxt() + " [" + currencyCodes[i] + "]";

        }
        selectCurrency(currencyNames, currencyCodes);
    }

    @Override
    public Date getFirstDate() {
        return startDate;
    }

    @Override
    public Date getLastDate() {
        return endDate;
    }

    @Override
    public String getCurrencyCode() {
        return buttonCurrency.getText().toString();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }
}
