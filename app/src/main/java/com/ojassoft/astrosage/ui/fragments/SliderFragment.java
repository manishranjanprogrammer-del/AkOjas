package com.ojassoft.astrosage.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.SliderModal;
import com.ojassoft.astrosage.ui.act.ActPlayVideo;
import com.ojassoft.astrosage.utils.CGlobalVariables;

/**
 * Created by ojas on २१/७/१६.
 */
public class SliderFragment extends Fragment {
    private View view;
    private TextView tvTitle,tvSubtitle;
    private NetworkImageView imgslide;
    private SliderModal modal;
    private Bundle bundle;
    //private YouTubeThumbnailView thumbnail;
   // private  ThumbnailListener thumbnailListener;
    public SliderFragment(){}
    //public static YouTubeThumbnailLoader loader;
    private int width=0;

    //http://img.youtube.com/vi/hpUBpQBs5fc/0.jpg

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.slider_view_learn_astro, container, false);
        } else {
            if (((ViewGroup) view.getParent()) != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }
        bundle=this.getArguments();
        if(bundle!=null)
        {
            modal=(SliderModal) bundle.get("DATA");

        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getActivity(), ActPlayVideo.class);
                i.putExtra("DATA",bundle);
                startActivity(i);

            }
        });
      //  thumbnailListener = new ThumbnailListener();
        init(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    private void init(View view)
    {
        //http://img.youtube.com/vi/hpUBpQBs5fc/0.jpg
        String url= CGlobalVariables.youTubeBaseUrl+modal.getVideo_url().trim()+"/0.jpg";
        //thumbnail=(YouTubeThumbnailView)  view.findViewById(R.id.thumbnail);
        imgslide=(NetworkImageView) view.findViewById(R.id.imgSlides);
        tvTitle=(TextView) view.findViewById(R.id.tvTitle);
        tvSubtitle=(TextView) view.findViewById(R.id.tvSubTitle);
      //  imgslide.setImageResource(R.drawable.ic_yearly);
        if(modal.getUrl()!=null&& !modal.getUrl().isEmpty())
        {
            url= modal.getUrl();
        }
        VolleySingleton.getInstance(getActivity()).getImageLoader().get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                imgslide.setImageBitmap(imageContainer.getBitmap());

            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        imgslide.setImageUrl(url, VolleySingleton.getInstance(getActivity()).getImageLoader());
        tvTitle.setText(modal.getTitle());
        tvSubtitle.setText(modal.getDescription());

    }




}



