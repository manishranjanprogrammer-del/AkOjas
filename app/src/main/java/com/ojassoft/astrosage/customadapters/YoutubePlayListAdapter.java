package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.YoutubeData;
import com.ojassoft.astrosage.jinterface.OnLoadMoreListener;
import com.ojassoft.astrosage.ui.act.YoutubePlaylist;
import com.ojassoft.astrosage.utils.CGlobalVariables;

import java.util.ArrayList;
import java.util.List;

public class YoutubePlayListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<YoutubeData> moviesList;
    Context context;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;
    //ArrayList<String> videoIdList;
    int selectedRow = 0;
    MyViewHolder myViewHolder;


    public YoutubePlayListAdapter(Context ctx, RecyclerView recyclerView, List<YoutubeData> moviesList) {
        context = ctx;
        this.moviesList = moviesList;
        /*videoIdList = new ArrayList<String>();
        for (int i = 0; i < moviesList.size(); i++) {
            videoIdList.add(moviesList.get(i).getObjectInfo().getVedioId());
        }*/

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = YoutubePlayListAdapter.this.moviesList.size();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading) {
                    if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

            return new MyViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    private void startVedio(int position) {
        if (position != selectedRow) {
            ((YoutubePlaylist) context).initializeVedio(position);
            selectedRow = position;
            notifyDataSetChanged();
        }

    }


    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return moviesList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    /*@Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }*/


   /* @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Bean bean = moviesList.get(position);
        holder.desc.setText(bean.getSnippet().getDescription());
        holder.imageView.setImageUrl(bean.getSnippet().getThumbnail().getTdefault().getUrl(), VolleySingleton.getInstance(context).getImageLoader());
    }*/

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            myViewHolder = (MyViewHolder) holder;
            YoutubeData bean = moviesList.get(position);
            String sourceString = "<b>" + bean.getSnippet().getTitle() + ": " + "</b> " + bean.getSnippet().getDescription();
            myViewHolder.desc.setText(Html.fromHtml(sourceString));
            myViewHolder.imageView.setImageUrl(bean.getSnippet().getThumbnail().getTmedium().getUrl(), VolleySingleton.getInstance(context).getImageLoader());
            if (selectedRow == position) {
                myViewHolder.linearLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary_day_night));
            } else {
                myViewHolder.linearLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.bg_card_view_color));
            }
            if(getItemCount()-1 == position){
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(25, 25, 25, 25);
                myViewHolder.card_view.setLayoutParams(buttonLayoutParams);
                //Log.e("myViewHolder.card_view","1");
            }else{
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(25, 25, 25, 0);
                myViewHolder.card_view.setLayoutParams(buttonLayoutParams);
                //Log.e("myViewHolder.card_view","2");
            }
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return moviesList == null ? 0 : moviesList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;

    }

    public void addData(ArrayList<YoutubeData> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            moviesList.add(arrayList.get(i));
            //videoIdList.add(arrayList.get(i).getObjectInfo().getVedioId());
        }

        notifyDataSetChanged();
    }

    public void addNullVal() {
        moviesList.add(null);
        notifyDataSetChanged();
    }

    public void removeNullVal() {
        moviesList.remove(null);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView desc;
        public NetworkImageView imageView;
        public LinearLayout linearLayout;
        public CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            desc = (TextView) view.findViewById(R.id.textview);

            imageView = (NetworkImageView) view.findViewById(R.id.imageview);
            linearLayout = (LinearLayout) view.findViewById(R.id.main_container);
            card_view = (CardView) view.findViewById(R.id.card_view);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startVedio(getLayoutPosition());

                }
            });
            desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startVedio(getLayoutPosition());
                }
            });
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startVedio(getLayoutPosition());
                }
            });

        }
    }

    public void updateSelectedPosition(int position) {
        selectedRow = position;
        notifyDataSetChanged();
        //selectedRow ++;
    }
}