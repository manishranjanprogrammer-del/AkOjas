package com.ojassoft.astrosage.varta.ui.activity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.NetworkImageView;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MagazineDescActivity extends BaseActivity {
    String title;
    String desc;
    String creator;
    String pubDate;
    String category;
    String imgUrl;
    TextView titleTV;
    TextView descTV;
    TextView pubDateTV;
    TextView creatorTV;
    TextView categoryTV;
    NetworkImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.magazine_desc_layout);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("title");
            desc = bundle.getString("desc");
            creator = bundle.getString("creator");
            pubDate = bundle.getString("pubDate");
            category = bundle.getString("category");
            imgUrl = bundle.getString("imgUrl");
        }
        titleTV = findViewById(R.id.title_tv);
        descTV = findViewById(R.id.desc_tv);
        pubDateTV = findViewById(R.id.date_tv);
        creatorTV = findViewById(R.id.creator_tv);
        categoryTV = findViewById(R.id.category_tv);
        imageView = findViewById(R.id.imageView);

        titleTV.setText(title);
        descTV.setText(Html.fromHtml(desc));
        pubDateTV.setText(getFormatedDate());
        creatorTV.setText(creator);
        categoryTV.setText(category);

        if (imgUrl != null && imgUrl.length() > 0)
            imageView.setImageUrl(imgUrl, VolleySingleton.getInstance(this).getImageLoader());
        imageView.setDefaultImageResId(R.drawable.magazine_default_image);
        imageView.setErrorImageResId(R.drawable.magazine_default_image);

        FontUtils.changeFont(this, titleTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this, pubDateTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, creatorTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, categoryTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

    }

    private String getFormatedDate() {
        String resultDate = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat(
                    "EEE, dd MMM yyyy HH:mm:ss", Locale.US);
            Date date = dateFormat.parse(pubDate);
            DateFormat dateFormat1 = new SimpleDateFormat(
                    "EEE, dd MMM yyyy ", Locale.US);
            resultDate = dateFormat1.format(date);
        } catch (Exception e) {

        }
        return resultDate;
    }
}
