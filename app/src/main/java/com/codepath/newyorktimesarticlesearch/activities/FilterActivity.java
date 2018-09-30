package com.codepath.newyorktimesarticlesearch.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;

import com.codepath.newyorktimesarticlesearch.R;
import com.codepath.newyorktimesarticlesearch.fragments.DatePickerFragment;
import com.codepath.newyorktimesarticlesearch.models.SearchFilter;

import java.util.Calendar;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private SearchFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        filter = getIntent().getParcelableExtra("filter");
    }

    // DataPicker

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    // Action Handler

    public void onClickClearBtn(View view) {
        filter.reset();
    }

    public void onClickApplyBtn(View view) {
//        Intent data = new Intent();
//        setResult(RESULT_OK, data);

        finish();
    }
}
