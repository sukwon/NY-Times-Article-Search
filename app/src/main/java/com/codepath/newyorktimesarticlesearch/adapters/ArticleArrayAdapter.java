package com.codepath.newyorktimesarticlesearch.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.newyorktimesarticlesearch.R;
import com.codepath.newyorktimesarticlesearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Article article= getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);

            ImageView iv = convertView.findViewById(R.id.ivImage);
            iv.setImageResource(0);

            TextView tv = convertView.findViewById(R.id.tvTitle);
            tv.setText(article.getHeadline());

            String thumbnail = article.getThumbnail();
            if (!TextUtils.isEmpty(thumbnail)) {
                Picasso.with(getContext()).load(thumbnail).into(iv);
            }

        }
        return convertView;
    }
}
