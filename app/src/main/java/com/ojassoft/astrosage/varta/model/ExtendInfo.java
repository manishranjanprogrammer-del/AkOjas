package com.ojassoft.astrosage.varta.model;

public class ExtendInfo{
private String serviceprice;
private String minimumrecharge;
private String chatduration;
private String remainingwalbal;
private String currentserviceprice;

//// Empty Constructor
//public ExtendInfo() {
//}
//
//// Full Constructor
//public ExtendInfo(String serviceprice,
//                         String minimumrecharge,
//                         String chatduration,
//                         String remainingwalbal,
//                         String currentserviceprice) {
//    this.serviceprice = serviceprice;
//    this.minimumrecharge = minimumrecharge;
//    this.chatduration = chatduration;
//    this.remainingwalbal = remainingwalbal;
//    this.currentserviceprice = currentserviceprice;
//}

// Getters and Setters

public String getServiceprice() {
    return serviceprice;
}

public void setServiceprice(String serviceprice) {
    this.serviceprice = serviceprice;
}

public String getMinimumrecharge() {
    return minimumrecharge;
}

public void setMinimumrecharge(String minimumrecharge) {
    this.minimumrecharge = minimumrecharge;
}

public String getChatduration() {
    return chatduration;
}

public void setChatduration(String chatduration) {
    this.chatduration = chatduration;
}

public String getRemainingwalbal() {
    return remainingwalbal;
}

public void setRemainingwalbal(String remainingwalbal) {
    this.remainingwalbal = remainingwalbal;
}

public String getCurrentserviceprice() {
    return currentserviceprice;
}

public void setCurrentserviceprice(String currentserviceprice) {
    this.currentserviceprice = currentserviceprice;
}
}