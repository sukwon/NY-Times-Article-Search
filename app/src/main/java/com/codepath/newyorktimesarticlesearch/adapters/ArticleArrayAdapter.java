package com.codepath.newyorktimesarticlesearch.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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

    private static class TextViewHolder {
        TextView tvTitle;
    }

    private static class ImageAndTextViewHolder {
        ImageView ivImage;
        TextView tvTitle;
    }

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Article article= getItem(position);
        int type = getItemViewType(position);

        if (type == Article.DisplayStyle.TEXT.ordinal()) {
            TextViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_article_result_text, parent, false);

                viewHolder = new TextViewHolder();
                viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (TextViewHolder) convertView.getTag();
            }

            viewHolder.tvTitle.setText(article.getHeadline());
        } else if (type == Article.DisplayStyle.IMAGE_AND_TEXT.ordinal()) {
            ImageAndTextViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_article_result, parent, false);

                viewHolder = new ImageAndTextViewHolder();
                viewHolder.ivImage = convertView.findViewById(R.id.ivImage);
                viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ImageAndTextViewHolder) convertView.getTag();
            }

            viewHolder.ivImage.setImageResource(0);
            viewHolder.tvTitle.setText(article.getHeadline());

            String thumbnail = article.getThumbnail();
            Picasso.with(getContext()).load(thumbnail).into(viewHolder.ivImage);
        } else {
            Log.d("DEBUG", "This can't be happened.");
        }
        return convertView;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItem(position).displayStyle.ordinal();
    }

    @Override
    public int getViewTypeCount() {
        return Article.DisplayStyle.values().length;
    }
}
