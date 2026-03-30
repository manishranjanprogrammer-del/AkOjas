package com.ojassoft.astrosage.varta.twiliochat.chat.history;

import android.view.View;

public interface RecyclerClickListner {
    void onItemClick(int position, View v);

    void onItemLongClick(int position, View v);
}
