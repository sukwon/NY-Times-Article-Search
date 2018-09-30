package com.codepath.newyorktimesarticlesearch.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.codepath.newyorktimesarticlesearch.R;
import com.codepath.newyorktimesarticlesearch.fragments.DatePickerFragment;
import com.codepath.newyorktimesarticlesearch.models.SearchFilter;

import java.util.ArrayList;
import java.util.Calendar;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private SearchFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        filter = getIntent().getParcelableExtra("filter");

        setupSortOrderDropdownMenu();
    }

    // Dropdown

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
                acTV.showDropDown();
                String selection = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT);
            }
        });

        acTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                acTV.showDropDown();
            }
        });
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
