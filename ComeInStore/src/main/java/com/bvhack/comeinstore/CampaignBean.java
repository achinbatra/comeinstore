package com.bvhack.comeinstore;

public class CampaignBean {
  private String campaignId;
  private String retailer;

  public String getRetailer() {
    return retailer;
  }

  public void setRetailer(String retailer) {
    this.retailer = retailer;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public boolean isTeaser() {
    return isTeaser;
  }

  public void setTeaser(boolean isTeaser) {
    this.isTeaser = isTeaser;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  private String address;
  private boolean isTeaser;
  private String url;


  public String getCampaignId() {
    return campaignId;
  }

  public void setCampaignId(String campaignId) {
    this.campaignId = campaignId;
  }
}
