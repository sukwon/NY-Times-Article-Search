package com.codepath.newyorktimesarticlesearch.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.codepath.newyorktimesarticlesearch.R;
import com.codepath.newyorktimesarticlesearch.helper.Util;
import com.codepath.newyorktimesarticlesearch.models.Article;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = findViewById(R.id.toolbar_article_activity);
        setSupportActionBar(toolbar);

        Article article = (Article) getIntent().getSerializableExtra(Article.id);
        WebView wv = findViewById(R.id.wvArticle);
        wv.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(String.valueOf(request.getUrl()));
                } else {
                    //todo
                }
                return true;
            }
        });
        wv.loadUrl(article.getWebUrl());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.isOnline() == false) {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    // Toolbar

    // Toolbar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_article, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);
        ShareActionProvider miShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        WebView wvArticle = findViewById(R.id.wvArticle);
        shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());

        miShare.setShareIntent(shareIntent);

        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_filter:
//                launchFilterActivity();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
