package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.PlaylistData;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ojas on २५/७/१६.
 */
public class PlaylistAdapter extends BaseAdapter {

    private final List<PlaylistData> entries;
    //private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
    private final LayoutInflater inflater;
    private Context context;


    public PlaylistAdapter(Context context, List<PlaylistData> entries) {
        this.entries = entries;
        //thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
        inflater = LayoutInflater.from(context);
        this.context = context;
        //Log.e("Size is", "" + entries.size());
    }

    public void releaseLoaders() {
        /*for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
            loader.release();
        }*/
    }


    @Override
    public PlaylistData getItem(int position) {
        return entries.get(position);
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        PlaylistData entry = entries.get(position);
        view = inflater.inflate(R.layout.playlist_item, parent, false);
        TextView playlistName = ((TextView) view.findViewById(R.id.playlist_name));
        ImageView playlistImage = ((ImageView) view.findViewById(R.id.playlist_image));
        playlistImage.setImageBitmap(entry.getBitmap());
        playlistName.setText(entry.getPlaylistName());
        playlistName.setTypeface(((BaseInputActivity) context).robotMediumTypeface);
        return view;
    }


}



