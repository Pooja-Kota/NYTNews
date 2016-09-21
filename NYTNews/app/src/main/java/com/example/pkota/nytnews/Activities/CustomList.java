package com.example.pkota.nytnews.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pkota.nytnews.R;
import com.example.pkota.nytnews.utils.News;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by pkota on 07-09-2016.
 */
public class CustomList  extends RecyclerView.Adapter<CustomList.MyViewHolder> {

    private List<News> newsDataSet;
    Context context;
    private static final String TAG = "CustomList";
    //Animation
    Animation animFadeOut;

    public CustomList(List<News> newsDataSet, Context context) {
        this.newsDataSet = newsDataSet;
        this.context = context;
    }


    public final static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView newsTitle;
        TextView newsPublishedDate;
        ImageView newsImageView;
        CardView newsCardView;
        ImageView shareIcon;
        TextView newsAbstractDescription;
        RelativeLayout relativeLayoutCard;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.newsTitle = (TextView) itemView.findViewById(R.id.title);
            this.newsPublishedDate = (TextView) itemView.findViewById(R.id.date);
            this.newsImageView = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.newsCardView = (CardView) itemView.findViewById(R.id.card_view);
            this.shareIcon = (ImageView) itemView.findViewById(R.id.shareIcon);
            this.newsAbstractDescription = (TextView) itemView.findViewById(R.id.abstractDescription);
            this. relativeLayoutCard = (RelativeLayout) itemView.findViewById(R.id.relativeLayoutCard);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_view, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        TextView newsTitle = holder.newsTitle;
        TextView newsPublishedDate = holder.newsPublishedDate;
        TextView newsAbstractDescription = holder.newsAbstractDescription;
        ImageView shareIcon = holder.shareIcon;
        ImageView newsImageView = holder.newsImageView;
        CardView newsCardView = holder.newsCardView;
        RelativeLayout relativeLayoutCard = holder.relativeLayoutCard;

        // date parsing and setting to the card view
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat desiredDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        java.util.Date currentDate = null;
        try
        {
            currentDate = currentDateFormat.parse(newsDataSet.get(listPosition).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String desiredDate = desiredDateFormat.format(currentDate);
        newsPublishedDate.setText(desiredDate);

        // setting title to card view
        newsTitle.setText(newsDataSet.get(listPosition).getTitle());

        // modifying and setting abstract to card view
        String abstractFullText = newsDataSet.get(listPosition).getAbstractDescription();
        if (abstractFullText.length() > 70) {
            abstractFullText = abstractFullText.substring(0, 67) + "...";
        }
        newsAbstractDescription.setText(abstractFullText);

        // setting image to card view
        String image = (newsDataSet.get(listPosition).getThumbnail());
        if (image.isEmpty()) {
            newsImageView.setImageResource(R.drawable.image_placeholder); //default image if empty
        } else {
            // imageLoader.DisplayImage(image, imageView);
            Picasso.with(context).load(image) .placeholder(R.drawable.image_placeholder).into(newsImageView);
        }
        // setting bounce animation
        animFadeOut = AnimationUtils.loadAnimation(context, R.anim.bounce);
        newsCardView.startAnimation(animFadeOut);

        // on click of share button
        shareIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, newsDataSet.get(listPosition).getUrl());
                sendIntent.setType("text/plain");
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(sendIntent);
            }
        });
        // on click of image view
        relativeLayoutCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", newsDataSet.get(listPosition).getUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return newsDataSet.size();
    }

    private void setImageHeightAndWidth(Context context,ImageView standardThumbnail)
    {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;
        RelativeLayout.LayoutParams parameters;
        int width, height;
        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                width=120;
                height = 120;
                parameters=   new RelativeLayout.LayoutParams(width,height);
                standardThumbnail.setLayoutParams(parameters);
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                Log.d("size::","normal");
                width=160;
                height = 160;
                parameters = new RelativeLayout.LayoutParams(width,height);
                standardThumbnail.setLayoutParams(parameters);
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                width=240;
                height = 240;
                parameters = new RelativeLayout.LayoutParams(width,height);
                standardThumbnail.setLayoutParams(parameters);
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                width=320;
                height = 320;
                parameters = new RelativeLayout.LayoutParams(width,height);
                standardThumbnail.setLayoutParams(parameters);
            default:
                width=120;
                height = 120;
                parameters = new RelativeLayout.LayoutParams(width,height);
                standardThumbnail.setLayoutParams(parameters);
        }
    }
}

