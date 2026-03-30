package com.ojassoft.astrosage.model;

public class PdfFileData {
    private String kundliFilePath;
    private String kundliName;
    private String address;
    private String dateOfBirth; // Store timestamp
    private String timeOfBirth; // Store timestamp
    private String gender; // Store timestamp
    private long getLocaleChartId; // Store timestamp
    private boolean isFromMatchMaking=false; // Store timestamp
    private int planId; // Store timestamp
    private String reportType;

    public PdfFileData(String filePath, String customName, String address, String dateOfBirth, String timeOfBirth, String gender, long getLocaleChartId, boolean isFromMatchMaking, int planId, String reportType) {
        this.kundliFilePath = filePath;
        this.kundliName = customName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.timeOfBirth = timeOfBirth;
        this.gender = gender;
        this.getLocaleChartId = getLocaleChartId;
        this.isFromMatchMaking = isFromMatchMaking;
        this.planId = planId;
        this.reportType = reportType;

    }

    public String getFilePath() { return kundliFilePath; }
    public String getKundliName() { return kundliName; }
    public String getAddress() { return address; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getTimeOfBirth() { return timeOfBirth; }
    public String getGender() { return gender; }
    public long getGetLocaleChartId() { return getLocaleChartId; }
    public boolean getIsFromMatchMaking() { return isFromMatchMaking; }
    public int getPlanId() { return planId; }
    public String getReportType() { return reportType; }
}
