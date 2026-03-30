package com.ojassoft.astrosage.customadapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

//import com.google.analytics.tracking.android.Log;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.IHandleSavedCards;
import com.ojassoft.astrosage.model.CardSaveModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by ojas on १/७/१६.
 */
public class SaveCardAdapter extends RecyclerView.Adapter<SaveCardAdapter.ViewHolder> {
    private List<CardSaveModel> _nameList;
    private Context context;
    private int itemPosition;
    private Bundle bundle;
    private CardSaveModel itemdetails;
    private Typeface typeface;
    private IHandleSavedCards isaveCards;
    private int selectedpos;
    public SaveCardAdapter()
    {}

    public SaveCardAdapter(Context context, List<CardSaveModel> _nameList,Typeface typeface,IHandleSavedCards isaveCards,int selectedpos) {
        this.context = context;
        this._nameList = _nameList;
        this.typeface=typeface;
        this.isaveCards=isaveCards;
        this.selectedpos=selectedpos;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public SaveCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lay_card_saveadpter, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        try {
            itemdetails = new CardSaveModel();
            itemdetails = _nameList.get(position);
            //Log.e("outposition" + position);

          //  viewHolder.tvCardno.setText(itemdetails.getCardNumber());

            viewHolder.tvCardno.setText(maskCardNumber(itemdetails.getCardNumber(), "xxxx-xxxx-xxxx-####"));

            //System.out.println(maskCardNumber("1234123412341234", "xxxx-xxxx-xxxx-####"));
            if(position==selectedpos)
            {
                viewHolder.rbCard.setChecked(true);
                viewHolder.etCardCvv.setText("");
                viewHolder.llMain.setVisibility(View.VISIBLE);

            }
            else
            {
                viewHolder.rbCard.setChecked(false);
                viewHolder.llMain.setVisibility(View.GONE);

            }


            viewHolder.rbCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedpos = position;
                    SaveCardAdapter.this.notifyDataSetChanged();
                  //  viewHolder.llMain.setVisibility(View.VISIBLE);
                }
            });

           /* viewHolder.rbCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                     //   rbCard.setChecked(true);
                        viewHolder.llMain.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                      //  rbCard.setChecked(false);
                        viewHolder.llMain.setVisibility(View.GONE);

                    }
                }
            });*/

            viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isaveCards.deleteCard(position);

                }
            });


            viewHolder.btn_pay_complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.e("Adapter clicked");

                    if (viewHolder.etCardCvv.getText().toString().trim().isEmpty() || viewHolder.etCardCvv.getText().toString().trim().length() < 3) {
                       // viewHolder.etCardCvv.setError(context.getResources().getString(R.string.enter_valid_cvv));
                        viewHolder.etCardCvv_layout.setError(context.getResources().getString(R.string.enter_valid_cvv));

                        requestFocus(viewHolder.etCardCvv);
                    }
                    else
                    {
                        isaveCards.ProceedPay(viewHolder.etCardCvv.getText().toString(),position);

                    }
                }
            });

       /*     if (itemdetails.getPImgUrl() != null && !itemdetails.getPImgUrl().isEmpty()) {
                viewHolder.image_url.setImageUrl(itemdetails.getPImgUrl(), imageLoader);
                //Log.e("Image hit" + position);

            } else {
                viewHolder.image_url.setImageDrawable(null);
            }

            if (itemdetails.getPName().contains("(")) {
                viewHolder.item_des.setVisibility(View.VISIBLE);
                separated = itemdetails.getPName().split("\\(");
                viewHolder.item_name.setText(separated[0].trim());
                viewHolder.item_des.setText("(" + separated[1].trim());
            } else {
                viewHolder.item_name.setText(itemdetails.getPName());
            }
            if (itemdetails.getP_OutOfStock().equalsIgnoreCase("true")) {
                viewHolder.img_outstock.setVisibility(View.VISIBLE);
                viewHolder.img_outstock.setImageResource(R.drawable.outofstock);

            } else {
                viewHolder.img_outstock.setVisibility(View.INVISIBLE);
            }*/

       //     viewHolder.item_cost.setText(context.getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getPPriceInDoller()), 2) + " / " + context.getResources().getString(R.string.astroshop_rupees_sign) + roundFunction(Double.parseDouble(itemdetails.getPPriceInRs()), 2));
            //  viewHolder.setIsRecyclable(false);

        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(context, "EXCEP", Toast.LENGTH_LONG).show();
        }
        //viewHolder.genderImage.setText(itemsData[position].getTitle());


    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private RadioButton rbCard;
        private TextView tvCardno,tvDelete,tvTrustMsg;
        private LinearLayout llMain;
        private EditText etCardCvv;
        private Button btn_pay_complete;
        private TextInputLayout etCardCvv_layout;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            cardView = (CardView) itemLayoutView.findViewById(R.id.card_view);
            rbCard=(RadioButton) itemLayoutView.findViewById(R.id.rbCard);
            tvCardno=(TextView) itemLayoutView.findViewById(R.id.tvCardno);
            tvDelete=(TextView) itemLayoutView.findViewById(R.id.tvDelete);
            tvDelete.setTypeface(typeface);
            tvTrustMsg=(TextView) itemLayoutView.findViewById(R.id.tvTrustMsg);
            tvTrustMsg.setTypeface(typeface);
            llMain=(LinearLayout) itemLayoutView.findViewById(R.id.llMain);
            etCardCvv=(EditText) itemLayoutView.findViewById(R.id.etCardCvv);
            etCardCvv_layout = (TextInputLayout) itemLayoutView.findViewById(R.id.etCardCvv_layout);
            btn_pay_complete=(Button) itemLayoutView.findViewById(R.id.btn_pay_complete);
            btn_pay_complete.setTypeface(typeface);
            etCardCvv.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (etCardCvv.getText().toString().trim().isEmpty() || etCardCvv.getText().toString().trim().length() < 3) {
                        etCardCvv_layout.setErrorEnabled(true);
                        etCardCvv_layout.setError(context.getResources().getString(R.string.enter_valid_cvv));
                        requestFocus(etCardCvv);
                        etCardCvv.getBackground().setColorFilter(etCardCvv.getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                    } else {
                        etCardCvv_layout.setError(null);
                        etCardCvv.getBackground().setColorFilter(null);
                        etCardCvv.getBackground().setColorFilter(etCardCvv.getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);

                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (etCardCvv.getText().toString().trim().isEmpty() || etCardCvv.getText().toString().trim().length() < 3) {
                        etCardCvv_layout.setError(context.getResources().getString(R.string.enter_valid_cvv));
                        requestFocus(etCardCvv);
                        etCardCvv.getBackground().setColorFilter(context.getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                    } else {
                        etCardCvv_layout.setErrorEnabled(false);
                        etCardCvv.getBackground().setColorFilter(null);
                        etCardCvv.getBackground().setColorFilter(context.getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);



                    }
                }
            });
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                }
            });



        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        int size = _nameList.size();
        //Log.e("Size=" + size);
        return size;
    }





    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ((Activity)context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public static String maskCardNumber(String cardNumber, String mask) {

        // format the number
        int index = 0;
        StringBuilder maskedNumber = new StringBuilder();
        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);
            if (c == '#') {
                maskedNumber.append(cardNumber.charAt(index));
                index++;
            } else if (c == 'x') {
                maskedNumber.append(c);
                index++;
            } else {
                maskedNumber.append(c);
            }
        }

        // return the masked number
        return maskedNumber.toString();
    }
}