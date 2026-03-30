package com.ojassoft.astrosage.model;

import static android.os.Build.VERSION_CODES.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.varta.ui.MiniChatWindow;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;
import java.util.List;


public class SuggestQuestionAdapter extends RecyclerView.Adapter<SuggestQuestionAdapter.MyViewHolder> {
    List<String> questions;
    Context context;

    public SuggestQuestionAdapter ( Context context , ArrayList<String> questionBeanList ) {
        this.context = context;
        this.questions = questionBeanList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {
        View view = LayoutInflater.from ( parent.getContext ( ) ).inflate ( com.ojassoft.astrosage.R.layout.item_suggesion_question_list , parent , false );
        return new MyViewHolder ( view );
    }

    @Override
    public void onBindViewHolder ( @NonNull MyViewHolder holder , int position ) {
        //FontUtils.changeFont(context, holder.tvQuestionName, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
//        Typeface openSansSemiBold = CUtils.getOpenSensSemiBold(context);
        String questionName = questions.get ( position );
        holder.tvQuestionName.setText ( questionName );
        FontUtils.changeFont(context,holder.tvQuestionName , CGlobalVariables.FONTS_POPPINS_LIGHT);
        holder.itemView.setOnClickListener ( v -> {
            ((MiniChatWindow) context).showSuggestions(false);
            ((MiniChatWindow) context).setQuestionFromSuggestions(questionName);
            //((MiniChatWindow) context).identifyLanguage ( questionName, true );
        });

    }

    @Override
    public int getItemCount () {
        return questions.size ( );
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionName;

        public MyViewHolder ( @NonNull View itemView ) {
            super ( itemView );
            tvQuestionName = (TextView) itemView.findViewById ( com.ojassoft.astrosage.R.id.tvQuestionName );
        }

    }
}

