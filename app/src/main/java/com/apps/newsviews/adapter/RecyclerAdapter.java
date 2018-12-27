package com.apps.newsviews.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.newsviews.R;
import com.apps.newsviews.activity.WebViewActivity;
import com.apps.newsviews.model.ArticleModel;
import com.apps.newsviews.utility.ConstantKey;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<ArticleModel> arrayList;
    private Context context;
    private int lastPosition = -1;

    public RecyclerAdapter(Context context, ArrayList<ArticleModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ArticleModel model = arrayList.get(position);

        holder.layoutAuthor.setText(model.getAuthor());
        holder.layoutTitle.setText(model.getTitle());
        holder.layoutDescription.setText(model.getDescription());
        holder.layoutUrl.setText(model.getUrl());
        holder.layoutPublishedAt.setText(model.getPublishedAt());
        holder.layoutContent.setText(model.getContent());

        Picasso.get().load(model.getUrlToImage()).into(holder.layoutUrlToImage);

        holder.layoutNewsModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog(v, model.getTitle(), model.getUrl());
                //Toast.makeText(context, "You clicked "+model.getAuthor(), Toast.LENGTH_SHORT).show();
            }
        });

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView, position);
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
            layoutDescription = (TextView) itemView.findViewById(R.id.layout_description);
            layoutUrl = (TextView) itemView.findViewById(R.id.layout_url);
            layoutPublishedAt = (TextView) itemView.findViewById(R.id.layout_published_at);
            layoutContent = (TextView) itemView.findViewById(R.id.layout_content);

            layoutNewsModel = (LinearLayout) itemView.findViewById(R.id.layout_news_model);
        }
    }

    //====================================================| Here is the key method to apply the animation
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    //====================================================| Date Format
    public String dateFormatFromTimestamp(String input) {
        DateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        java.sql.Timestamp ts = java.sql.Timestamp.valueOf(input);
        Date date = new Date(ts.getTime());
        return String.valueOf(sdf.format(date));
    }

    //====================================================| Alert Message
    public void alertDialog(final View v, final String msg, final String url) {
        new AlertDialog.Builder(v.getRootView().getContext())
                .setTitle(R.string.alert_open_web)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        new AlertDialog.Builder(v.getRootView().getContext())
                                .setTitle(R.string.alert_open_browser)
                                .setMessage(msg)
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        /*if (!url.startsWith("http://") && !url.startsWith("https://"))
                                            url = "http://" + url;*/
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                        context.startActivity(browserIntent);
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(context, WebViewActivity.class);
                                        intent.putExtra(ConstantKey.WEB_URL, url);
                                        context.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

}
