package com.codepath.newyorktimesarticlesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.codepath.newyorktimesarticlesearch.R;
import com.codepath.newyorktimesarticlesearch.adapters.ArticleArrayAdapter;
import com.codepath.newyorktimesarticlesearch.models.Article;
import com.codepath.newyorktimesarticlesearch.models.SearchFilter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private static String BASE_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    private static String API_KEY = "6475b9261bb44f24a931b9088cc1c8d7";
    private final int FILTER_ACTIVITY_REQUEST_CODE = 20;

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;

    private ArrayList<Article> articles;
    private ArticleArrayAdapter adapter;
    private SearchFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupModels();
        setupViews();
    }

    private void setupModels() {
        articles = new ArrayList<>();
        filter = new SearchFilter();
    }

    private void setupViews() {
        Toolbar toolbar = findViewById(R.id.toolbar_search_activity);
        setSupportActionBar(toolbar);

        etQuery = findViewById(R.id.etQuery);
        gvResults = findViewById(R.id.gvResults);
        btnSearch = findViewById(R.id.btnSearch);
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchArticleActivity(position);
            }
        });
    }

    // Action Handler

    public void onArticleSearch(View view) {
        dismissKeyboard();

        String query = etQuery.getText().toString();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("api-key", API_KEY);
        params.put("page", 0);
        params.put("q", query);
        params.put("begin_date", filter.getBeginDate());
        params.put("sort", filter.getSortOrder());
        params.put("news_desk", filter.getNewsDeskValues());

        client.get(BASE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Toolbar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                launchFilterActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Navigation

    private void launchArticleActivity(int position) {
        Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
        Article article = articles.get(position);
        i.putExtra("article", article);
        startActivity(i);
    }

    private void launchFilterActivity() {
        Intent i = new Intent(this, FilterActivity.class);
        i.putExtra("filter", filter);
//        startActivityForResult(i, FILTER_ACTIVITY_REQUEST_CODE);
        startActivity(i);
    }

    // Private Methods

    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etQuery.getWindowToken(), 0);
    }
}
