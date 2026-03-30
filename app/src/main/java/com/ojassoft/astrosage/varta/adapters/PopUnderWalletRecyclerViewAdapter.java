package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.dialog.QuickRechargeBottomSheet;
import com.ojassoft.astrosage.varta.model.WalletAmountBean;
import com.ojassoft.astrosage.varta.ui.activity.WalletActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;


public class PopUnderWalletRecyclerViewAdapter extends RecyclerView.Adapter<PopUnderWalletRecyclerViewAdapter.MyViewHolder> {
    private WalletAmountBean walletAmountBean;
    private Context context;
    int selectedRow = 4;


    public PopUnderWalletRecyclerViewAdapter(Context context, WalletAmountBean walletAmountBean) {
        this.context = context;
        this.walletAmountBean = walletAmountBean;
        getAmountLength();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pop_up_recharge_services, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
        WalletAmountBean.Services data = walletAmountBean.getServiceList().get(position);
        //Log.e("tagwallet", " : "+data.getOffermessage());
        // viewHolder.amount.setText(context.getResources().getString(R.string.rs_sign)  +CUtils.convertAmtIntoIndianFormat( data.getActualraters() ));
        //Log.e("convertAmtIntoIndianFormat", CUtils.convertAmtIntoIndianFormat( data.getActualraters() ));
        viewHolder.amount.setText(CUtils.convertAmtIntoIndianFormat( data.getActualraters()));
        if (!data.getOffermessage().isEmpty()) {
            viewHolder.offer.setVisibility(View.VISIBLE);
//            viewHolder.imageFl.setVisibility(View.VISIBLE);

//            viewHolder.imageFl.setBackground(writeTextOnDrawable(R.drawable.red_ribbon,data.getOffermessage()));

        } else {

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,0);
            viewHolder.linLayoutCurrencySymbol.setLayoutParams(params);
            viewHolder.offer.setVisibility(View.GONE);

            viewHolder.imageFl.setVisibility(View.GONE);
        }
        viewHolder.offer.setText(data.getOffermessage());
        FontUtils.changeFont(context, viewHolder.amount, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        //FontUtils.changeFont(context, viewHolder.offer, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, viewHolder.offer, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRow = position;
                if (context instanceof WalletActivity) {
                    ((WalletActivity) context).setSelectedPosition(position);
                } else {
                    QuickRechargeBottomSheet.getInstance().setSelectedPosition(position);
                }
                notifyDataSetChanged();
            }
        });
        /*if (selectedRow == position) {
            viewHolder.container.setBackground(context.getResources().getDrawable(R.drawable.purple_rect));
            viewHolder.amount.setTextColor(context.getResources().getColor(R.color.white));
        } else {*/
        // viewHolder.container.setBackground(context.getResources().getDrawable(R.drawable.bg_border_purple));
        //viewHolder.amount.setTextColor(context.getResources().getColor(R.color.wallet_text_color));
        //}
    }

    @Override
    public int getItemCount() {
        return walletAmountBean.getServiceList().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amount,txtViewRupees, offer;
        LinearLayout linLayoutCurrencySymbol;
        ConstraintLayout container;
        FrameLayout imageFl;

        public MyViewHolder(View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.recharge1_tv);
            txtViewRupees = itemView.findViewById(R.id.txtViewRupees);
            linLayoutCurrencySymbol = itemView.findViewById(R.id.linLayoutCurrencySymbol);
            offer = itemView.findViewById(R.id.recharge1_offer_tv);
            container = itemView.findViewById(R.id.recharge1_ll);
            imageFl = itemView.findViewById(R.id.imagefl);
            amount.setTextSize(fontSize);
            txtViewRupees.setTextSize(fontSize+2);


        }
    }

    int fontSize = 18;
    private void getAmountLength(){
        for (WalletAmountBean.Services item : walletAmountBean.getServiceList()) {
            if (CUtils.convertAmtIntoIndianFormat( item.getActualraters()).length() > 5){
                fontSize = 13;
            }else {
                fontSize = 15;
            }

        }
    }


    private BitmapDrawable writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.createFromAsset(context.getAssets(), CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(context, 25));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);
        canvas.rotate(-45,0,0);
        //If the text is bigger than the canvas , reduce the font size
        if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(context, 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = 0;//(canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset
        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) /8)) ;

        canvas.drawText(text, xPos, yPos, paint);
        return new BitmapDrawable(context.getResources(), bm);
    }

    public static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;
        return (int) ((nDP * conversionScale) + 0.5f) ;
    }
}
