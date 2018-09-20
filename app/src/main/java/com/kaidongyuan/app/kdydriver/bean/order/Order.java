package com.kaidongyuan.app.kdydriver.bean.order;

import java.util.List;

public class Order implements java.io.Serializable {
    private String TMS_DATE_LOAD;//
    private String TMS_DATE_ISSUE;//
    private String TMS_SHIPMENT_NO;//
    private String TMS_FLEET_NAME;//
    private String ORD_IDX;
    private String IDX;//
    private String ORD_NO;//K56订单编号
    private String ORD_NO_CLIENT;//客户订单编号
    private String ORD_TO_NAME;//
    private String PARTY_TYPE;
    private String ORD_TO_CNAME;
    private String ORD_TO_ADDRESS;//
    private String ORD_QTY;
    private String ORD_WEIGHT;
    private String ORD_VOLUME;
    private String ORD_ISSUE_QTY;//
    private String ORD_ISSUE_WEIGHT;//
    private String ORD_ISSUE_VOLUME;//
    private String ORD_WORKFLOW;//
    private String AUDIT_FLAG;//是否结费标志Y/N " "
    private String ERROR_FLAG;//是否计费错误标志Y/N " "
    private String SPLIT_TYPE;//
    private String OTS_RETURN_DATE;//
    private String OTS_DELIVERY_ACTUAL;//
    private String OMS_PARENT_NO;
    private String ORD_STATE;//
    private String ORD_DATE_ADD;
    private String ADD_CODE;
    private String ORD_REQUEST_ISSUE;
    private String ORD_FROM_NAME;//
    private String FROM_COORDINATE;//起始点坐标
    private String TO_COORDINATE;//到达点坐标
    private String ORD_REMARK_CLIENT;
    private String TMS_DRIVER_NAME;//司机姓名
    private String TMS_PLATE_NUMBER;//车牌号
    private String TMS_DRIVER_TEL;//司机电话
    private String PAYMENT_TYPE;
    private double ORG_PRICE;
    private double ACT_PRICE;
    private double MJ_PRICE;
    private String ORD_REMARK_CONSIGNEE;
    private String MJ_REMARK;
    private String DRIVER_PAY;//
    private ShipMent Shipment;
    private List<OrderDetails> OrderDetails;
    private List<StateTack> StateTack;
    public List<com.kaidongyuan.app.kdydriver.bean.order.StateTack> getStateTack() {
        return StateTack;
    }

    public ShipMent getShipment() {
        return Shipment;
    }

    public void setShipment(ShipMent shipment) {
        Shipment = shipment;
    }

    public String getDRIVER_PAY() {
        return DRIVER_PAY;
    }

    public void setDRIVER_PAY(String DRIVER_PAY) {
        this.DRIVER_PAY = DRIVER_PAY;
    }

    public String getTMS_DATE_LOAD() {
        return TMS_DATE_LOAD;
    }

    public void setTMS_DATE_LOAD(String TMS_DATE_LOAD) {
        this.TMS_DATE_LOAD = TMS_DATE_LOAD;
    }

    public String getTMS_DATE_ISSUE() {
        return TMS_DATE_ISSUE;
    }

    public void setTMS_DATE_ISSUE(String TMS_DATE_ISSUE) {
        this.TMS_DATE_ISSUE = TMS_DATE_ISSUE;
    }

    public String getTMS_SHIPMENT_NO() {
        return TMS_SHIPMENT_NO;
    }

    public void setTMS_SHIPMENT_NO(String TMS_SHIPMENT_NO) {
        this.TMS_SHIPMENT_NO = TMS_SHIPMENT_NO;
    }

    public String getAUDIT_FLAG() {
        return AUDIT_FLAG;
    }

    public void setAUDIT_FLAG(String AUDIT_FLAG) {
        this.AUDIT_FLAG = AUDIT_FLAG;
    }

    public String getERROR_FLAG() {
        return ERROR_FLAG;
    }

    public void setERROR_FLAG(String ERROR_FLAG) {
        this.ERROR_FLAG = ERROR_FLAG;
    }

    public String getTMS_FLEET_NAME() {
        return TMS_FLEET_NAME;
    }

    public void setTMS_FLEET_NAME(String TMS_FLEET_NAME) {
        this.TMS_FLEET_NAME = TMS_FLEET_NAME;
    }

    public String getORD_IDX() {
        return ORD_IDX;
    }

    public void setORD_IDX(String ORD_IDX) {
        this.ORD_IDX = ORD_IDX;
    }

    public String getORD_REMARK_CONSIGNEE() {
        return ORD_REMARK_CONSIGNEE;
    }

    public void setORD_REMARK_CONSIGNEE(String ORD_REMARK_CONSIGNEE) {
        this.ORD_REMARK_CONSIGNEE = ORD_REMARK_CONSIGNEE;
    }

    public String getMJ_REMARK() {
        return MJ_REMARK;
    }

    public void setMJ_REMARK(String MJ_REMARK) {
        this.MJ_REMARK = MJ_REMARK;
    }

    public double getORG_PRICE() {
        return ORG_PRICE;
    }

    public void setORG_PRICE(double ORG_PRICE) {
        this.ORG_PRICE = ORG_PRICE;
    }

    public double getACT_PRICE() {
        return ACT_PRICE;
    }

    public void setACT_PRICE(double ACT_PRICE) {
        this.ACT_PRICE = ACT_PRICE;
    }

    public double getMJ_PRICE() {
        return MJ_PRICE;
    }

    public void setMJ_PRICE(double MJ_PRICE) {
        this.MJ_PRICE = MJ_PRICE;
    }

    public void setStateTack(List<com.kaidongyuan.app.kdydriver.bean.order.StateTack> stateTack) {
        StateTack = stateTack;
    }

    public String getPAYMENT_TYPE() {
        return PAYMENT_TYPE;
    }

    public void setPAYMENT_TYPE(String PAYMENT_TYPE) {
        this.PAYMENT_TYPE = PAYMENT_TYPE;
    }

    public String getFROM_COORDINATE() {
        return FROM_COORDINATE;
    }

    public void setFROM_COORDINATE(String FROM_COORDINATE) {
        this.FROM_COORDINATE = FROM_COORDINATE;
    }

    public String getTO_COORDINATE() {
        return TO_COORDINATE;
    }

    public void setTO_COORDINATE(String TO_COORDINATE) {
        this.TO_COORDINATE = TO_COORDINATE;
    }

    public String getORD_REMARK_CLIENT() {
        return ORD_REMARK_CLIENT;
    }

    public void setORD_REMARK_CLIENT(String ORD_REMARK_CLIENT) {
        this.ORD_REMARK_CLIENT = ORD_REMARK_CLIENT;
    }

    public String getORD_ISSUE_QTY() {
        return ORD_ISSUE_QTY;
    }

    public void setORD_ISSUE_QTY(String ORD_ISSUE_QTY) {
        this.ORD_ISSUE_QTY = ORD_ISSUE_QTY;
    }

    public String getORD_ISSUE_WEIGHT() {
        return ORD_ISSUE_WEIGHT;
    }

    public void setORD_ISSUE_WEIGHT(String ORD_ISSUE_WEIGHT) {
        this.ORD_ISSUE_WEIGHT = ORD_ISSUE_WEIGHT;
    }

    public String getORD_ISSUE_VOLUME() {
        return ORD_ISSUE_VOLUME;
    }

    public void setORD_ISSUE_VOLUME(String ORD_ISSUE_VOLUME) {
        this.ORD_ISSUE_VOLUME = ORD_ISSUE_VOLUME;
    }

    public String getORD_FROM_NAME() {
        return ORD_FROM_NAME;
    }

    public void setORD_FROM_NAME(String ORD_FROM_NAME) {
        this.ORD_FROM_NAME = ORD_FROM_NAME;
    }

    public String getORD_REQUEST_ISSUE() {
        return ORD_REQUEST_ISSUE;
    }

    public void setORD_REQUEST_ISSUE(String ORD_REQUEST_ISSUE) {
        this.ORD_REQUEST_ISSUE = ORD_REQUEST_ISSUE;
    }

    public String getORD_DATE_ADD() {
        return ORD_DATE_ADD;
    }

    public void setORD_DATE_ADD(String ORD_DATE_ADD) {
        this.ORD_DATE_ADD = ORD_DATE_ADD;
    }

    public String getADD_CODE() {
        return ADD_CODE;
    }

    public void setADD_CODE(String ADD_CODE) {
        this.ADD_CODE = ADD_CODE;
    }

    public List<OrderDetails> getOrderDetails() {
        return OrderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        OrderDetails = orderDetails;
    }

    public String getIDX() {
        return IDX;
    }

    public void setIDX(String IDX) {
        this.IDX = IDX;
    }

    public String getORD_NO() {
        return ORD_NO;
    }

    public void setORD_NO(String ORD_NO) {
        this.ORD_NO = ORD_NO;
    }

    public String getORD_NO_CLIENT() {
        return ORD_NO_CLIENT;
    }

    public void setORD_NO_CLIENT(String ORD_NO_CLIENT) {
        this.ORD_NO_CLIENT = ORD_NO_CLIENT;
    }

    public String getORD_TO_NAME() {
        return ORD_TO_NAME;
    }

    public void setORD_TO_NAME(String ORD_TO_NAME) {
        this.ORD_TO_NAME = ORD_TO_NAME;
    }

    public String getPARTY_TYPE() {
        return PARTY_TYPE;
    }

    public void setPARTY_TYPE(String PARTY_TYPE) {
        this.PARTY_TYPE = PARTY_TYPE;
    }

    public String getORD_TO_CNAME() {
        return ORD_TO_CNAME;
    }

    public void setORD_TO_CNAME(String ORD_TO_CNAME) {
        this.ORD_TO_CNAME = ORD_TO_CNAME;
    }

    public String getORD_TO_ADDRESS() {
        return ORD_TO_ADDRESS;
    }

    public void setORD_TO_ADDRESS(String ORD_TO_ADDRESS) {
        this.ORD_TO_ADDRESS = ORD_TO_ADDRESS;
    }

    public String getORD_QTY() {
        return ORD_QTY;
    }

    public void setORD_QTY(String ORD_QTY) {
        this.ORD_QTY = ORD_QTY;
    }

    public String getORD_WEIGHT() {
        return ORD_WEIGHT;
    }

    public void setORD_WEIGHT(String ORD_WEIGHT) {
        this.ORD_WEIGHT = ORD_WEIGHT;
    }

    public String getORD_VOLUME() {
        return ORD_VOLUME;
    }

    public void setORD_VOLUME(String ORD_VOLUME) {
        this.ORD_VOLUME = ORD_VOLUME;
    }

    public String getORD_WORKFLOW() {
        return ORD_WORKFLOW;
    }

    public void setORD_WORKFLOW(String ORD_WORKFLOW) {
        this.ORD_WORKFLOW = ORD_WORKFLOW;
    }

    public String getSPLIT_TYPE() {
        return SPLIT_TYPE;
    }

    public void setSPLIT_TYPE(String SPLIT_TYPE) {
        this.SPLIT_TYPE = SPLIT_TYPE;
    }

    public String getOTS_RETURN_DATE() {
        return OTS_RETURN_DATE;
    }

    public void setOTS_RETURN_DATE(String OTS_RETURN_DATE) {
        this.OTS_RETURN_DATE = OTS_RETURN_DATE;
    }

    public String getOTS_DELIVERY_ACTUAL() {
        return OTS_DELIVERY_ACTUAL;
    }

    public void setOTS_DELIVERY_ACTUAL(String OTS_DELIVERY_ACTUAL) {
        this.OTS_DELIVERY_ACTUAL = OTS_DELIVERY_ACTUAL;
    }

    public String getOMS_PARENT_NO() {
        return OMS_PARENT_NO;
    }

    public void setOMS_PARENT_NO(String OMS_PARENT_NO) {
        this.OMS_PARENT_NO = OMS_PARENT_NO;
    }

    public String getORD_STATE() {
        return ORD_STATE;
    }

    public void setORD_STATE(String ORD_STATE) {
        this.ORD_STATE = ORD_STATE;
    }

    public String getTMS_DRIVER_NAME() {
        return TMS_DRIVER_NAME;
    }

    public void setTMS_DRIVER_NAME(String TMS_DRIVER_NAME) {
        this.TMS_DRIVER_NAME = TMS_DRIVER_NAME;
    }

    public String getTMS_PLATE_NUMBER() {
        return TMS_PLATE_NUMBER;
    }

    public void setTMS_PLATE_NUMBER(String TMS_PLATE_NUMBER) {
        this.TMS_PLATE_NUMBER = TMS_PLATE_NUMBER;
    }

    public String getTMS_DRIVER_TEL() {
        return TMS_DRIVER_TEL;
    }

    public void setTMS_DRIVER_TEL(String TMS_DRIVER_TEL) {
        this.TMS_DRIVER_TEL = TMS_DRIVER_TEL;
    }

    @Override
    public String toString() {
        return "Order{" +
                "TMS_DATE_LOAD='" + TMS_DATE_LOAD + '\'' +
                ", TMS_DATE_ISSUE='" + TMS_DATE_ISSUE + '\'' +
                ", TMS_SHIPMENT_NO='" + TMS_SHIPMENT_NO + '\'' +
                ", TMS_FLEET_NAME='" + TMS_FLEET_NAME + '\'' +
                ", ORD_IDX='" + ORD_IDX + '\'' +
                ", IDX='" + IDX + '\'' +
                ", ORD_NO='" + ORD_NO + '\'' +
                ", ORD_NO_CLIENT='" + ORD_NO_CLIENT + '\'' +
                ", ORD_TO_NAME='" + ORD_TO_NAME + '\'' +
                ", PARTY_TYPE='" + PARTY_TYPE + '\'' +
                ", ORD_TO_CNAME='" + ORD_TO_CNAME + '\'' +
                ", ORD_TO_ADDRESS='" + ORD_TO_ADDRESS + '\'' +
                ", ORD_QTY='" + ORD_QTY + '\'' +
                ", ORD_WEIGHT='" + ORD_WEIGHT + '\'' +
                ", ORD_VOLUME='" + ORD_VOLUME + '\'' +
                ", ORD_ISSUE_QTY='" + ORD_ISSUE_QTY + '\'' +
                ", ORD_ISSUE_WEIGHT='" + ORD_ISSUE_WEIGHT + '\'' +
                ", ORD_ISSUE_VOLUME='" + ORD_ISSUE_VOLUME + '\'' +
                ", ORD_WORKFLOW='" + ORD_WORKFLOW + '\'' +
                ", AUDIT_FLAG='" + AUDIT_FLAG + '\'' +
                ", ERROR_FLAG='" + ERROR_FLAG + '\'' +
                ", SPLIT_TYPE='" + SPLIT_TYPE + '\'' +
                ", OTS_RETURN_DATE='" + OTS_RETURN_DATE + '\'' +
                ", OTS_DELIVERY_ACTUAL='" + OTS_DELIVERY_ACTUAL + '\'' +
                ", OMS_PARENT_NO='" + OMS_PARENT_NO + '\'' +
                ", ORD_STATE='" + ORD_STATE + '\'' +
                ", ORD_DATE_ADD='" + ORD_DATE_ADD + '\'' +
                ", ADD_CODE='" + ADD_CODE + '\'' +
                ", ORD_REQUEST_ISSUE='" + ORD_REQUEST_ISSUE + '\'' +
                ", ORD_FROM_NAME='" + ORD_FROM_NAME + '\'' +
                ", FROM_COORDINATE='" + FROM_COORDINATE + '\'' +
                ", TO_COORDINATE='" + TO_COORDINATE + '\'' +
                ", ORD_REMARK_CLIENT='" + ORD_REMARK_CLIENT + '\'' +
                ", TMS_DRIVER_NAME='" + TMS_DRIVER_NAME + '\'' +
                ", TMS_PLATE_NUMBER='" + TMS_PLATE_NUMBER + '\'' +
                ", TMS_DRIVER_TEL='" + TMS_DRIVER_TEL + '\'' +
                ", PAYMENT_TYPE='" + PAYMENT_TYPE + '\'' +
                ", ORG_PRICE=" + ORG_PRICE +
                ", ACT_PRICE=" + ACT_PRICE +
                ", MJ_PRICE=" + MJ_PRICE +
                ", ORD_REMARK_CONSIGNEE='" + ORD_REMARK_CONSIGNEE + '\'' +
                ", MJ_REMARK='" + MJ_REMARK + '\'' +
                ", DRIVER_PAY='" + DRIVER_PAY + '\'' +
                ", Shipment=" + Shipment +
                ", OrderDetails=" + OrderDetails +
                ", StateTack=" + StateTack +
                '}';
    }

    public static  class ShipMent implements java.io.Serializable{
        private String AUDIT_FLAG;//是否计费,Y/N
        private String ERROR_FLAG;//是否有错误提示,Y/N
        private String ERROR_DESC;//错误提示描述

        public String getAUDIT_FLAG() {
            return AUDIT_FLAG;
        }

        public void setAUDIT_FLAG(String AUDIT_FLAG) {
            this.AUDIT_FLAG = AUDIT_FLAG;
        }

        public String getERROR_FLAG() {
            return ERROR_FLAG;
        }

        public void setERROR_FLAG(String ERROR_FLAG) {
            this.ERROR_FLAG = ERROR_FLAG;
        }

        public String getERROR_DESC() {
            return ERROR_DESC;
        }

        public void setERROR_DESC(String ERROR_DESC) {
            this.ERROR_DESC = ERROR_DESC;
        }
    }


    public static   class OrderDetails implements java.io.Serializable{
        private String PRODUCT_NO;//
        private String PRODUCT_NAME;//
        private String ORDER_UOM;//
        private String ISSUE_QTY;//
        private String ISSUE_WEIGHT;//
        private String ISSUE_VOLUME;//
        private String PRODUCT_URL;//""
        private String PRODUCT_TYPE;//""

        public String getPRODUCT_TYPE() {
            return PRODUCT_TYPE;
        }

        public void setPRODUCT_TYPE(String PRODUCT_TYPE) {
            this.PRODUCT_TYPE = PRODUCT_TYPE;
        }

        public String getPRODUCT_URL() {
            return PRODUCT_URL;
        }

        public void setPRODUCT_URL(String PRODUCT_URL) {
            this.PRODUCT_URL = PRODUCT_URL;
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

        public String getORDER_UOM() {
            return ORDER_UOM;
        }

        public void setORDER_UOM(String ORDER_UOM) {
            this.ORDER_UOM = ORDER_UOM;
        }

        public String getISSUE_QTY() {
            return ISSUE_QTY;
        }

        public void setISSUE_QTY(String ISSUE_QTY) {
            this.ISSUE_QTY = ISSUE_QTY;
        }

        public String getISSUE_WEIGHT() {
            return ISSUE_WEIGHT;
        }

        public void setISSUE_WEIGHT(String ISSUE_WEIGHT) {
            this.ISSUE_WEIGHT = ISSUE_WEIGHT;
        }

        public String getISSUE_VOLUME() {
            return ISSUE_VOLUME;
        }

        public void setISSUE_VOLUME(String ISSUE_VOLUME) {
            this.ISSUE_VOLUME = ISSUE_VOLUME;
        }

        @Override
        public String toString() {
            return "OrderDetails{" +
                    "PRODUCT_NO='" + PRODUCT_NO + '\'' +
                    ", PRODUCT_NAME='" + PRODUCT_NAME + '\'' +
                    ", ORDER_UOM='" + ORDER_UOM + '\'' +
                    ", ISSUE_QTY='" + ISSUE_QTY + '\'' +
                    ", ISSUE_WEIGHT='" + ISSUE_WEIGHT + '\'' +
                    ", ISSUE_VOLUME='" + ISSUE_VOLUME + '\'' +
                    ", PRODUCT_URL='" + PRODUCT_URL + '\'' +
                    ", PRODUCT_TYPE='" + PRODUCT_TYPE + '\'' +
                    '}';
        }
    }
}
