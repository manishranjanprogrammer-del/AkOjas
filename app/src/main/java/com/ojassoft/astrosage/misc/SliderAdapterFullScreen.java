package com.ojassoft.astrosage.misc;

import android.content.Context;

import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.customcontrols.CustomNetworkimageView;

import java.util.ArrayList;

/**
 * Created by ojas on १२/१/१८.
 */



/**
 * Created by ojas on १२/१/१८.
 */



public class SliderAdapterFullScreen extends PagerAdapter {

    private ArrayList<String> largeImages;

    private LayoutInflater inflater;
    private Context context;

    public SliderAdapterFullScreen(Context context, ArrayList<String> largeImages) {
        this.context = context;
        this.largeImages=largeImages;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return largeImages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.fullscreen_slides, view, false);
        CustomNetworkimageView myImage = (CustomNetworkimageView) myImageLayout.findViewById(R.id.full_image);
        myImage.setImageUrl(largeImages.get(position), VolleySingleton.getInstance(context).getImageLoader());
        myImage.setZoom(0);
        view.addView(myImageLayout, 0);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Bundle bundle=new Bundle();
                Intent intent=new Intent(context,ActFullScreenSlider.class);
                bundle.putInt("Index",position);
                bundle.putSerializable("Data",largeImages);
                intent.putExtras(bundle);
                context.startActivity(intent);*/
            }
        });
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}

