package com.smartfarm.net.bean;



public class Report extends Entity {

	private static final long serialVersionUID = 4532215347368253395L;
	
	public final static String REPORT_ID = "id";
    public final static String REPORT_LINK = "link";
    public final static String REPORT_REASON = "reason";
    public final static String REPORT_OTHER_REASON = "otherreason";
    
    private int reportId;
    private String linkAddress;
    private String reason;
    private String otherReason;

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Report [reportId=" + reportId + ", linkAddress=" + linkAddress
                + ", reason=" + reason + ", otherReason=" + otherReason + "]";
    }
}