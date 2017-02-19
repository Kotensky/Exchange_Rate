package com.kotensky.exchangerate.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kotensky.exchangerate.R;
import com.kotensky.exchangerate.model.data.Currency;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stas on 19.02.2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Currency> currencyList = new ArrayList<>();

    public void setCurrencyList (List<Currency> currencyList){
        this.currencyList = currencyList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Currency currency = currencyList.get(position);
        holder.name.setText(currency.getTxt());
        holder.code.setText(currency.getCc());
        holder.rate.setText(currency.getRate().toString());
        holder.date.setText(currency.getExchangedate());
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView code;
        private TextView rate;
        private TextView date;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            code = (TextView) itemView.findViewById(R.id.code);
            rate = (TextView) itemView.findViewById(R.id.rate);
            date = (TextView) itemView.findViewById(R.id.date);

        }
    }
}