package com.codepath.newyorktimesarticlesearch.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchFilter implements Parcelable, Cloneable {

    public static String id = "searchFilter";

    public enum SortOrder {
        NEWEST("Newest"),
        OLDEST("Oldest")
        ;

        private final String text;

        SortOrder(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

        public int getPosition() {
            if (text.equals(NEWEST.toString())) {
                return 0;
            } else if (text.equals(OLDEST.toString())){
                return 1;
            } else {
                return -1;
            }
        }
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

    private static int YEARS_BEFORE_THRESHOLD = -30;
    private static String OLDEST = "oldest";
    private static String NEWEST = "newest";

    private Date beginDate;
    private SortOrder sortOrder;
    private ArrayList<NewsDeskValues> newsDeskValues;

    public SearchFilter() {
        reset();
    }

    public void reset() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, YEARS_BEFORE_THRESHOLD);
        beginDate = cal.getTime();

        sortOrder = SortOrder.NEWEST;
        newsDeskValues = new ArrayList<>();
    }

    // Setters

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setNewsDeskValues(ArrayList<NewsDeskValues> newsDeskValues) {
        this.newsDeskValues = newsDeskValues;
    }

    // Getters

    public Date getBeginDate() {
        return beginDate;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public ArrayList<NewsDeskValues> getNewsDeskValues() {
        return newsDeskValues;
    }

    public int getBeginDateInt() {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(beginDate);
        dateStr = dateStr.replace("-", "");
        int date = Integer.parseInt(dateStr);
        return date;
    }

    public String getSortOrderStr() {
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

    public String getNewsDeskValuesStr() {
        String joined = TextUtils.join(", ", newsDeskValues);
        return "(" + joined + ")";
    }

    // Parcelable

    protected SearchFilter(Parcel in) {
        beginDate = (java.util.Date) in.readSerializable();
        sortOrder = (SortOrder) in.readSerializable();
        newsDeskValues = (ArrayList<NewsDeskValues>) in.readSerializable();
    }

    public static final Creator<SearchFilter> CREATOR = new Creator<SearchFilter>() {
        @Override
        public SearchFilter createFromParcel(Parcel in) {
            return new SearchFilter(in);
        }

        @Override
        public SearchFilter[] newArray(int size) {
            return new SearchFilter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(beginDate);
        dest.writeSerializable(sortOrder);
        dest.writeSerializable(newsDeskValues);
    }

    // Cloneable

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
