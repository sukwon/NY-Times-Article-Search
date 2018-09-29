package com.codepath.newyorktimesarticlesearch.models;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Filter {

    enum SortOrder {
        OLDEST,
        NEWEST
    }

    public enum NewsDeskValues {
        ARTS("Arts"),
        FASHION_AND_STYLES("Fashion and styles"),
        SPORTS("Sports")
        ;

        private final String text;

        NewsDeskValues(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    Date beginDate;
    SortOrder sortOrder;
    ArrayList<NewsDeskValues> newsDeskValues;

    private static int YEARS_BEFORE_THRESHOLD = -30;
    private static String OLDEST = "oldest";
    private static String NEWEST = "newest";

    public Filter() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, YEARS_BEFORE_THRESHOLD);
        beginDate = cal.getTime();

        sortOrder = SortOrder.NEWEST;
        newsDeskValues = new ArrayList<>();
    }

    // Getters

    public int getBeginDate() {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(beginDate);
        dateStr = dateStr.replace("-", "");
        int date = Integer.parseInt(dateStr);
        return date;
    }

    public String getSortOrder() {
        String result = "";
        switch (sortOrder) {
            case NEWEST:
                result = NEWEST;
                break;
            case OLDEST:
                result = OLDEST;
                break;
        }
        return result;
    }

    public String getNewsDeskValues() {
        String joined = TextUtils.join(", ", newsDeskValues);
        return "(" + joined + ")";
    }
}
