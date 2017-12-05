package com.yalantis.reddittestclient.flow.link;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yalantis.reddittestclient.R;
import com.yalantis.reddittestclient.data.Link;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ak on 03.12.2017.
 */

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.LinkViewHolder> {

    private final LinkClickListener clickListener;
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

    void addLoadingLink() {
        linkList.add(null);
        Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(linkList.size());
            }
        };

        handler.post(runnable);
    }

    void removeLoadingLink() {
        linkList.remove(linkList.size() - 1);
        notifyItemRemoved(linkList.size() + 1);
    }

    List<Link> getLinks() {
        return linkList;
    }

    @Override
    public LinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View linkView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.link_card, parent, false);
        return new LinkViewHolder(linkView, clickListener);
    }

    @Override
    public void onBindViewHolder(LinkViewHolder holder, int position) {
        Link link = linkList.get(position);
        holder.bindData(link);
    }

    @Override
    public int getItemCount() {
        return linkList.size();
    }

    interface LinkClickListener {
        void onLinkClick(Link link);
    }

    static class LinkViewHolder extends RecyclerView.ViewHolder {

        final LinkClickListener linkClickListener;
        private Link link;

        private CardView linkCard;
        private LinearLayout linkMainLayout;
        private TextView titleTV;
        private TextView authorTV;
        private TextView subredditTV;
        private TextView createdTV;
        private ImageView thumbnailIV;
        private TextView ratingTV;
        private TextView commentsNumberTV;
        private ContentLoadingProgressBar progressBar;

        LinkViewHolder(View itemView, LinkClickListener clickListener) {
            super(itemView);
            linkClickListener = clickListener;
            linkCard = itemView.findViewById(R.id.link_card);
            linkMainLayout = itemView.findViewById(R.id.link_main_layout);
            titleTV = itemView.findViewById(R.id.link_title);
            authorTV = itemView.findViewById(R.id.link_author);
            subredditTV = itemView.findViewById(R.id.link_subreddit);
            createdTV = itemView.findViewById(R.id.link_created);
            thumbnailIV = itemView.findViewById(R.id.link_thumbnail);
            ratingTV = itemView.findViewById(R.id.link_rating);
            commentsNumberTV = itemView.findViewById(R.id.link_number_of_comments);
            progressBar = itemView.findViewById(R.id.link_progress_bar);

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

            if (link != null) {
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
            } else {
                linkMainLayout.setVisibility(View.GONE);
                progressBar.show();
            }
        }
    }
}
