package com.ojassoft.astrosage.misc;

import android.content.Context;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

//import com.google.analytics.tracking.android.Log;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopUserAddressModel;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ojas on ३/५/१६.
 */
public class AstroShopAllUserAddressAdapter extends RecyclerView.Adapter<AstroShopAllUserAddressAdapter.ViewHolder> {
    private List<AstroShopUserAddressModel> _nameList;
    private Context context;
    private int itemPosition;
    private Bundle bundle;


    public AstroShopAllUserAddressAdapter() {
    }

    public AstroShopAllUserAddressAdapter(Context context, List<AstroShopUserAddressModel> _nameList) {
        this.context = context;
        this._nameList = new ArrayList<AstroShopUserAddressModel>();
        this._nameList = _nameList;

    }


    // Create new views (invoked by the layout manager)
    @Override
    public AstroShopAllUserAddressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lay_custom_cardview_astroshopalluseraddress, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        try {
            String[] separated = null;


            //Log.e("outposition" + position);
            viewHolder.item_name.setText(_nameList.get(position).getUserName().trim() + "\n" + _nameList.get(position).getUserAddress().trim() + "\n" + _nameList.get(position).getUsercity().trim() + "\n" + _nameList.get(position).getUserState().trim() + "\n" + _nameList.get(position).getUserPincode().trim() + "\n" + _nameList.get(position).getUserCountry().trim());
            if (_nameList.get(position).getIsDefault().equalsIgnoreCase("1")) {
                viewHolder.radio_check_btn.setChecked(true);
            } else {
                viewHolder.radio_check_btn.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(context, "EXCEP", Toast.LENGTH_LONG).show();
        }
        //viewHolder.genderImage.setText(itemsData[position].getTitle());


    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        private TextView item_name;
        private RadioButton radio_check_btn;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            cardView = (CardView) itemLayoutView.findViewById(R.id.card_view);

            item_name = (TextView) itemLayoutView.findViewById(R.id.text_title);
            radio_check_btn = (RadioButton) itemLayoutView.findViewById(R.id.radio_check_btn);

            item_name.setTypeface(((BaseInputActivity) context).robotRegularTypeface);



           /* cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    setDataTosendDescription(getLayoutPosition());

                }
            });
*/
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        int size = _nameList.size();
        //Log.e("Size=" + size);
        return size;
    }

   /* private void setDataTosendDescription(int itemPosition) {
        Intent itemdescriptionIntent = new Intent(context, AstroShopItemDescription.class);
        bundle = new Bundle();
        itemdescriptionIntent.putExtras(bundle);
        if (itemdescriptionIntent != null) {
            if (!_nameList.get(itemPosition).getP_OutOfStock().equalsIgnoreCase("true"))
                context.startActivity(itemdescriptionIntent);
        }

    }


*/


}
