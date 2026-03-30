package com.ojassoft.astrosage.varta.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.adapters.ChatHistoryAdapter;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

public class ChatHistoryListActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView recyclerview;
    ChatHistoryAdapter chatHistoryAdapter;
    RelativeLayout vartaIconLayout;
    RelativeLayout walletBoxLayout;
    LinearLayout walletLayout, backTitleLayout;
    TextView walletPriceTxt, tvTitle;
    ImageView ivBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history_list);
        initView();
    }

    private void initView() {
        recyclerview = findViewById(R.id.recyclerview);
        vartaIconLayout = findViewById(R.id.varta_icon_layout);
        walletBoxLayout = findViewById(R.id.wallet_box_layout);
        walletLayout = findViewById(R.id.wallet_layout);
        backTitleLayout = findViewById(R.id.back_title_layout);
        walletPriceTxt = findViewById(R.id.wallet_price_txt);
        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);

        vartaIconLayout.setVisibility(View.GONE);
        backTitleLayout.setVisibility(View.VISIBLE);

        recyclerview.setLayoutManager(new LinearLayoutManager(ChatHistoryListActivity.this));
        chatHistoryAdapter = new ChatHistoryAdapter(ChatHistoryListActivity.this, null);
        recyclerview.setAdapter(chatHistoryAdapter);

        FontUtils.changeFont(ChatHistoryListActivity.this, walletPriceTxt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(ChatHistoryListActivity.this, tvTitle, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        walletPriceTxt.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + CUtils.convertAmtIntoIndianFormat(CUtils.getWalletRs(ChatHistoryListActivity.this)));
        ivBack.setOnClickListener(this);
        tvTitle.setText(getResources().getString(R.string.chathistory));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ivBack:
                finish();
                break;
        }
    }
}