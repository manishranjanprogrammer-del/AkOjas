package com.ojassoft.astrosage.customadapters;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.HoraMetadata;
import com.ojassoft.astrosage.ui.act.HoraMeaning;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.List;

public class CustomAdapterforHora extends BaseAdapter {

    Activity context;

    LayoutInflater inflater = null;
    Typeface typeFace;
    List<HoraMetadata> data;
    List<HoraMetadata> datatime;
    List<HoraMetadata> datatimeexit;
    String Horatag;

    public CustomAdapterforHora(Activity context, List<HoraMetadata> data,
                                List<HoraMetadata> datatime, List<HoraMetadata> datatimeexit,
                                String Horatag) {
        this.context = context;
        this.data = data;
        this.datatime = datatime;
        this.datatimeexit = datatimeexit;
        this.Horatag = Horatag;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.typeFace = ((InputPanchangActivity) context).regularTypeface;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        // Toast.makeText(context, data.size()+"", Toast.LENGTH_SHORT).show();
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressLint("InlinedApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView = null;
        try {
            rowView = convertView;
            Holder holder;
            if (convertView == null) {
                rowView = inflater.inflate(R.layout.activity_hora_custom_list,
                        null);
                holder = new Holder();
                holder.headerview = (LinearLayout) rowView
                        .findViewById(R.id.headerview);

                holder.tvlistplanet = (TextView) rowView
                        .findViewById(R.id.tvlistplanet);
                holder.tvMuhurat = (TextView) rowView
                        .findViewById(R.id.tvMuhurat);

                holder.tvlistentrytme = (TextView) rowView
                        .findViewById(R.id.tvlistentrytme);
                holder.tvlistexittme = (TextView) rowView
                        .findViewById(R.id.tvlistexittme);

                rowView.setTag(holder);
            } else {
                holder = (Holder) rowView.getTag();
            }
            holder.headerview.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    String planetMeaning = null;
                    String planet = null;

                    planetMeaning = data.get(position).getPlanetmeaning();
                    planet = data.get(position).getPlanetdata();
                    if (planet != null && planetMeaning != null) {
                        FragmentManager fm = ((InputPanchangActivity) context).getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();

                        Fragment prev = fm.findFragmentByTag("HOME_INPUT_LANGUAGE");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        //         ft.addToBackStack(null);
                        /*CustomDialog clfd = new CustomDialog() {
                        };*/
                        HoraMeaning horaMeaning = HoraMeaning.newInstance(Horatag, planet, planetMeaning);
                        horaMeaning.show(fm, "HOME_INPUT_LANGUAGE");
                        ft.commit();

                        /*Intent horaMeaningIntent = new Intent(context,
                                com.ojassoft.astrosage.ui.act.HoraMeaning.class);
                        horaMeaningIntent.putExtra("horaPlanet", planet);
                        horaMeaningIntent.putExtra("horaPlanetMeaning",
                                planetMeaning);
                        horaMeaningIntent.putExtra("Horatag", Horatag);
                        context.startActivity(horaMeaningIntent);*/
                    }
                }
            });
            if (Horatag.equalsIgnoreCase("doghati")) {
                /*
                 * holder.tvlistentrytme .setPadding(100, 0, 0, 0);
                 * holder.tvlistexittme.setPadding(100, 0, 0, 0);
                 */

                //	holder.tvMuhurat.setVisibility(View.VISIBLE);
                holder.tvlistplanet.setText(data.get(position).getPlanetdata());
                holder.tvlistentrytme.setText(data.get(position).getPlanetmeaning());

				/*holder.tvlistentrytme.setText(datatime.get(position)
                        .getEntertimedata());*/
                //	holder.tvlistentrytme.setVisibility(View.GONE);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT, 0.8f);
                holder.tvlistexittme.setLayoutParams(param);
                holder.tvlistexittme.setText(CUtils.getTimeInFormate(datatime.get(position)
                        .getEntertimedata()).replace("+", context.getString(R.string.tomorrow_label) + "\n") + " - "
                        + CUtils.getTimeInFormate(datatimeexit.get(position).getExittimedata()).replace("+", context.getString(R.string.tomorrow_label) + "\n"));


            } else {
                holder.tvMuhurat.setVisibility(View.GONE);
                holder.tvlistplanet.setText(data.get(position).getPlanetdata());
                holder.tvlistentrytme.setText(CUtils.getTimeInFormate(datatime.get(position)
                        .getEntertimedata()).replace("+", context.getString(R.string.tomorrow_label) + "\n"));
                holder.tvlistexittme.setText(CUtils.getTimeInFormate(datatimeexit.get(position)
                        .getExittimedata()).replace("+", context.getString(R.string.tomorrow_label) + "\n"));


            }
            // holder.tvlistplanet.setTypeface(typeFace);

		/*	holder.tvlistentrytme.setText(datatime.get(position)
					.getEntertimedata());
			holder.tvlistexittme.setText(datatimeexit.get(position)
					.getExittimedata());
*/
        } catch (Exception ex) {
        }
        return rowView;
    }

    public class Holder {
        TextView tvlistplanet, tvMuhurat;
        TextView tvlistentrytme;
        TextView tvlistexittme;
        LinearLayout headerview;
    }

}
