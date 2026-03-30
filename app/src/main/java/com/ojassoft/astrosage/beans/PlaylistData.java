package com.ojassoft.astrosage.beans;

import android.graphics.Bitmap;

/**
 * Created by ojas-08 on 3/7/17.
 */
public class PlaylistData {
    Bitmap bitmap;
    String playlistName;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
