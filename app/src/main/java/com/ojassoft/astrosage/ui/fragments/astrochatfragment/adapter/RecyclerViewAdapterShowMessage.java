package com.ojassoft.astrosage.ui.fragments.astrochatfragment.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.CircularNetworkImageView;
import com.ojassoft.astrosage.beans.RoundPic;
import com.ojassoft.astrosage.model.GmailAccountInfo;
import com.ojassoft.astrosage.ui.act.ActAstroPaymentOptions;
import com.ojassoft.astrosage.ui.act.EditProfileActivity;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.model.MessageDecode;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.networkdataload.NetworkSendRatingStatus;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UserEmailFetcher;

import java.util.ArrayList;

/**
 * Created by ojas-10 on 8/3/16.
 */
public class RecyclerViewAdapterShowMessage extends RecyclerView.Adapter<RecyclerViewAdapterShowMessage.ViewHolder> {
    // private ArrayList<AstrologerModel> astrologerModelArrayList;
    static ArrayList<MessageDecode> messageDecode;
    ImageLoader mImageLoader;
    public  ViewHolder viewHolder;
    static  Context context;
    private String emailId="";
    private Boolean isFromChatHistory=false;
    private Bitmap bm;
    private SharedPreferences myPrefrence;
    private boolean isPictureSaved = false;


    public RecyclerViewAdapterShowMessage(ArrayList<MessageDecode> messageDecode, Context context, Boolean isFromChatHistory) {
        // this.astrologerModelArrayList = astrologerModelArrayList;
        this.messageDecode = messageDecode;
        this.context = context;
        this.isFromChatHistory=isFromChatHistory;
        mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
        emailId= UserEmailFetcher.getEmail(this.context);
         myPrefrence = context.getSharedPreferences(CGlobalVariables.KEY_USER_PICTURE, Context.MODE_PRIVATE);
        checkForAvailableUserPicture();
    }

    private void checkForAvailableUserPicture() {
        isPictureSaved = myPrefrence.getBoolean(CGlobalVariables.HAS_PICTURE_SAVED,false);
        if(isPictureSaved) {
            String userId;
            GmailAccountInfo gmailAccountInfo;
            gmailAccountInfo = CUtils.getGmailAccountInfo(context);
            userId = gmailAccountInfo.getId();
            String img = myPrefrence.getString(userId, "");
            bm = EditProfileActivity.decodeBase64(img);
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapterShowMessage.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.msg_show_astrochat, null);

        // create ViewHolder

        viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
       /* if(position==messageDecode.size()-2)
        {
            //Log.e("value is",new Gson().toJson(messageDecode.get(position)));
        }*/

        viewHolder.setIsRecyclable(false);

        String dateformatted;

        //Set astrologer image
        if (messageDecode.get(position).getUserType().equalsIgnoreCase("Astrologer") && messageDecode.get(position).getAstrologerImagePath() != null && !messageDecode.get(position).getAstrologerImagePath().trim().isEmpty()) {
            viewHolder.networkImageViewLayout.setImageUrl(CGlobalVariables.ASTROCAMP_BASE_URL.concat(messageDecode.get(position).getAstrologerImagePath()), mImageLoader);
            //Log.e("tag","Image hit" + position);
        } else {
            viewHolder.networkImageViewLayout.setImageDrawable(context.getResources().getDrawable(R.drawable.ganesh));
        }


        //Set Date On Each Message
        if ((CUtils.formatDate(messageDecode.get(position).getDateTimeShow())) != null) {
            dateformatted = CUtils.formatDate(messageDecode.get(position).getDateTimeShow())+" ";
        } else {
            dateformatted = " ";
        }
        if(messageDecode.get(position).getUserType()!=null) {
            if (messageDecode.get(position).getUserType().equals("System") || (messageDecode.get(position).getAstrologerName()!=null && messageDecode.get(position).getAstrologerName().equals(""))) {
                viewHolder.dateUserName.setText(dateformatted + "by " + messageDecode.get(position).getUserType());
            } else if (messageDecode.get(position).getUserType().equals("Astrologer")&&(messageDecode.get(position).getAstrologerName()!=null && !messageDecode.get(position).getAstrologerName().isEmpty())){
                viewHolder.dateUserName.setText(dateformatted + "by " + messageDecode.get(position).getAstrologerName());
            }else {
                viewHolder.dateUserName.setText(dateformatted + "by " + messageDecode.get(position).getUserType());

            }
        }

        if(messageDecode.get(position).getMessageText()!=null) {
            viewHolder.textMessage.setText(messageDecode.get(position).getMessageText());
        } else {
            viewHolder.textMessage.setText("");
        }

        if(messageDecode.get(position).getRating()!=null && !messageDecode.get(position).getRating().isEmpty()) {
            viewHolder.ratingBar.setRating(Integer.parseInt(messageDecode.get(position).getRating()));
        } else {
            viewHolder.ratingBar.setRating(0);
        }

        if (messageDecode.get(position).getShareLinkShow()!=null && getBool(messageDecode.get(position).getShareLinkShow()) && messageDecode.get(position).isRateShow()!=null&& getBool(messageDecode.get(position).isRateShow())) {
            viewHolder.outerShareRateLayout.setVisibility(View.VISIBLE);
            viewHolder.shareAnswer.setVisibility(View.VISIBLE);
            viewHolder.rateAnswerLayout.setVisibility(View.VISIBLE);
        } else if (messageDecode.get(position).getShareLinkShow()!=null && getBool(messageDecode.get(position).getShareLinkShow())) {
            viewHolder.outerShareRateLayout.setVisibility(View.VISIBLE);
            viewHolder.shareAnswer.setVisibility(View.VISIBLE);
            viewHolder.rateAnswerLayout.setVisibility(View.GONE);
        } else if (messageDecode.get(position).isRateShow()!=null&& getBool(messageDecode.get(position).isRateShow())) {
            viewHolder.outerShareRateLayout.setVisibility(View.VISIBLE);
            viewHolder.rateAnswerLayout.setVisibility(View.VISIBLE);
            viewHolder.shareAnswer.setVisibility(View.GONE);
        } else {
            viewHolder.outerShareRateLayout.setVisibility(View.GONE);
            viewHolder.rateAnswerLayout.setVisibility(View.GONE);
            viewHolder.shareAnswer.setVisibility(View.GONE);
        }

        if(isFromChatHistory &&(messageDecode.get(position).getUserType().equalsIgnoreCase("user")||messageDecode.get(position).getUserType().contains("@")||emailId.equalsIgnoreCase(messageDecode.get(position).getUserType().trim())))
        {
            if(messageDecode.get(position).getOrderId()!=null&& !messageDecode.get(position).getOrderId().isEmpty() && messageDecode.get(position).getOrderId().equalsIgnoreCase("0"))
            {
                viewHolder.notPaidLayout.setVisibility(View.VISIBLE);
                messageDecode.get(position).setNotPaidLayoutShow("True");

            }
            else
            {
                viewHolder.notPaidLayout.setVisibility(View.GONE);
                messageDecode.get(position).setNotPaidLayoutShow("False");

            }
        }
        else
        {
            if (messageDecode.get(position).getNotPaidLayoutShow()!=null&&getBool(messageDecode.get(position).getNotPaidLayoutShow()) && (messageDecode.get(position).getUserType().equalsIgnoreCase("user")||messageDecode.get(position).getUserType().contains("@")||emailId.equalsIgnoreCase(messageDecode.get(position).getUserType().trim()))) {
                viewHolder.notPaidLayout.setVisibility(View.VISIBLE);
            } else {
                viewHolder.notPaidLayout.setVisibility(View.GONE);
            }

        }

        if(messageDecode.get(position).getColorOfMessage()!=null&&!messageDecode.get(position).getColorOfMessage().isEmpty()){
            try {
                viewHolder.marginLayout.setBackgroundColor(Color.parseColor(messageDecode.get(position).getColorOfMessage().trim()));
            }catch(Exception e){
                viewHolder.marginLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        }else{
            viewHolder.marginLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
       /* if (((messageDecode.get(position).getNotPaidLayoutShow()!=null&&getBool(messageDecode.get(position).getNotPaidLayoutShow()))||(messageDecode.get(position).getOrderId()!=null&& !messageDecode.get(position).getOrderId().isEmpty() &&messageDecode.get(position).getOrderId().equalsIgnoreCase("0")))&&(((messageDecode.get(position).getUserType().equalsIgnoreCase("user"))||messageDecode.get(position).getUserType().contains("@")||emailId.equalsIgnoreCase(messageDecode.get(position).getUserType().trim()))))  {
            messageDecode.get(position).setNotPaidLayoutShow("True");
        } else {
            messageDecode.get(position).setNotPaidLayoutShow("False");
        }*/



        if (messageDecode.get(position).getUserType().equalsIgnoreCase("user") ||messageDecode.get(position).getUserType().contains("@")||emailId.equalsIgnoreCase(messageDecode.get(position).getUserType().trim())) {
            viewHolder.networkImageViewLayoutRight.setVisibility(View.VISIBLE);
            int paddingLeft = CUtils.getPaddingInPixel(30, context);
            int otherPadding = CUtils.getPaddingInPixel(10, context);
            int framePadding = CUtils.getPaddingInPixel(20, context);
            viewHolder.marginLayout.setPadding(otherPadding, otherPadding, paddingLeft, otherPadding);
            viewHolder.mainMessageLayout.setPadding(framePadding, otherPadding, otherPadding, otherPadding);
            viewHolder.networkImageViewLayout.setVisibility(View.GONE);

            if ((isPictureSaved && CUtils.isUserLogedIn(context))) {
                viewHolder.networkImageViewLayoutRight.setImageBitmap(bm);
            } else {
                viewHolder.networkImageViewLayoutRight.setImageDrawable(context.getResources().getDrawable(ActAstroPaymentOptions.userImageResource));
            }

        } else {
            viewHolder.networkImageViewLayout.setVisibility(View.VISIBLE);
            int paddingRight = CUtils.getPaddingInPixel(30, context);
            int otherPadding = CUtils.getPaddingInPixel(10, context);
            int framePadding = CUtils.getPaddingInPixel(20, context);
            viewHolder.marginLayout.setPadding(paddingRight, otherPadding, otherPadding, otherPadding);
            viewHolder.mainMessageLayout.setPadding(otherPadding, otherPadding, framePadding, otherPadding);
            viewHolder.networkImageViewLayoutRight.setVisibility(View.GONE);
        }
        // //Log.e("!!!!!!!!!!!!!!!!!!!", "" + CGlobalVariable.urlAstrologerImage + messageDecode.get(position).getMessageText());

    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView dateUserName;
        public TextView textMessage;
        public LinearLayout shareAnswer;
        public LinearLayout rateAnswerLayout;
        public CircularNetworkImageView networkImageViewLayout;
        public RoundPic networkImageViewLayoutRight;
        public LinearLayout outerShareRateLayout;
        public LinearLayout marginLayout;
        public LinearLayout notPaidLayout;
        public FrameLayout mainMessageLayout;
        final RatingBar ratingBar;
        ImageView shareAnswerButton;
        MessageDecode message;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            mainMessageLayout = (FrameLayout) itemLayoutView.findViewById(R.id.mainMsgFrame);
            dateUserName = (TextView) itemLayoutView.findViewById(R.id.dateusername);
            textMessage = (TextView) itemLayoutView.findViewById(R.id.textmsg);
            shareAnswer = (LinearLayout) itemLayoutView.findViewById(R.id.sharelayout);
            outerShareRateLayout = (LinearLayout) itemLayoutView.findViewById(R.id.outersharerate);
            rateAnswerLayout = (LinearLayout) itemLayoutView.findViewById(R.id.ratingLayout);
            networkImageViewLayout = (CircularNetworkImageView) itemLayoutView.findViewById(R.id.image_server_left);
            networkImageViewLayoutRight = (RoundPic) itemLayoutView.findViewById(R.id.image_user_right);
            marginLayout = (LinearLayout) itemLayoutView.findViewById(R.id.marginLayout);
            shareAnswerButton = (ImageView) itemLayoutView.findViewById(R.id.share);
            ratingBar = (RatingBar) itemLayoutView.findViewById(R.id.ratingBar);
            networkImageViewLayoutRight.setBorderColor(context.getResources().getColor(R.color.primary_orange));
            networkImageViewLayoutRight.setBorderWidth(2);

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    //messageDecode=CUtils.getDataFromPrefrence(context);
                    if (rateAnswerLayout.getVisibility()==View.VISIBLE) {
                        message = messageDecode.get(getLayoutPosition());
                        if (rating == 5) {
                            SharedPreferences prefs = context.getSharedPreferences("rate_app_new", Context.MODE_PRIVATE);
                            // Main rating Dialog
                            boolean hasRateAlreadyGiven = prefs.getBoolean("HAS_GIVEN_RATE", false);
                            if (hasRateAlreadyGiven) {
                                saveRatingOnPerticulerMessage(getLayoutPosition(), (int) rating);
                                sendCurrentRatingToServer(rating,message);
                            } else {
                                ((ActAstroPaymentOptions) context).displayRateDialog();
                                saveRatingOnPerticulerMessage(getLayoutPosition(), (int) rating);
                                sendCurrentRatingToServer(rating,message);
                            }
                        } else {
                            saveRatingOnPerticulerMessage(getLayoutPosition(), (int) rating);
                            sendCurrentRatingToServer(rating,message);
                        }
                    }
                }
            });

            shareAnswerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    message = messageDecode.get(getLayoutPosition());
                    CUtils.sharePrediction(context,"",message.getMessageText());
                }
            });

            notPaidLayout = (LinearLayout) itemLayoutView.findViewById(R.id.paidbutton);
            notPaidLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActAstroPaymentOptions.isMessageNeedToSend=true;
                    if (CUtils.getBooleanData(context, CGlobalVariables.IS_USER_PROFILE_FILLED, false)) {
                        message = messageDecode.get(getLayoutPosition());
                        ((ActAstroPaymentOptions) context).gotBuyAskQuestionPlan(message, getLayoutPosition());
                    }else{
                        ((ActAstroPaymentOptions) context).openBirthProfileUpdatorDialog(((ActAstroPaymentOptions) context).RESPONSE_CODE_SELECT_PROFILE);
                    }
                }
            });
        }

        private void sendCurrentRatingToServer(float rating, MessageDecode message) {
            if (message.getOrderId()!=null) {
                new NetworkSendRatingStatus(context,rating,message.getOrderId(),message.getChatId()).sendUpdatedRating();
            } else {
                Toast.makeText(context,"Order ID not yet generated",Toast.LENGTH_LONG).show();
            }
        }

        // save/update rating each time when user change rating.
        private void saveRatingOnPerticulerMessage(int position, int rating) {
            messageDecode.get(position).setRating((""+rating).trim());
            ActAstroPaymentOptions.isMessagesModified =true;
        }

    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return messageDecode.size();
    }

    private boolean getBool(String str)
    {
        if(str==null || str.isEmpty())
            return false;
        else if(str.equalsIgnoreCase("True"))
            return true;
        else
            return false;
    }

}