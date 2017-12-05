package com.yalantis.reddittestclient.flow.link;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yalantis.reddittestclient.R;
import com.yalantis.reddittestclient.data.Link;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ak on 03.12.2017.
 */

public class LinkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_PROGRESS = 0;
    private final LinkClickListener clickListener;
    private boolean isProgressEnabled = false;
    private List<Link> linkList = new ArrayList<>();

    LinkAdapter(LinkClickListener clickListener) {
        this.clickListener = clickListener;
    }

    void setLinks(List<Link> links, boolean clearList) {
        if (clearList) {
            linkList.clear();
        }
        linkList.addAll(links);
        if (clearList) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(linkList.size() - links.size(), links.size());
        }
    }

    void enableProgress(boolean isProgressEnabled) {
        this.isProgressEnabled = isProgressEnabled;
        if (isProgressEnabled) {
            notifyItemInserted(linkList.size());
        } else {
            notifyItemRemoved(linkList.size());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_TYPE_ITEM) {
            View linkView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.link_card, parent, false);
            viewHolder = new LinkViewHolder(linkView, clickListener);

        } else {
            View progressView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.link_progress, parent, false);
            viewHolder = new ProgressViewHolder(progressView);

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LinkViewHolder && position < linkList.size()) {
            Link link = linkList.get(position);
            ((LinkViewHolder) holder).bindData(link);
        }
    }

    @Override
    public int getItemCount() {
        return isProgressEnabled ? linkList.size() + 1 : linkList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return isProgressEnabled && position >= linkList.size() ? VIEW_TYPE_PROGRESS : VIEW_TYPE_ITEM;
    }

    interface LinkClickListener {
        void onLinkClick(Link link);
    }

    static class LinkViewHolder extends RecyclerView.ViewHolder {

        final LinkClickListener linkClickListener;
        private Link link;

        private CardView linkCard;
        private TextView titleTV;
        private TextView authorTV;
        private TextView subredditTV;
        private TextView createdTV;
        private ImageView thumbnailIV;
        private TextView ratingTV;
        private TextView commentsNumberTV;

        LinkViewHolder(View itemView, LinkClickListener clickListener) {
            super(itemView);
            linkClickListener = clickListener;
            linkCard = itemView.findViewById(R.id.link_card);
            titleTV = itemView.findViewById(R.id.link_title);
            authorTV = itemView.findViewById(R.id.link_author);
            subredditTV = itemView.findViewById(R.id.link_subreddit);
            createdTV = itemView.findViewById(R.id.link_created);
            thumbnailIV = itemView.findViewById(R.id.link_thumbnail);
            ratingTV = itemView.findViewById(R.id.link_rating);
            commentsNumberTV = itemView.findViewById(R.id.link_number_of_comments);

            linkCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (linkClickListener != null) {
                        linkClickListener.onLinkClick(link);
                    }
                }
            });
        }

        void bindData(Link link) {
            this.link = link;

            Context context = linkCard.getContext();

            Picasso.with(context)
                    .load(link.getThumbnailURL())
                    .placeholder(R.drawable.thumbnail_blank)
                    .error(R.drawable.thumbnail_blank)
                    .into(thumbnailIV);

            authorTV.setText(context.getString(R.string.link_author, link.getAuthor()));
            subredditTV.setText(context.getString(R.string.link_subreddit, link.getSubReddit()));
            createdTV.setText(context.getString(R.string.link_created, link.getCreatedHours()));
            titleTV.setText(link.getTitle());
            commentsNumberTV.setText(context.getString(R.string.link_number_comments, link.getNumberOfComments()));
            ratingTV.setText(String.valueOf(link.getRating()));
        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {

        ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }
}
