package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.interfacefile.RecyclerClickListner;
import com.ojassoft.astrosage.varta.model.LanguageModel;
import com.ojassoft.astrosage.varta.ui.activity.VartaReqJoinActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.List;

public class ChooseVartaLanguageAdapter extends RecyclerView.Adapter<ChooseVartaLanguageAdapter.MyViewHolder> {

    private RecyclerClickListner recyclerClickListner;
    private Context context;
    private List<LanguageModel> languageModels;

    public ChooseVartaLanguageAdapter(Context context, List<LanguageModel> languageModels) {
        this.languageModels = languageModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.items_varta_choose_language, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (languageModels != null && languageModels.size() > position) {
            final LanguageModel languageModel = languageModels.get(position);
            if (languageModel == null) {
                return;
            }
            holder.radioLang.setText(languageModel.getLanguageName() + "");
            if (languageModel.isSelected()) {
                holder.radioLang.setChecked(true);
            } else {
                holder.radioLang.setChecked(false);
            }
            final int pos = position;
            holder.radioLang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LanguageModel languageModel = languageModels.get(pos);
                    if(holder.radioLang.isChecked()) {
                        languageModel.setSelected(true);
                    }else {
                        languageModel.setSelected(false);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (languageModels == null) return 0;
        return languageModels.size();
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout containerLayout;
        public CheckBox radioLang;

        public MyViewHolder(View view) {
            super(view);

            containerLayout = view.findViewById(R.id.containerLayout);
            radioLang = view.findViewById(R.id.radioLang);
            //radioLang.setTypeface(Typeface.createFromAsset(context.getAssets(), AppConstants.FONT_ROBOTO_REGULAR));
            FontUtils.changeFont(context, radioLang, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        }
    }
}
