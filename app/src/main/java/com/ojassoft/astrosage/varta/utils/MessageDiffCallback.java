package com.ojassoft.astrosage.varta.utils;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;

import java.util.List;

public class MessageDiffCallback extends DiffUtil.Callback {
    List<ChatMessage> mOldMessageList, mNewMessageList;
    Context context;

    public MessageDiffCallback(List<ChatMessage> oldMessageList, List<ChatMessage> newMessageList, Context context) {
        mOldMessageList = oldMessageList;
        mNewMessageList = newMessageList;
        this.context = context;
    }


    @Override
    public int getOldListSize() {
        return mOldMessageList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewMessageList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        ChatMessage oldMessage = mOldMessageList.get(oldItemPosition);
        ChatMessage newMessage = mNewMessageList.get(newItemPosition);
        return oldMessage.chatId() == newMessage.chatId()
                && oldMessage.getAuthor().equals(newMessage.getAuthor());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        ChatMessage oldMessage = mOldMessageList.get(oldItemPosition);
        ChatMessage newMessage = mNewMessageList.get(newItemPosition);

        return oldMessage.getAuthor().equals(newMessage.getAuthor())
                && oldMessage.getMessageBody(context).equals(newMessage.getMessageBody(context))
                && oldMessage.isDelayed() == newMessage.isDelayed()
                && oldMessage.isSeen() == newMessage.isSeen();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
