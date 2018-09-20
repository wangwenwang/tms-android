package com.kaidongyuan.app.kdydriver.bean.order;
// default package


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 订单产品信息
 */
public class Product implements Parcelable {
    private long IDX;
    private String BUSINESS_IDX;
    private String PRODUCT_NO;
    private String PRODUCT_NAME;
    private String PRODUCT_DESC;
    private String PRODUCT_BARCODE;
    private String PRODUCT_TYPE;
    private String PRODUCT_CLASS;
    private double PRODUCT_PRICE;
    private String PRODUCT_URL;
    private List<ProductPolicy> PRODUCT_POLICY;

    public Product(){

    }

    public List<ProductPolicy> getPRODUCT_POLICY() {
        return PRODUCT_POLICY;
    }

    public void setPRODUCT_POLICY(List<ProductPolicy> PRODUCT_POLICY) {
        this.PRODUCT_POLICY = PRODUCT_POLICY;
    }

    public String getBUSINESS_IDX() {
        return BUSINESS_IDX;
    }

    public void setBUSINESS_IDX(String BUSINESS_IDX) {
        this.BUSINESS_IDX = BUSINESS_IDX;
    }

    public String getPRODUCT_NO() {
        return PRODUCT_NO;
    }

    public void setPRODUCT_NO(String PRODUCT_NO) {
        this.PRODUCT_NO = PRODUCT_NO;
    }

    public String getPRODUCT_NAME() {
        return PRODUCT_NAME;
    }

    public void setPRODUCT_NAME(String PRODUCT_NAME) {
        this.PRODUCT_NAME = PRODUCT_NAME;
    }

    public String getPRODUCT_DESC() {
        return PRODUCT_DESC;
    }

    public void setPRODUCT_DESC(String PRODUCT_DESC) {
        this.PRODUCT_DESC = PRODUCT_DESC;
    }

    public String getPRODUCT_BARCODE() {
        return PRODUCT_BARCODE;
    }

    public void setPRODUCT_BARCODE(String PRODUCT_BARCODE) {
        this.PRODUCT_BARCODE = PRODUCT_BARCODE;
    }

    public long getIDX() {
        return IDX;
    }

    public void setIDX(long IDX) {
        this.IDX = IDX;
    }

    public String getPRODUCT_TYPE() {
        return PRODUCT_TYPE;
    }

    public void setPRODUCT_TYPE(String PRODUCT_TYPE) {
        this.PRODUCT_TYPE = PRODUCT_TYPE;
    }

    public String getPRODUCT_CLASS() {
        return PRODUCT_CLASS;
    }

    public void setPRODUCT_CLASS(String PRODUCT_CLASS) {
        this.PRODUCT_CLASS = PRODUCT_CLASS;
    }

    public double getPRODUCT_PRICE() {
        return PRODUCT_PRICE;
    }

    public void setPRODUCT_PRICE(double PRODUCT_PRICE) {
        this.PRODUCT_PRICE = PRODUCT_PRICE;
    }

    public String getPRODUCT_URL() {
        return PRODUCT_URL;
    }

    public void setPRODUCT_URL(String PRODUCT_URL) {
        this.PRODUCT_URL = PRODUCT_URL;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(IDX);
        dest.writeString(BUSINESS_IDX);
        dest.writeString(PRODUCT_NO);
        dest.writeString(PRODUCT_NAME);
        dest.writeString(PRODUCT_DESC);
        dest.writeString(PRODUCT_BARCODE);
        dest.writeString(PRODUCT_TYPE);
        dest.writeString(PRODUCT_CLASS);
        dest.writeDouble(PRODUCT_PRICE);
        dest.writeString(PRODUCT_URL);
        dest.writeList(PRODUCT_POLICY);
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }

        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }
    };

    public Product(Parcel in) {
        IDX = in.readLong();
        BUSINESS_IDX = in.readString();
        PRODUCT_NO = in.readString();
        PRODUCT_NAME = in.readString();
        PRODUCT_DESC = in.readString();
        PRODUCT_BARCODE = in.readString();
        PRODUCT_TYPE = in.readString();
        PRODUCT_CLASS = in.readString();
        PRODUCT_PRICE = in.readDouble();
        PRODUCT_URL = in.readString();
        PRODUCT_POLICY = in.readArrayList(ProductPolicy.class.getClassLoader());
//        in.readList(PRODUCT_POLICY, ProductPolicy.class.getClassLoader());
        //
    }

}