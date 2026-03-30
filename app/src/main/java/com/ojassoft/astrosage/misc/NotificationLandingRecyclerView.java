package com.ojassoft.astrosage.misc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.CircularNetworkImageView;
import com.ojassoft.astrosage.beans.RoundPic;
import com.ojassoft.astrosage.jinterface.IExtraActionForAskAQuestionChat;
import com.ojassoft.astrosage.model.AstrologerServiceInfo;
import com.ojassoft.astrosage.model.GmailAccountInfo;
import com.ojassoft.astrosage.ui.act.ActAstroPaymentOptions;
import com.ojassoft.astrosage.ui.act.ActNotificationLanding;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.EditProfileActivity;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.model.MessageDecode;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.networkdataload.NetworkSendRatingStatus;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.savedata.SaveDataInternalStorage;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UserEmailFetcher;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ojas-20 on 28/3/17.
 */
public class NotificationLandingRecyclerView extends RecyclerView.Adapter<NotificationLandingRecyclerView.ViewHolder> {
    // private ArrayList<AstrologerModel> astrologerModelArrayList;
    static ArrayList<MessageDecode> messageDecode;
    ImageLoader mImageLoader;
    public NotificationLandingRecyclerView.ViewHolder viewHolder;
    static Context context;
    private String emailId = "";
    private Boolean isFromChatHistory = false;
    private Bitmap bm;
    private SharedPreferences myPrefrence;
    private boolean isPictureSaved = false;
    String registerUserName;
    ActNotificationLanding actNotificationLanding;
    private static IExtraActionForAskAQuestionChat iExtraActionForAskAQuestionChat;


    public NotificationLandingRecyclerView(ArrayList<MessageDecode> messageDecode, Context context, Boolean isFromChatHistory, ActNotificationLanding actNotificationLanding) {
        // this.astrologerModelArrayList = astrologerModelArrayList;
        if (messageDecode != null) {
            this.messageDecode = messageDecode;
            Log.i("GOOGLE_PAY", "messageDecode: " + messageDecode);
        } else {
            this.messageDecode = new ArrayList<>();
        }

        Collections.reverse(this.messageDecode);
        this.context = context;
        iExtraActionForAskAQuestionChat = (IExtraActionForAskAQuestionChat) context;
        this.isFromChatHistory = isFromChatHistory;
        this.actNotificationLanding = actNotificationLanding;
        mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
        emailId = UserEmailFetcher.getEmail(this.context);
        if (emailId == null) {
            emailId = "";
        }
        myPrefrence = context.getSharedPreferences(CGlobalVariables.KEY_USER_PICTURE, Context.MODE_PRIVATE);
        checkForAvailableUserPicture();
        getUserNameToShow();
    }

    private void checkForAvailableUserPicture() {
        isPictureSaved = myPrefrence.getBoolean(CGlobalVariables.HAS_PICTURE_SAVED, false);
        if (isPictureSaved) {
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
    public NotificationLandingRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.msg_show_notificationlanding, parent, false);
       /* View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.msg_show_notificationlanding, false);
*/
        viewHolder = new NotificationLandingRecyclerView.ViewHolder(itemLayoutView);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final NotificationLandingRecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder == null || messageDecode == null) return;

        if (messageDecode.get(position) == null) return;
        viewHolder.setIsRecyclable(false);

        String dateformatted;

        //messageDecode.get(position).setNotPaidLayoutShow("False");
        if (messageDecode.get(position).getUserType() == null) {
            messageDecode.get(position).setUserType("user");
        }
        if (messageDecode.get(position).getOrderId() == null) {
            messageDecode.get(position).setOrderId("");
        }
        //Set astrologer image
        if (messageDecode.get(position).getUserType().equalsIgnoreCase("Astrologer") && messageDecode.get(position).getAstrologerImagePath() != null && !messageDecode.get(position).getAstrologerImagePath().trim().isEmpty()) {
            viewHolder.networkImageViewLayout.setImageUrl(messageDecode.get(position).getAstrologerImagePath(), mImageLoader);
            Log.e("tag", "Image hit" + position);
        } else {
            viewHolder.networkImageViewLayout.setImageDrawable(context.getResources().getDrawable(R.drawable.ganesh));
        }


        //Set Date On Each Message
        if ((CUtils.formatDate(messageDecode.get(position).getDateTimeShow())) != null) {
            dateformatted = CUtils.formatDate(messageDecode.get(position).getDateTimeShow()) + " ";
        } else {
            dateformatted = " ";
        }
        if (messageDecode.get(position).getUserType() != null) {
            if (messageDecode.get(position).getUserType().equals("System") || (messageDecode.get(position).getAstrologerName() != null && messageDecode.get(position).getAstrologerName().equals(""))) {
                if (messageDecode.get(position).getMessageTextTitle() != null && !messageDecode.get(position).getMessageTextTitle().isEmpty())
                    viewHolder.titleMessage.setText(messageDecode.get(position).getMessageTextTitle() + "");
                else
                    viewHolder.titleMessage.setText("");
            } else if (messageDecode.get(position).getUserType().equals("Astrologer") && (messageDecode.get(position).getAstrologerName() != null && !messageDecode.get(position).getAstrologerName().isEmpty())) {
                if (messageDecode.get(position).getMessageTextTitle() != null && !messageDecode.get(position).getMessageTextTitle().isEmpty())
                    viewHolder.titleMessage.setText(messageDecode.get(position).getMessageTextTitle());
                else
                    viewHolder.titleMessage.setText("");
            } else {

                if (registerUserName != null && !registerUserName.isEmpty()) {
                    String title = replaceText(context.getString(R.string.this_question_ask_by), "#", registerUserName);
                    viewHolder.titleMessage.setText(title);
                } else {
                    String title = replaceText(context.getString(R.string.this_question_ask_by), "#", registerUserName);
                    viewHolder.titleMessage.setText(title);
                }
            }
        }

        if (messageDecode.get(position).getMessageText() != null) {
            viewHolder.textMsgTitle.setVisibility(View.GONE);
            viewHolder.textMessage.setText(getFormattedMessageText(messageDecode.get(position).getMessageText()));
            viewHolder.textMsgContent.setVisibility(View.GONE);
        } else {
            viewHolder.textMsgTitle.setVisibility(View.GONE);
            viewHolder.textMessage.setText("");
            viewHolder.textMsgContent.setVisibility(View.GONE);
        }

        if (messageDecode.get(position).getRating() != null && !messageDecode.get(position).getRating().isEmpty()) {
            viewHolder.ratingBar.setRating(Integer.parseInt(messageDecode.get(position).getRating()));
        } else {
            viewHolder.ratingBar.setRating(0);
        }

        if (messageDecode.get(position).getShareLinkShow() != null && getBool(messageDecode.get(position).getShareLinkShow()) && messageDecode.get(position).isRateShow() != null && getBool(messageDecode.get(position).isRateShow())) {
            viewHolder.outerShareRateLayout.setVisibility(View.VISIBLE);
            viewHolder.shareAnswer.setVisibility(View.VISIBLE);
            viewHolder.rateAnswerLayout.setVisibility(View.VISIBLE);
        } else if (messageDecode.get(position).getShareLinkShow() != null && getBool(messageDecode.get(position).getShareLinkShow())) {
            viewHolder.outerShareRateLayout.setVisibility(View.VISIBLE);
            viewHolder.shareAnswer.setVisibility(View.VISIBLE);
            viewHolder.rateAnswerLayout.setVisibility(View.GONE);
        } else if (messageDecode.get(position).isRateShow() != null && getBool(messageDecode.get(position).isRateShow())) {
            viewHolder.outerShareRateLayout.setVisibility(View.VISIBLE);
            viewHolder.rateAnswerLayout.setVisibility(View.VISIBLE);
            viewHolder.shareAnswer.setVisibility(View.GONE);
        } else {
            viewHolder.outerShareRateLayout.setVisibility(View.GONE);
            viewHolder.rateAnswerLayout.setVisibility(View.GONE);
            viewHolder.shareAnswer.setVisibility(View.GONE);
        }


        if (messageDecode.get(position).getUserType().equalsIgnoreCase("user") || messageDecode.get(position).getUserType().contains("@") || emailId.equalsIgnoreCase(messageDecode.get(position).getUserType().trim())) {
            viewHolder.networkImageViewLayoutRight.setVisibility(View.VISIBLE);
            viewHolder.networkImageViewLayout.setVisibility(View.GONE);

            if ((isPictureSaved && CUtils.isUserLogedIn(context))) {
                viewHolder.networkImageViewLayoutRight.setImageBitmap(bm);
            } else {

                if (isFromChatHistory && (messageDecode.get(position).getUserType().equalsIgnoreCase("user") || messageDecode.get(position).getUserType().contains("@") || emailId.equalsIgnoreCase(messageDecode.get(position).getUserType().trim()))) {
                    if (messageDecode.get(position).getOrderId() != null && !messageDecode.get(position).getOrderId().isEmpty() && messageDecode.get(position).getOrderId().equalsIgnoreCase("0")) {
                        viewHolder.networkImageViewLayout.setClickable(true);

                        viewHolder.payNow.setVisibility(View.VISIBLE);
                        viewHolder.orderId.setVisibility(View.GONE);
                        viewHolder.orderidContainerLayout.setVisibility(View.GONE);
                        viewHolder.networkImageViewLayoutRight.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_name));
                        viewHolder.titleMessage.setText(context.getResources().getString(R.string.unpaidquestion));
                        if (messageDecode.get(position).getMessageText() != null) {
                            viewHolder.textMsgTitle.setVisibility(View.VISIBLE);
                            viewHolder.textMsgContent.setVisibility(View.VISIBLE);
                            viewHolder.textMsgTitle.setText(context.getResources().getString(R.string.youaskedthisquestion));
                            viewHolder.textMessage.setText("“" + getFormattedMessageText(messageDecode.get(position).getMessageText()) + "”");
                            viewHolder.textMsgContent.setText(context.getResources().getString(R.string.buthavenotmadeapayment));
                        }
                    } else {
                        //paid question
                        viewHolder.networkImageViewLayout.setClickable(false);
                        viewHolder.payNow.setVisibility(View.GONE);
                        viewHolder.orderId.setVisibility(View.VISIBLE);
                        viewHolder.orderidContainerLayout.setVisibility(View.VISIBLE);
                        viewHolder.networkImageViewLayoutRight.setImageDrawable(context.getResources().getDrawable(ActAstroPaymentOptions.userImageResource));
                        viewHolder.titleMessage.setText(context.getResources().getString(R.string.successfullaskquestion));
                        if (messageDecode.get(position).getMessageText() != null) {
                            viewHolder.textMsgTitle.setVisibility(View.VISIBLE);
                            viewHolder.textMsgContent.setVisibility(View.VISIBLE);
                            viewHolder.textMsgTitle.setText(context.getResources().getString(R.string.wehaverecivedyourquestion));
                            viewHolder.textMessage.setText("“" + getFormattedMessageText(messageDecode.get(position).getMessageText()) + "”");
                            /*String text = context.getResources().getString(R.string.youexpectedtoget24hours);
                            if(text.contains("#")){
                                text = text.replace("#",context.getResources().getString(R.string.expectedHoursToSendReport));
                            }*/
                            viewHolder.textMsgContent.setText(context.getResources().getString(R.string.youexpectedtoget24hours));
                        }
                    }
                } else {
                    if (messageDecode.get(position).getNotPaidLayoutShow() != null && getBool(messageDecode.get(position).getNotPaidLayoutShow()) && (messageDecode.get(position).getUserType().equalsIgnoreCase("user") || messageDecode.get(position).getUserType().contains("@") || emailId.equalsIgnoreCase(messageDecode.get(position).getUserType().trim()))) {
                        viewHolder.networkImageViewLayout.setClickable(true);

                        viewHolder.payNow.setVisibility(View.VISIBLE);
                        viewHolder.orderId.setVisibility(View.GONE);
                        viewHolder.orderidContainerLayout.setVisibility(View.GONE);
                        viewHolder.networkImageViewLayoutRight.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_name));
                        viewHolder.titleMessage.setText(context.getResources().getString(R.string.unpaidquestion));
                        if (messageDecode.get(position).getMessageText() != null) {
                            viewHolder.textMsgTitle.setVisibility(View.VISIBLE);
                            viewHolder.textMsgContent.setVisibility(View.VISIBLE);
                            viewHolder.textMsgTitle.setText(context.getResources().getString(R.string.youaskedthisquestion));
                            viewHolder.textMessage.setText("“" + getFormattedMessageText(messageDecode.get(position).getMessageText()) + "”");
                            viewHolder.textMsgContent.setText(context.getResources().getString(R.string.buthavenotmadeapayment));
                        }
                    } else {
                        viewHolder.networkImageViewLayout.setClickable(false);
                        viewHolder.payNow.setVisibility(View.GONE);
                        viewHolder.orderId.setVisibility(View.VISIBLE);
                        viewHolder.orderidContainerLayout.setVisibility(View.VISIBLE);
                        viewHolder.networkImageViewLayoutRight.setImageDrawable(context.getResources().getDrawable(ActAstroPaymentOptions.userImageResource));
                        viewHolder.titleMessage.setText(context.getResources().getString(R.string.successfullaskquestion));
                        if (messageDecode.get(position).getMessageText() != null) {
                            viewHolder.textMsgTitle.setVisibility(View.VISIBLE);
                            viewHolder.textMsgContent.setVisibility(View.VISIBLE);
                            viewHolder.textMsgTitle.setText(context.getResources().getString(R.string.wehaverecivedyourquestion));
                            viewHolder.textMessage.setText("“" + getFormattedMessageText(messageDecode.get(position).getMessageText()) + "”");
                            /*String text = context.getResources().getString(R.string.youexpectedtoget24hours);
                            if(text.contains("#")){
                                text = text.replace("#",context.getResources().getString(R.string.expectedHoursToSendReport));
                            }*/
                            viewHolder.textMsgContent.setText(context.getResources().getString(R.string.youexpectedtoget24hours));
                        }
                    }
                }

            }

        } else {
            viewHolder.networkImageViewLayout.setVisibility(View.VISIBLE);
            viewHolder.networkImageViewLayoutRight.setVisibility(View.GONE);
        }

        String dateFormatAndAstrologerName = dateformatted;
        if (messageDecode != null && messageDecode.get(position).getAstrologerName() != null && !messageDecode.get(position).getAstrologerName().trim().equals("")) {
            if (((BaseInputActivity) context).LANGUAGE_CODE == CGlobalVariables.HINDI)
                dateFormatAndAstrologerName = dateFormatAndAstrologerName + " - " + messageDecode.get(position).getAstrologerName() + " " + context.getResources().getString(R.string.by_text);
            else
                dateFormatAndAstrologerName = dateFormatAndAstrologerName + " " + context.getResources().getString(R.string.by_text) + " " + messageDecode.get(position).getAstrologerName();
        }
        //String astrologerName =

        viewHolder.dateMessage.setText(dateFormatAndAstrologerName);
        if (messageDecode != null) {
            String orderId = context.getResources().getString(R.string.order_id) + " " + messageDecode.get(position).getOrderId();
            viewHolder.orderId.setText(orderId);
            // Hide progressbar when orderid recieved  
            if (!TextUtils.isEmpty(messageDecode.get(position).getOrderId())) {
                viewHolder.orderIdProgressBar.setVisibility(View.GONE);
            }
        }


     /*   viewHolder.mainMessageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(messageDecode.get(position).getAstrologerServiceInfo() != null) {
                if (messageDecode.get(position).getUserType().equalsIgnoreCase("user") || messageDecode.get(position).getUserType().contains("@") || emailId.equalsIgnoreCase(messageDecode.get(position).getUserType().trim())) {
                    if (isFromChatHistory && (messageDecode.get(position).getUserType().equalsIgnoreCase("user") || messageDecode.get(position).getUserType().contains("@") || emailId.equalsIgnoreCase(messageDecode.get(position).getUserType().trim()))) {
                        if (messageDecode.get(position).getOrderId() != null && !messageDecode.get(position).getOrderId().isEmpty() && messageDecode.get(position).getOrderId().equalsIgnoreCase("0")) {
                            if(messageDecode.get(position).getAstrologerServiceInfo() != null) {
                                actNotificationLanding.selectOptionPayment(((messageDecode.size() - position) - 1), messageDecode.get(position).getMessageText(), messageDecode.get(position).getChatId(), messageDecode.get(position));
                            }else{
                                callHomeInputActToGetTheBirthDetails(messageDecode.get(position),((messageDecode.size() - position) - 1));
                            }
                        } else {

                        }
                    } else {
                        if (messageDecode.get(position).getNotPaidLayoutShow() != null && getBool(messageDecode.get(position).getNotPaidLayoutShow()) && (messageDecode.get(position).getUserType().equalsIgnoreCase("user") || messageDecode.get(position).getUserType().contains("@") || emailId.equalsIgnoreCase(messageDecode.get(position).getUserType().trim()))) {
                            if(messageDecode.get(position).getAstrologerServiceInfo() != null) {
                                actNotificationLanding.selectOptionPayment(((messageDecode.size() - position) - 1), messageDecode.get(position).getMessageText(), messageDecode.get(position).getChatId(), messageDecode.get(position));
                            }else{
                                callHomeInputActToGetTheBirthDetails(messageDecode.get(position),((messageDecode.size() - position) - 1));
                            }
                        } else {

                        }
                    }

                } else {

                }

            }
        });*/
        viewHolder.payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageDecode.get(position).getUserType().equalsIgnoreCase("user") || messageDecode.get(position).getUserType().contains("@") || emailId.equalsIgnoreCase(messageDecode.get(position).getUserType().trim())) {
                    if (isFromChatHistory && (messageDecode.get(position).getUserType().equalsIgnoreCase("user") || messageDecode.get(position).getUserType().contains("@") || emailId.equalsIgnoreCase(messageDecode.get(position).getUserType().trim()))) {
                        if (messageDecode.get(position).getOrderId() != null && !messageDecode.get(position).getOrderId().isEmpty() && messageDecode.get(position).getOrderId().equalsIgnoreCase("0")) {
                            if (messageDecode.get(position).getAstrologerServiceInfo() != null) {
                                actNotificationLanding.selectOptionPayment(((messageDecode.size() - position) - 1), messageDecode.get(position).getMessageText(), messageDecode.get(position).getChatId(), messageDecode.get(position));
                            } else {
                                callHomeInputActToGetTheBirthDetails(messageDecode.get(position), ((messageDecode.size() - position) - 1));
                            }
                        } else {

                        }
                    } else {
                        if (messageDecode.get(position).getNotPaidLayoutShow() != null && getBool(messageDecode.get(position).getNotPaidLayoutShow()) && (messageDecode.get(position).getUserType().equalsIgnoreCase("user") || messageDecode.get(position).getUserType().contains("@") || emailId.equalsIgnoreCase(messageDecode.get(position).getUserType().trim()))) {
                            AstrologerServiceInfo astrologerServiceInfo = messageDecode.get(position).getAstrologerServiceInfo();
                            if (astrologerServiceInfo != null && astrologerServiceInfo.getCountry() != null) {
                                actNotificationLanding.selectOptionPayment(((messageDecode.size() - position) - 1), messageDecode.get(position).getMessageText(), messageDecode.get(position).getChatId(), messageDecode.get(position));
                            } else {
                                callHomeInputActToGetTheBirthDetails(messageDecode.get(position), ((messageDecode.size() - position) - 1));
                            }
                        } else {

                        }
                    }

                } else {

                }
            }
        });
    }

    /**
     * @param messageDecode
     * @author Amit Rautela
     * this method is used to call HomeInput Act to get Birth Details
     */
    private void callHomeInputActToGetTheBirthDetails(MessageDecode messageDecode, int layPos) {
        try {
            actNotificationLanding.callToAskQuestion(messageDecode, layPos);
        } catch (Exception ex) {
            //
        }
    }

    public Object getUserNameToShow() {
        String fullJsonDataObj = CUtils.getStringData(context, CGlobalVariables.USERPROFILEASTROCHAT, "");
        try {
            JSONObject jsonObj = new JSONObject(fullJsonDataObj);
            registerUserName = jsonObj.getString("regName");
        } catch (Exception e) {

        }
        return registerUserName;
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleMessage;
        public TextView textMessage, textMsgTitle, textMsgContent;
        public TextView dateMessage;
        private TextView orderId;
        public TextView payNow;

        private ProgressBar orderIdProgressBar;
        public LinearLayout orderidContainerLayout;
        public LinearLayout shareAnswer;
        public LinearLayout rateAnswerLayout;
        public CircularNetworkImageView networkImageViewLayout;
        public RoundPic networkImageViewLayoutRight;
        public LinearLayout outerShareRateLayout;
        public LinearLayout marginLayout;
        public LinearLayout notPaidLayout;
        //public FrameLayout mainMessageLayout;
        CardView cardView;
        final RatingBar ratingBar;
        ImageView shareAnswerButton;
        MessageDecode message;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            Typeface robotMediumTypeface = ((BaseInputActivity) context).robotMediumTypeface;
            Typeface robotRegularTypeface = ((BaseInputActivity) context).robotRegularTypeface;

            //mainMessageLayout = (FrameLayout) itemLayoutView.findViewById(R.id.mainMsgFrame);
            cardView = (CardView) itemLayoutView.findViewById(R.id.card_view);
            titleMessage = (TextView) itemLayoutView.findViewById(R.id.title_template);
            titleMessage.setTypeface(robotMediumTypeface);
            dateMessage = (TextView) itemLayoutView.findViewById(R.id.dateusername);
            dateMessage.setTypeface(robotRegularTypeface);
            orderId = (TextView) itemLayoutView.findViewById(R.id.orderId);
            orderId.setTypeface(robotMediumTypeface);
            orderidContainerLayout = (LinearLayout) itemLayoutView.findViewById(R.id.orderid_container);
            orderIdProgressBar = (ProgressBar) itemLayoutView.findViewById(R.id.orderid_progressbar);

            textMessage = (TextView) itemLayoutView.findViewById(R.id.textmsg);
            textMessage.setTypeface(robotRegularTypeface);
            textMsgTitle = (TextView) itemLayoutView.findViewById(R.id.textMsgTitle);
            textMsgTitle.setTypeface(robotMediumTypeface);
            textMsgContent = (TextView) itemLayoutView.findViewById(R.id.textMsgContent);
            textMsgContent.setTypeface(robotMediumTypeface);
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
            payNow = (TextView) itemLayoutView.findViewById(R.id.paynow);


            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    //messageDecode=CUtils.getDataFromPrefrence(context);
                    if (rateAnswerLayout.getVisibility() == View.VISIBLE) {
                        message = messageDecode.get(getLayoutPosition());
                        if (rating == 5) {
                            SharedPreferences prefs = context.getSharedPreferences("rate_app_new", Context.MODE_PRIVATE);
                            // Main rating Dialog
                            boolean hasRateAlreadyGiven = prefs.getBoolean("HAS_GIVEN_RATE", false);
                            if (hasRateAlreadyGiven) {
                                saveRatingOnPerticulerMessage(getLayoutPosition(), (int) rating);
                                sendCurrentRatingToServer(rating, message);
                            } else {
                                ((ActNotificationLanding) context).displayRateDialog();
                                saveRatingOnPerticulerMessage(getLayoutPosition(), (int) rating);
                                sendCurrentRatingToServer(rating, message);
                            }
                        } else {
                            saveRatingOnPerticulerMessage(getLayoutPosition(), (int) rating);
                            sendCurrentRatingToServer(rating, message);
                        }
                    }
                }
            });

            shareAnswerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    message = messageDecode.get(getLayoutPosition());
                    String textToShare = message.getMessageTextTitle() + "\n\n" + message.getMessageText() + "\n\nDownload " + CUtils.getMyApplicationName(context)
                            + " App: \n https://go.astrosage.com/akwa";
                    CUtils.shareAnswerMessage(getFormattedMessageText(textToShare), context);
                }
            });

            notPaidLayout = (LinearLayout) itemLayoutView.findViewById(R.id.paidbutton);
           /* notPaidLayout.setOnClickListener(new View.OnClickListener() {
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
            });*/

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = getLayoutPosition();
                    // String message1 = messageDecode.get(pos).getMessageText();
                    String chatId = messageDecode.get(pos).getChatId();
                    // callExtraActionMethod(message1,chatId);
                    iExtraActionForAskAQuestionChat.showDialog(pos, chatId);
                    return false;
                }
            });
        }

        private void sendCurrentRatingToServer(float rating, MessageDecode message) {
            if (message.getOrderId() != null && message.getChatId() != null) {
                new NetworkSendRatingStatus(context, rating, message.getOrderId(), message.getChatId()).sendUpdatedRating();
            } else {
                Toast.makeText(context, "Order ID not yet generated", Toast.LENGTH_LONG).show();
            }
        }

        // save/update rating each time when user change rating.
        private void saveRatingOnPerticulerMessage(int position, int rating) {
            messageDecode.get(position).setRating(("" + rating).trim());
            ActNotificationLanding.isMessagesModified = false;
            ArrayList<MessageDecode> chatMessageArrayList = new ArrayList<MessageDecode>(messageDecode);
            //chatMessageArrayList=messageDecode;

            Collections.reverse(chatMessageArrayList);
            Intent intent = new Intent(context, SaveDataInternalStorage.class);
            intent.putParcelableArrayListExtra(CGlobalVariables.CHATWITHASTROLOGER, chatMessageArrayList);
            intent.putExtra(CGlobalVariables.ISINSERT, false);
            (context).startService(intent);
        }

    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (messageDecode == null) return 0;
        return messageDecode.size();
    }

    private boolean getBool(String str) {
        if (str == null || str.isEmpty())
            return false;
        else if (str.equalsIgnoreCase("True"))
            return true;
        else
            return false;
    }

    private static String getFormattedMessageText(String text) {
        String actualString = "";
        if (text.contains("(Girl")) {
            String[] arr = text.split("\\(Girl");
            actualString = arr[0];
        } else if (text.contains("^^")) {
            String[] arr = text.split("\\^\\^");
            actualString = arr[0];
        } else if (text.contains("##")) {
            String[] arr = text.split("##");
            actualString = arr[0];
        } else {
            actualString = text;
        }
        return actualString.trim();
    }

    /**
     * This method is used to replace key words
     *
     * @param data
     * @param replaceKeyWord
     * @param newWord
     * @return
     */
    private String replaceText(String data, String replaceKeyWord, String newWord) {
        String word = "";
        if (data.contains(replaceKeyWord)) {
            if (newWord != null && newWord.length() > 0) {
                word = data.replace(replaceKeyWord, newWord);
            } else {
                word = data.replace(replaceKeyWord, "");
            }
        } else {
            word = data;
        }
        return word;
    }

}