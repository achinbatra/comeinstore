package com.bvhack.comeinstore;

public class CampaignBean {

  private String _id;
  private String address;
  private boolean isTeaser;
  private String retailer;
  private String name;
  private int discountPct;
  private double latitude;
  private double longitude;

  public CampaignBean(String _id, String address, boolean isTeaser, String retailer, int discountPct, String name, double latitude, double longitude) {
    this._id = _id;
    this.address = address;
    this.isTeaser = isTeaser;
    this.retailer = retailer;
    this.discountPct = discountPct;
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
  }

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

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public int getDiscountPct() {
    return discountPct;
  }

  public void setDiscountPct(int discountPct) {
    this.discountPct = discountPct;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
}
