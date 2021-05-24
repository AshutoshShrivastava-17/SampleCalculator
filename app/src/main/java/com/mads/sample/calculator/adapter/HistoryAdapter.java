package com.mads.sample.calculator.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mads.sample.calculator.R;
import com.mads.sample.calculator.model.HistoryItem;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.SingleViewHolder> {

    private Context context;
    private List<HistoryItem> historyItems;

    public static class SingleViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewExpression, textViewResult;

        public SingleViewHolder(View view) {
            super(view);
            textViewExpression = itemView.findViewById(R.id.expression);
            textViewResult = itemView.findViewById(R.id.result);
        }
    }

    public HistoryAdapter(Activity context, List<HistoryItem> historyItems) {
        this.context = context;
        this.historyItems = historyItems;
        Log.v("####","Setting adapter History items:"+historyItems);
    }

    @NonNull
    @Override
    public HistoryAdapter.SingleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_history, viewGroup,
                false);
        return new SingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.SingleViewHolder singleViewHolder, int position) {
        HistoryItem historyItem = historyItems.get(position);
        Log.v("####","Setting values"+historyItem.toString());
        singleViewHolder.textViewExpression.setText(historyItem.getExpression());
        singleViewHolder.textViewResult.setText(historyItem.getResult());
    }

    @Override
    public int getItemCount() {
        Log.v("###","item count:"+historyItems.size());
        return historyItems.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }
}
