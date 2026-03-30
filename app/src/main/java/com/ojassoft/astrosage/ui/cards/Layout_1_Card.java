package com.ojassoft.astrosage.ui.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.Bean1;


/**
 * Created by ojas on 7/2/17.
 * This Layout contains Title and Description
 *
 * Format :-
 * Title
 * Description
 */

public class Layout_1_Card extends RelativeLayout{

    private Context context;
    private Bean1 bean;
    private TextView tvTitle;
    private TextView tvDesc;

    public Layout_1_Card(Context context, Bean1 bean) {
        super(context);
        this.bean =bean;
        this.context = context;
        init();
        checkBeanForCalculation();
    }

    private void init(){

        // Set the CardView layoutParams
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );

        setLayoutParams(params);

        View view = LayoutInflater.from(context).inflate(R.layout.layout1, null);

        tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        tvDesc = (TextView)view.findViewById(R.id.tvDesc);

       /* // Set CardView corner radius
        setRadius(9);

        // Set cardView content padding
        setContentPadding(15, 15, 15, 15);

        // Set a background color for CardView
        setCardBackgroundColor(Color.parseColor("#000000"));

        // Set the CardView maximum elevation
        setMaxCardElevation(15);

        // Set CardView elevation
        setCardElevation(9);*/

        addView(view);
    }

    private void checkBeanForCalculation(){
        if(bean.isWithUrl()){

        }else if(bean.isLiabraryNeed()){

        }else{
            tvTitle.setText(bean.getTitle());
            tvDesc.setText(bean.getDesc());
        }
    }
}
