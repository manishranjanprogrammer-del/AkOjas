package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.model.NameSwarCombModel;
import com.ojassoft.astrosage.ui.act.NameMatchingOutputActivity;
import com.ojassoft.astrosage.ui.customcontrols.ClickSpan;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;

import java.util.List;

public class NameSwarAdapter extends RecyclerView.Adapter<NameSwarAdapter.MyViewHolder> {

    private Context context;
    private List<NameSwarCombModel> nameSwarCombModels;
    private int rowPos;

    public NameSwarAdapter(Context context, List<NameSwarCombModel> nameSwarCombModels) {
        this.nameSwarCombModels = nameSwarCombModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.items_swar_combo, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (nameSwarCombModels != null && nameSwarCombModels.size() > position) {
            final NameSwarCombModel nameSwarCombModel = nameSwarCombModels.get(position);
            if (nameSwarCombModel == null) {
                return;
            }

            String boySwar = nameSwarCombModel.getBoyName();
            String girlSwar = nameSwarCombModel.getGirlName();

            String clickHereText = context.getResources().getString(R.string.text_click_here);
            String swar2 = context.getString(R.string.text_sawar2);
            swar2 = swar2.replace("#boy", boySwar);
            swar2 = swar2.replace("#girl", girlSwar);

            holder.swarTV.setText(swar2 + " " + clickHereText);
            holder.swarTV.setLinkTextColor(ContextCompat.getColor(context, R.color.primary_orange));

            clickify(holder.swarTV, clickHereText, new ClickSpan.OnClickListener() {
                @Override
                public void onClick() {
                    if (context == null) return;
                    ((NameMatchingOutputActivity) context).handleSwarCombo(nameSwarCombModel);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (nameSwarCombModels == null) return 0;
        return nameSwarCombModels.size();
    }

    public int getItemViewType(int position) {
        return 0;
    }

    private void clickify(TextView view, final String clickableText,
                          final ClickSpan.OnClickListener listener) {

        CharSequence text = view.getText();
        String string = text.toString();
        ClickSpan span = new ClickSpan(listener);

        int start = string.indexOf(clickableText);
        int end = start + clickableText.length();
        if (start == -1) return;

        if (text instanceof Spannable) {
            ((Spannable) text).setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            SpannableString s = SpannableString.valueOf(text);
            s.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            view.setText(s);
        }

        MovementMethod m = view.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            view.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView swarTV;

        public MyViewHolder(View view) {
            super(view);
            swarTV = view.findViewById(R.id.swarTV);

            FontUtils.changeFont(context, swarTV, AppConstants.FONT_ROBOTO_REGULAR);
        }
    }

}
