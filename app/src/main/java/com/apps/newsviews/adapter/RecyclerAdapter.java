package com.apps.newsviews.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.newsviews.R;
import com.apps.newsviews.model.ArticleModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<ArticleModel> arrayList;
    private Context context;

    public RecyclerAdapter(Context context, ArrayList<ArticleModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ArticleModel model = arrayList.get(position);

        Log.d("Author", ""+model.getAuthor());

        holder.layoutAuthor.setText(model.getAuthor());
        holder.layoutTitle.setText(model.getTitle());
        holder.layoutDescription.setText(model.getDescription());
        holder.layoutUrl.setText(model.getUrl());
        holder.layoutPublishedAt.setText(model.getPublishedAt());
        holder.layoutContent.setText(model.getContent());

        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
        Picasso.get().load(model.getUrlToImage()).into(holder.layoutUrlToImage);

        holder.layoutNewsModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You clicked "+model.getAuthor(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView layoutUrlToImage;

        public TextView layoutAuthor;
        public TextView layoutTitle;
        public TextView layoutDescription;
        public TextView layoutUrl;
        public TextView layoutPublishedAt;
        public TextView layoutContent;

        public LinearLayout layoutNewsModel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutUrlToImage = (ImageView) itemView.findViewById(R.id.layout_url_to_image);

            layoutAuthor = (TextView) itemView.findViewById(R.id.layout_author);
            layoutTitle = (TextView) itemView.findViewById(R.id.layout_title);
            layoutUrl = (TextView) itemView.findViewById(R.id.layout_description);
            layoutDescription = (TextView) itemView.findViewById(R.id.layout_url);
            layoutPublishedAt = (TextView) itemView.findViewById(R.id.layout_published_at);
            layoutContent = (TextView) itemView.findViewById(R.id.layout_content);

            layoutNewsModel = (LinearLayout) itemView.findViewById(R.id.layout_news_model);
        }
    }
}
