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
import android.widget.Toast;

import com.apps.newsviews.R;
import com.apps.newsviews.activity.WebViewActivity;
import com.apps.newsviews.model.ArticleModel;
import com.apps.newsviews.utility.ConstantKey;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ArticleModel> arrayList;
    private Context context;
    private int lastPosition = -1;
    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;

    public MyAdapter(Context context, ArrayList<ArticleModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    // get the size of the list
    @Override
    public int getItemCount() {
        return (arrayList != null && arrayList.size() > 0 ) ? arrayList.size() : 0;
    }

    // determine which layout to use for the row
    @Override
    public int getItemViewType(int position) {
        ArticleModel model = arrayList.get(position);
        if (model.getType() == ArticleModel.ItemType.ONE_ITEM) {
            return TYPE_ONE;
        } else if (model.getType() == ArticleModel.ItemType.TWO_ITEM) {
            return TYPE_TWO;
        } else {
            return -1;
        }
    }

    // specify the row layout file and click for each row
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_type1, parent, false);
            return new ViewHolderOne(view);
        } else if (viewType == TYPE_TWO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_type2, parent, false);
            return new ViewHolderTwo(view);
        } else {
            throw new RuntimeException("The type has to be ONE or TWO");
        }
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case TYPE_ONE:
                initLayoutOne((ViewHolderOne)holder, position);
                break;
            case TYPE_TWO:
                initLayoutTwo((ViewHolderTwo) holder, position);
                break;
            default:
                break;
        }
    }

    private void initLayoutOne(ViewHolderOne holder, int pos) {
        final ArticleModel model = arrayList.get(pos);
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
        setAnimation(holder.itemView, pos);
    }

    private void initLayoutTwo(ViewHolderTwo holder, int pos) {
        final ArticleModel model = arrayList.get(pos);
        holder.layoutAuthor2.setText(model.getAuthor());
        holder.layoutTitle2.setText(model.getTitle());
        holder.layoutDescription2.setText(model.getDescription());
        holder.layoutUrl2.setText(model.getUrl());
        holder.layoutPublishedAt2.setText(model.getPublishedAt());
        holder.layoutContent2.setText(model.getContent());
        Picasso.get().load(model.getUrlToImage()).into(holder.layoutUrlToImage2);

        holder.layoutNewsModel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog(v, model.getTitle(), model.getUrl());
                //Toast.makeText(context, "You clicked "+model.getAuthor(), Toast.LENGTH_SHORT).show();
            }
        });

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView, pos);
    }

    // Static inner class to initialize the views of rows
    static class ViewHolderOne extends RecyclerView.ViewHolder {
        public ImageView layoutUrlToImage;
        public TextView layoutAuthor;
        public TextView layoutTitle;
        public TextView layoutDescription;
        public TextView layoutUrl;
        public TextView layoutPublishedAt;
        public TextView layoutContent;
        public LinearLayout layoutNewsModel;

        public ViewHolderOne(View itemView) {
            super(itemView);
            layoutUrlToImage = (ImageView) itemView.findViewById(R.id.layout_url_to_image1);
            layoutAuthor = (TextView) itemView.findViewById(R.id.layout_author1);
            layoutTitle = (TextView) itemView.findViewById(R.id.layout_title1);
            layoutDescription = (TextView) itemView.findViewById(R.id.layout_description1);
            layoutUrl = (TextView) itemView.findViewById(R.id.layout_url1);
            layoutPublishedAt = (TextView) itemView.findViewById(R.id.layout_published_at1);
            layoutContent = (TextView) itemView.findViewById(R.id.layout_content1);
            layoutNewsModel = (LinearLayout) itemView.findViewById(R.id.list_row_id1);
        }
    }

    static class ViewHolderTwo extends RecyclerView.ViewHolder {
        public ImageView layoutUrlToImage2;
        public TextView layoutAuthor2;
        public TextView layoutTitle2;
        public TextView layoutDescription2;
        public TextView layoutUrl2;
        public TextView layoutPublishedAt2;
        public TextView layoutContent2;
        public LinearLayout layoutNewsModel2;

        public ViewHolderTwo(View itemView) {
            super(itemView);
            layoutUrlToImage2 = (ImageView) itemView.findViewById(R.id.layout_url_to_image2);
            layoutAuthor2 = (TextView) itemView.findViewById(R.id.layout_author2);
            layoutTitle2 = (TextView) itemView.findViewById(R.id.layout_title2);
            layoutDescription2 = (TextView) itemView.findViewById(R.id.layout_description2);
            layoutUrl2 = (TextView) itemView.findViewById(R.id.layout_url2);
            layoutPublishedAt2 = (TextView) itemView.findViewById(R.id.layout_published_at2);
            layoutContent2 = (TextView) itemView.findViewById(R.id.layout_content2);
            layoutNewsModel2 = (LinearLayout) itemView.findViewById(R.id.list_row_id2);
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
