package com.ojassoft.astrosage.varta;

import java.io.Serializable;

public class DataModel implements Serializable {

    String AstrologerAccepted="", CreatedAt="", EndChat="", UserAccepted="", endChat="";

    public String getAstrologerAccepted() {
        return AstrologerAccepted;
    }

    public void setAstrologerAccepted(String astrologerAccepted) {
        AstrologerAccepted = astrologerAccepted;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getEndChat() {
        return EndChat;
    }

    public void setEndChat(String endChat) {
        EndChat = endChat;
    }

    public String getUserAccepted() {
        return UserAccepted;
    }

    public void setUserAccepted(String userAccepted) {
        UserAccepted = userAccepted;
    }
}
