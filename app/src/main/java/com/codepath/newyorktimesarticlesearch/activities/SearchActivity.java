package com.codepath.newyorktimesarticlesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.newyorktimesarticlesearch.R;
import com.codepath.newyorktimesarticlesearch.adapters.ArticleArrayAdapter;
import com.codepath.newyorktimesarticlesearch.helper.Util;
import com.codepath.newyorktimesarticlesearch.listeners.EndlessScrollListener;
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

    GridView gvResults;
    MenuItem searchItem;

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

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.isOnline() == false) {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    private void setupModels() {
        articles = new ArrayList<>();
        filter = new SearchFilter();
    }

    private void setupViews() {
        Toolbar toolbar = findViewById(R.id.toolbar_search_activity);
        setSupportActionBar(toolbar);

        gvResults = findViewById(R.id.gvResults);
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        gvResults.setOnItemClickListener((parent, view, position, id) -> launchArticleActivity(position));

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                fetchArticles(page);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    // Network

    private void fetchArticles(int page) {
        if (page == 1) {
            adapter.clear();
        }
        SearchView searchView = (SearchView) searchItem.getActionView();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("api-key", API_KEY);
        params.put("page", page);
        params.put("q", searchView.getQuery().toString());
        params.put("begin_date", filter.getBeginDateInt());
        params.put("sort", filter.getSortOrderStr());
        params.put("news_desk", filter.getNewsDeskValuesStr());

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

                    Util.showNetworkFailure(SearchActivity.this);
                }
            }
        });
    }

    // Toolbar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchArticles(1);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            searchView.setQuery("", false);
            adapter.clear();
        });


        return super.onCreateOptionsMenu(menu);
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
        i.putExtra(Article.id, article);
        startActivity(i);
    }

    private void launchFilterActivity() {
        Intent i = new Intent(this, FilterActivity.class);
        i.putExtra(SearchFilter.id, filter);
        startActivityForResult(i, FILTER_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == FILTER_ACTIVITY_REQUEST_CODE) {
            SearchFilter newFilter = (SearchFilter) data.getExtras().get(SearchFilter.id);
            filter = newFilter;

            SearchView searchView = (SearchView) searchItem.getActionView();
            String query = searchView.getQuery().toString();
            if (query.isEmpty() == false) {
                articles.clear();
                adapter.notifyDataSetChanged();
                fetchArticles(1);
            }
        }
    }
}
