package com.example.xyzreader.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xyzreader.R;

import java.util.List;

/**
 * Created by Waszak on 05.11.2017.
 */

public class AndroidArticleAdapter extends Adapter<AndroidArticleAdapter.MyViewHolder> {

    private List<String> paragraphList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        private MyViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.article_body);
            mTextView.setTypeface(Typeface.createFromAsset(view.getResources().getAssets(), "Rosario-Regular.ttf"));
        }
    }


    public AndroidArticleAdapter(List<String> paragraphs) {
        this.paragraphList = paragraphs;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_body_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView.setText(Html.fromHtml(paragraphList.get(position).replaceAll("(\r\n|\n)", "<br />")));
    }

    @Override
    public int getItemCount() {
        return paragraphList.size();
    }
}