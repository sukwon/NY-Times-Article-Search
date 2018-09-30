package com.codepath.newyorktimesarticlesearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.codepath.newyorktimesarticlesearch.R;
import com.codepath.newyorktimesarticlesearch.fragments.DatePickerFragment;
import com.codepath.newyorktimesarticlesearch.models.SearchFilter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private SearchFilter filter;
    private SearchFilter newFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        filter = getIntent().getParcelableExtra(SearchFilter.id);
        try {
            newFilter = (SearchFilter) filter.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        setupSortOrderDropdownMenu();

        updateViewsWithFilter();
    }

    // UI

    private void updateViewsWithFilter() {
        setBeginDate(filter.getBeginDate());
        setSortOrder(filter.getSortOrder());
        setNewsDeskValues(filter.getNewsDeskValues());
    }

    // News Desk Values

    private void setNewsDeskValues(ArrayList<SearchFilter.NewsDeskValues> newsDeskValues) {
        CheckBox cbArts = findViewById(R.id.cbArts);
        CheckBox cbFashionStyles = findViewById(R.id.cbFashionStyles);
        CheckBox cbSports = findViewById(R.id.cbSports);

        cbArts.setChecked(false);
        cbFashionStyles.setChecked(false);
        cbSports.setChecked(false);

        for (SearchFilter.NewsDeskValues v: newsDeskValues) {
            if (v.equals(SearchFilter.NewsDeskValues.ARTS)) {
                cbArts.setChecked(true);
            } else if (v.equals(SearchFilter.NewsDeskValues.FASHION_AND_STYLES)) {
                cbFashionStyles.setChecked(true);
            } else if (v.equals(SearchFilter.NewsDeskValues.SPORTS)) {
                cbSports.setChecked(true);
            }
        }
    }

    // Sort Order

    private void setSortOrder(SearchFilter.SortOrder sortOrder) {
        AutoCompleteTextView tvSortOrder = findViewById(R.id.acTVSortOrder);
        ArrayAdapter adapter = (ArrayAdapter) tvSortOrder.getAdapter();
        tvSortOrder.setAdapter(null);
        tvSortOrder.setText(sortOrder.toString());
        tvSortOrder.setAdapter(adapter);
    }

    private void setupSortOrderDropdownMenu() {
        final AutoCompleteTextView acTV = findViewById(R.id.acTVSortOrder);
        ArrayList<String> sortOrders = new ArrayList<>();
        sortOrders.add(String.valueOf(SearchFilter.SortOrder.NEWEST));
        sortOrders.add(String.valueOf(SearchFilter.SortOrder.OLDEST));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sortOrders);
        acTV.setAdapter(arrayAdapter);
        acTV.setCursorVisible(false);
        acTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == SearchFilter.SortOrder.OLDEST.getPosition()) {
                    newFilter.setSortOrder(SearchFilter.SortOrder.OLDEST);
                } else if (position == SearchFilter.SortOrder.NEWEST.getPosition()) {
                    newFilter.setSortOrder(SearchFilter.SortOrder.NEWEST);
                } else {
                    Log.d("DEBUG", "This can't be happening");
                }
            }
        });

        acTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                acTV.showDropDown();
            }
        });
    }

    // Begin Date

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

        setBeginDate(c.getTime());
    }

    private void setBeginDate(Date date) {
        String dateStr = new SimpleDateFormat("MM/dd/yyyy").format(date);
        EditText etBeginDate = findViewById(R.id.etBeginDate);
        etBeginDate.setText(dateStr);

        newFilter.setBeginDate(date);
    }

    // Action Handler

    public void onClickClearBtn(View view) {
        newFilter.reset();
        setBeginDate(newFilter.getBeginDate());
        setSortOrder(newFilter.getSortOrder());
        setNewsDeskValues(newFilter.getNewsDeskValues());
    }

    public void onClickApplyBtn(View view) {
        Intent data = new Intent();
        data.putExtra(SearchFilter.id, newFilter);
        setResult(RESULT_OK, data);
        finish();
    }

    public void onClickArts(View view) {
        CheckBox cb = (CheckBox) view;
        handleOnClickNewsDeskValues(cb.isChecked(), SearchFilter.NewsDeskValues.ARTS);
    }

    public void onClickFashionStyles(View view) {
        CheckBox cb = (CheckBox) view;
        handleOnClickNewsDeskValues(cb.isChecked(), SearchFilter.NewsDeskValues.FASHION_AND_STYLES);
    }

    public void onClickSports(View view) {
        CheckBox cb = (CheckBox) view;
        handleOnClickNewsDeskValues(cb.isChecked(), SearchFilter.NewsDeskValues.SPORTS);
    }

    private void handleOnClickNewsDeskValues(boolean isChecked, SearchFilter.NewsDeskValues newsDeskValues) {
        ArrayList<SearchFilter.NewsDeskValues> result = filter.getNewsDeskValues();
        result.remove(newsDeskValues);
        if (isChecked) {
            result.add(newsDeskValues);
        } else {
            result.remove(newsDeskValues);
        }
        newFilter.setNewsDeskValues(result);
    }
}
