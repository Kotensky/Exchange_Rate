package com.kotensky.exchangerate.view;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, IView {



    @Bind(R.id.buttonStartDate)
    Button buttonStartDate;

    @Bind(R.id.buttonEndDate)
    Button buttonEndDate;

    @Bind(R.id.buttonAnalysis)
    Button buttonAnalysis;

    @Bind(R.id.chart)
    LineChart lineChart;

    private IPresenter presenter;

    private long currentDate;

    private Date startDate, endDate;

    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new PresenterImpl(this);
        buttonAnalysis.setOnClickListener(this);
        buttonStartDate.setOnClickListener(this);
        buttonEndDate.setOnClickListener(this);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        currentDate = c.getTimeInMillis();
        startDate = c.getTime();
        endDate = c.getTime();


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
                            Log.e("startDate", c.getTime().toString() + "  " + startDate.toString());
                            if (startDate.after(endDate)) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStartDate:
                onClickSetDate(true);
                break;
            case R.id.buttonEndDate:
                onClickSetDate(false);
                break;
            case R.id.buttonAnalysis:
                presenter.loadData();
                break;
        }
    }

    @Override
    public void showError(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showData(List<Currency> currencyList) {

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
        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
//        dataSet.setColor(...);
//        dataSet.setValueTextColor(...);
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
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
        return "USD";
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }
}
