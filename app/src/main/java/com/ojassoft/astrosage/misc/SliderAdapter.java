package com.ojassoft.astrosage.misc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.ui.act.ActFullScreenSlider;

/**
 * Created by ojas on १२/१/१८.
 */



    public class SliderAdapter extends PagerAdapter {

      private  AstroShopItemDetails itemdetail;

    private LayoutInflater inflater;
        private Context context;

        public SliderAdapter(Context context, AstroShopItemDetails itemdetail) {
            this.context = context;
            this.itemdetail=itemdetail;

            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return itemdetail.getLargeImageList().size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View myImageLayout = inflater.inflate(R.layout.slides, view, false);
            NetworkImageView myImage = (NetworkImageView) myImageLayout
                    .findViewById(R.id.image);
            myImage.setImageUrl(itemdetail.getLargeImageList().get(position), VolleySingleton.getInstance(context).getImageLoader());
            view.addView(myImageLayout, 0);
            myImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    Intent intent=new Intent(context,ActFullScreenSlider.class);
                    bundle.putInt("Index",position);
                    bundle.putSerializable("Data",itemdetail);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            return myImageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }

