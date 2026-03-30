package com.ojassoft.astrosage.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.ui.MiniChatWindow;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StaticQuestionAdapter  extends RecyclerView.Adapter<StaticQuestionAdapter.MyViewHolder> {
    List<String> questions;
    Context context;
    private OnItemClickListener onItemClickListener;
//    String url = "https://ai.astrosage.com/images/icons/";

    int[] icons = {
            R.drawable.true_love,
            R.drawable.family,
            R.drawable.career,
            R.drawable.money,
            R.drawable.life_path,
            R.drawable.timer
    };

//    List<String> icons = Arrays.asList(
//            "life_path.png",
//            "money.png",
//            "carrer.png",
//            "family.png",
//            "festival.png",
//            "daily.png",
//            "chat_logo.png",
//            "money.png",
//            "timer.png",
//            "recharge.png",
//            "premium_astrologer.png"
//    );

    public StaticQuestionAdapter ( Context context , ArrayList<String> questionBeanList ) {
        this.context = context;
        this.questions = questionBeanList;

    }

    // Define the OnItemClickListener interface
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    // Setter for the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent , int viewType ) {
        View view = LayoutInflater.from (parent.getContext()).inflate(R.layout.item_static_questions,parent ,false );
        return new MyViewHolder (view);
    }

    @Override
    public void onBindViewHolder ( @NonNull MyViewHolder holder , int position ) {
       try {

           int actualPosition = position % questions.size();
           String questionName = questions.get(actualPosition);
           //String questionName = questions.get ( position );
           holder.tvQuestionName.setText(questionName);
           FontUtils.changeFont(context, holder.tvQuestionName, CGlobalVariables.FONTS_POPPINS_LIGHT);
           setRandomIcon(holder.question_image);
           holder.itemView.setOnClickListener(v -> {
               //((MiniChatWindow) context).setQuestionText ( questionName, "" ) );
               ((MiniChatWindow) context).setQuestionFromSuggestions(questionName);
            /*if (((MiniChatWindow) context).ivStop.getVisibility() == View.GONE || ((MiniChatWindow) context).thre.getVisibility() == View.GONE) {
                ((MiniChatWindow) context).identifyLanguage(questionName, true);
            } else {
                ((MiniChatWindow) context).setQuestionText ( questionName, "" );
            }*/
               if (onItemClickListener != null) {
                   onItemClickListener.onItemClick(position);
               }
           });
           holder.itemView.setOnTouchListener((v, event) -> {
               switch(event.getAction()){
                   case MotionEvent.ACTION_DOWN: ((MiniChatWindow) context).stopAutoScroll();
                       break;
                   case MotionEvent.ACTION_MOVE: ((MiniChatWindow) context).startAutoScroll();

                       break;
               }
               return false;
           });
       }catch (Exception e){
           //
       }
               // ((MiniChatWindow) context).stopAutoScroll();



    }

    @Override
    public int getItemCount () {
        //return questions.size ( );
        // Return a finite dataset size
        return questions.isEmpty() ? 0 : 1000; // Simulate 10 "loops" of the dataset
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionName;
        ImageView question_image;

        public MyViewHolder ( @NonNull View itemView ) {
            super ( itemView );
            tvQuestionName = (TextView) itemView.findViewById ( com.ojassoft.astrosage.R.id.tvQuestionName );
            question_image =  itemView.findViewById ( com.ojassoft.astrosage.R.id.question_image );
        }

    }


    public void setRandomIcon(ImageView imageView) {
        Random random = new Random();
        int randomIndex = random.nextInt(icons.length);
        imageView.setImageResource(icons[randomIndex]);
    }
}
