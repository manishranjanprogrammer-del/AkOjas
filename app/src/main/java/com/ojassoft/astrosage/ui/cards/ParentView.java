package com.ojassoft.astrosage.ui.cards;

import android.content.Context;
import android.widget.RelativeLayout;

/**
 * Created by ojas on 8/2/17.
 */

public abstract class ParentView extends RelativeLayout {
    public ParentView(Context context) {
        super(context);
    }
    public abstract  void onChangeFromSettings(int pos);
}
