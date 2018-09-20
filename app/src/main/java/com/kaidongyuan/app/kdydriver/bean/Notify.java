package com.kaidongyuan.app.kdydriver.bean;

public class Notify implements java.io.Serializable {
    private String IID;//订单消息号
    private String ITitle;//订单号
    private String url;
    private String IImage;//icon url字段
    private String IAddTime;//配载时间
    private String IFrom;//起运地址
    private String ITo;//目的地址
    private String IContent;//订单详情
    private String DriverName;//司机名

    public Notify() {
    }

    public String getIID() {
        return IID;
    }

    public void setIID(String IID) {
        this.IID = IID;
    }

    public String getIAddTime() {
        return IAddTime;
    }

    public void setIAddTime(String IAddTime) {
        this.IAddTime = IAddTime;
    }

    public String getIFrom() {
        return IFrom;
    }

    public void setIFrom(String IFrom) {
        this.IFrom = IFrom;
    }

    public String getIContent() {
        return IContent;
    }

    public void setIContent(String IContent) {
        this.IContent = IContent;
    }


    public String getITitle() {
        return ITitle;
    }

    public void setITitle(String ITitle) {
        this.ITitle = ITitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIImage() {
        return IImage;
    }

    public void setIImage(String IImage) {
        this.IImage = IImage;
    }

    public String getITo() {
        return ITo;
    }

    public void setITo(String ITo) {
        this.ITo = ITo;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }
}
