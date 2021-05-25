package com.mads.sample.calculator.view;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mads.sample.calculator.R;
import com.mads.sample.calculator.adapter.HistoryAdapter;
import com.mads.sample.calculator.databinding.ActivityMainBinding;
import com.mads.sample.calculator.model.HistoryItem;
import com.mads.sample.calculator.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mMainViewModel;
    private BottomSheetDialog bottomSheetDialog;
    private RecyclerView historyRecyclerView;
    private TextView noHistoryTextView;
    private TextView historyLabel;
    private HistoryAdapter historyAdapter;
    private List<HistoryItem> mHistoryItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_main);
        activityMainBinding.setLifecycleOwner(this);
        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        activityMainBinding.setViewModel(mMainViewModel);
        activityMainBinding.executePendingBindings();
        Toolbar toolbar = findViewById(R.id.toolbar);
        activityMainBinding.historyBtn.setOnClickListener(v -> {
            bottomSheetDialog.show();
        });
        initHistoryBottomSheet();
        setSupportActionBar(toolbar);
        mMainViewModel.getHistoryItems().observe(this, userListUpdateObserver);
    }

    Observer<List<HistoryItem>> userListUpdateObserver = new Observer<List<HistoryItem>>() {
        @Override
        public void onChanged(List<HistoryItem> historyItems) {
            mHistoryItems.clear();
            mHistoryItems.addAll(historyItems);
            historyAdapter.notifyDataSetChanged();
        }
    };

    private void initHistoryBottomSheet() {
        historyAdapter = new HistoryAdapter(this, mHistoryItems);
        bottomSheetDialog = new BottomSheetDialog(this, R.style.DialogStyle);
        bottomSheetDialog.setContentView(R.layout.history_bottom_sheet);
        historyRecyclerView = bottomSheetDialog.findViewById(R.id.recycler_bottom_history);
        noHistoryTextView = bottomSheetDialog.findViewById(R.id.no_history_tv);
        historyLabel = bottomSheetDialog.findViewById(R.id.label);
        bottomSheetDialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog dialog = (BottomSheetDialog) dialogInterface;
            FrameLayout fl_bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            assert fl_bottomSheet != null;
            BottomSheetBehavior.from(fl_bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            if (mHistoryItems.size() > 0) {
                historyRecyclerView.setVisibility(View.VISIBLE);
                historyLabel.setVisibility(View.VISIBLE);
                noHistoryTextView.setVisibility(View.GONE);
            } else {
                historyRecyclerView.setVisibility(View.GONE);
                historyLabel.setVisibility(View.GONE);
                noHistoryTextView.setVisibility(View.VISIBLE);
            }
        });
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        historyRecyclerView.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();
    }
}