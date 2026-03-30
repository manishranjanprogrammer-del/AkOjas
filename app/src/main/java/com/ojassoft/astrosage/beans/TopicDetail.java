package com.ojassoft.astrosage.beans;

/**
 * Created by ojas on ६/३/१८.
 */

public class TopicDetail {
    String topicName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopicDetail that = (TopicDetail) o;

        if (!topicName.equals(that.topicName)) return false;
        return topicId.equals(that.topicId);
    }

    @Override
    public int hashCode() {
        int result = topicName.hashCode();
        result = 31 * result + topicId.hashCode();
        return result;
    }

    String topicId;
    boolean isSubscribed;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

}
