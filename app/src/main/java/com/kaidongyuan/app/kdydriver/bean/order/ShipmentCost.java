package com.kaidongyuan.app.kdydriver.bean.order;

import java.util.ArrayList;
import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/3/25.
 */
public class ShipmentCost implements java.io.Serializable  {
    private ShipmentCostDetail Shipment;//装运费用情况
    private ArrayList<OrderCost> Order;//订单费用明细列表
    private ArrayList<OtherCost>  Other;//其它费用明细列表

    public ShipmentCostDetail getShipment() {
        return Shipment;
    }

    public void setShipment(ShipmentCostDetail shipment) {
        Shipment = shipment;
    }

    public ArrayList<OrderCost> getOrder() {
        return Order;
    }

    public void setOrder(ArrayList<OrderCost> order) {
        Order = order;
    }

    public ArrayList<OtherCost> getOther() {
        return Other;
    }

    public void setOther(ArrayList<OtherCost> other) {
        Other = other;
    }

    public static class ShipmentCostDetail{
         private String SHIPMENT_NO;//装运编号
         private String ADJUST_FEES;//		调整金额
         private String ADJUST_CLASS;//		调整类型
         private String  DATE_ISSUE;//		出库时间
         private String AUDIT_REMARK;//		审核备注
         private  String DRIVER_NAME;//
         private String DRIVER_TEL;//
         private String TMS_FLEET_NAME;//
         private  String PLATE_NUMBER;//
         private String  DATE_ADD;//		装运时间
         private String  TOTAL_QTY;//		总量
         private String  TOTAL_WEIGHT;//		重量
         private String TOTAL_VOLUME;//		体积
         private String AUDIT_FLAG;//		是否计费
         private String ERROR_FLAG;//		是否错误
         private String ERROR_DESC;//		提示
         private String CHARGE_AMOUNT;//		计费量
         private String TRANSPORT_FEES;//		运输费
         private String DROPPOINT_FEES;//		分点费
         private String FUEL_SURCHARGE;//		燃油附加费费
         private String SITE_SURCHARGE;//		收货人附加费
         private String RETURN_FEES;//		退货费
         private String DELIVER_FEES;//		提送货费
         private String PRESS_NIGHT;//		压夜费
         private  String  LOAD_FEES;//		装卸费
         private  String OTHER_FEES;//		其他费
         private  String  AMOUNT_PRICE;//		计费单价
         private  String  FEESCOUNT;//		总费用

        public String getSHIPMENT_NO() {
            return SHIPMENT_NO;
        }

        public void setSHIPMENT_NO(String SHIPMENT_NO) {
            this.SHIPMENT_NO = SHIPMENT_NO;
        }

        public String getADJUST_FEES() {
            return ADJUST_FEES;
        }

        public void setADJUST_FEES(String ADJUST_FEES) {
            this.ADJUST_FEES = ADJUST_FEES;
        }

        public String getADJUST_CLASS() {
            return ADJUST_CLASS;
        }

        public void setADJUST_CLASS(String ADJUST_CLASS) {
            this.ADJUST_CLASS = ADJUST_CLASS;
        }

        public String getDATE_ISSUE() {
            return DATE_ISSUE;
        }

        public void setDATE_ISSUE(String DATE_ISSUE) {
            this.DATE_ISSUE = DATE_ISSUE;
        }

        public String getAUDIT_REMARK() {
            return AUDIT_REMARK;
        }

        public void setAUDIT_REMARK(String AUDIT_REMARK) {
            this.AUDIT_REMARK = AUDIT_REMARK;
        }

        public String getDRIVER_NAME() {
            return DRIVER_NAME;
        }

        public void setDRIVER_NAME(String DRIVER_NAME) {
            this.DRIVER_NAME = DRIVER_NAME;
        }

        public String getDRIVER_TEL() {
            return DRIVER_TEL;
        }

        public void setDRIVER_TEL(String DRIVER_TEL) {
            this.DRIVER_TEL = DRIVER_TEL;
        }

        public String getTMS_FLEET_NAME() {
            return TMS_FLEET_NAME;
        }

        public void setTMS_FLEET_NAME(String TMS_FLEET_NAME) {
            this.TMS_FLEET_NAME = TMS_FLEET_NAME;
        }

        public String getPLATE_NUMBER() {
            return PLATE_NUMBER;
        }

        public void setPLATE_NUMBER(String PLATE_NUMBER) {
            this.PLATE_NUMBER = PLATE_NUMBER;
        }

        public String getDATE_ADD() {
            return DATE_ADD;
        }

        public void setDATE_ADD(String DATE_ADD) {
            this.DATE_ADD = DATE_ADD;
        }

        public String getTOTAL_QTY() {
            return TOTAL_QTY;
        }

        public void setTOTAL_QTY(String TOTAL_QTY) {
            this.TOTAL_QTY = TOTAL_QTY;
        }

        public String getTOTAL_WEIGHT() {
            return TOTAL_WEIGHT;
        }

        public void setTOTAL_WEIGHT(String TOTAL_WEIGHT) {
            this.TOTAL_WEIGHT = TOTAL_WEIGHT;
        }

        public String getTOTAL_VOLUME() {
            return TOTAL_VOLUME;
        }

        public void setTOTAL_VOLUME(String TOTAL_VOLUME) {
            this.TOTAL_VOLUME = TOTAL_VOLUME;
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

        public String getERROR_DESC() {
            return ERROR_DESC;
        }

        public void setERROR_DESC(String ERROR_DESC) {
            this.ERROR_DESC = ERROR_DESC;
        }

        public String getCHARGE_AMOUNT() {
            return CHARGE_AMOUNT;
        }

        public void setCHARGE_AMOUNT(String CHARGE_AMOUNT) {
            this.CHARGE_AMOUNT = CHARGE_AMOUNT;
        }

        public String getTRANSPORT_FEES() {
            return TRANSPORT_FEES;
        }

        public void setTRANSPORT_FEES(String TRANSPORT_FEES) {
            this.TRANSPORT_FEES = TRANSPORT_FEES;
        }

        public String getDROPPOINT_FEES() {
            return DROPPOINT_FEES;
        }

        public void setDROPPOINT_FEES(String DROPPOINT_FEES) {
            this.DROPPOINT_FEES = DROPPOINT_FEES;
        }

        public String getFUEL_SURCHARGE() {
            return FUEL_SURCHARGE;
        }

        public void setFUEL_SURCHARGE(String FUEL_SURCHARGE) {
            this.FUEL_SURCHARGE = FUEL_SURCHARGE;
        }

        public String getSITE_SURCHARGE() {
            return SITE_SURCHARGE;
        }

        public void setSITE_SURCHARGE(String SITE_SURCHARGE) {
            this.SITE_SURCHARGE = SITE_SURCHARGE;
        }

        public String getRETURN_FEES() {
            return RETURN_FEES;
        }

        public void setRETURN_FEES(String RETURN_FEES) {
            this.RETURN_FEES = RETURN_FEES;
        }

        public String getDELIVER_FEES() {
            return DELIVER_FEES;
        }

        public void setDELIVER_FEES(String DELIVER_FEES) {
            this.DELIVER_FEES = DELIVER_FEES;
        }

        public String getPRESS_NIGHT() {
            return PRESS_NIGHT;
        }

        public void setPRESS_NIGHT(String PRESS_NIGHT) {
            this.PRESS_NIGHT = PRESS_NIGHT;
        }

        public String getLOAD_FEES() {
            return LOAD_FEES;
        }

        public void setLOAD_FEES(String LOAD_FEES) {
            this.LOAD_FEES = LOAD_FEES;
        }

        public String getOTHER_FEES() {
            return OTHER_FEES;
        }

        public void setOTHER_FEES(String OTHER_FEES) {
            this.OTHER_FEES = OTHER_FEES;
        }

        public String getAMOUNT_PRICE() {
            return AMOUNT_PRICE;
        }

        public void setAMOUNT_PRICE(String AMOUNT_PRICE) {
            this.AMOUNT_PRICE = AMOUNT_PRICE;
        }

        public String getFEESCOUNT() {
            return FEESCOUNT;
        }

        public void setFEESCOUNT(String FEESCOUNT) {
            this.FEESCOUNT = FEESCOUNT;
        }
    }

    public static class OrderCost{
         private String ORD_NO;//	订单号
         private String  ORD_NO_CLIENT;//		客户订单号
//        private String  TMS_FLEET_NAME;//		承运商名
         private String   ORD_STATE;//		订单状态
         private String   ORD_WORKFLOW;//		订单流程
//        private String   TMS_DRIVER_NAME;//		司机
//        private String   TMS_DRIVER_TEL;//	司机号码
//        private String   TMS_PLATE_NUMBER;//		车牌号
         private String   UPODATE03;//		交付状态
         private String   ORD_FROM_NAMES	;//	起运名称
         private String   ORD_TO_NAME;//		到达名称
         private String   CHARGE_AMOUNT;//		计费量
         private String   TRANSPORT_FEES;//		运输费
         private String   DROPPOINT_FEES;//		分点费
         private String  LOAD_FEES;//		装卸费
         private String   SITE_SURCHARGE;//		收货人附加费
         private String   FUEL_SURCHARGE;//		燃油附加费
         private String   RETURN_FEES;//		退货费
         private String   DELIVER_FEES;//		提送货费
         private String   PRESS_NIGHT;//		压夜费
         private String   OTHER_FEES;//		其他费
         private String   AMOUNT_PRICE;//		计费单价
         private String   FEESCOUNT;//		总费用
         private String   ORD_QTY;//		总量
         private String    ORD_WEIGHT;//		重量
         private String   ORD_VOLUME;//		体积

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

//        public String getTMS_FLEET_NAME() {
//            return TMS_FLEET_NAME;
//        }
//
//        public void setTMS_FLEET_NAME(String TMS_FLEET_NAME) {
//            this.TMS_FLEET_NAME = TMS_FLEET_NAME;
//        }

        public String getORD_STATE() {
            return ORD_STATE;
        }

        public void setORD_STATE(String ORD_STATE) {
            this.ORD_STATE = ORD_STATE;
        }

        public String getORD_WORKFLOW() {
            return ORD_WORKFLOW;
        }

        public void setORD_WORKFLOW(String ORD_WORKFLOW) {
            this.ORD_WORKFLOW = ORD_WORKFLOW;
        }

//        public String getTMS_DRIVER_NAME() {
//            return TMS_DRIVER_NAME;
//        }
//
//        public void setTMS_DRIVER_NAME(String TMS_DRIVER_NAME) {
//            this.TMS_DRIVER_NAME = TMS_DRIVER_NAME;
//        }
//
//        public String getTMS_DRIVER_TEL() {
//            return TMS_DRIVER_TEL;
//        }
//
//        public void setTMS_DRIVER_TEL(String TMS_DRIVER_TEL) {
//            this.TMS_DRIVER_TEL = TMS_DRIVER_TEL;
//        }
//
//        public String getTMS_PLATE_NUMBER() {
//            return TMS_PLATE_NUMBER;
//        }
//
//        public void setTMS_PLATE_NUMBER(String TMS_PLATE_NUMBER) {
//            this.TMS_PLATE_NUMBER = TMS_PLATE_NUMBER;
//        }


        public String getUPODATE03() {
            return UPODATE03;
        }

        public void setUPODATE03(String UPODATE03) {
            this.UPODATE03 = UPODATE03;
        }

        public String getORD_FROM_NAMES() {
            return ORD_FROM_NAMES;
        }

        public void setORD_FROM_NAMES(String ORD_FROM_NAMES) {
            this.ORD_FROM_NAMES = ORD_FROM_NAMES;
        }

        public String getORD_TO_NAME() {
            return ORD_TO_NAME;
        }

        public void setORD_TO_NAME(String ORD_TO_NAME) {
            this.ORD_TO_NAME = ORD_TO_NAME;
        }

        public String getCHARGE_AMOUNT() {
            return CHARGE_AMOUNT;
        }

        public void setCHARGE_AMOUNT(String CHARGE_AMOUNT) {
            this.CHARGE_AMOUNT = CHARGE_AMOUNT;
        }

        public String getTRANSPORT_FEES() {
            return TRANSPORT_FEES;
        }

        public void setTRANSPORT_FEES(String TRANSPORT_FEES) {
            this.TRANSPORT_FEES = TRANSPORT_FEES;
        }

        public String getDROPPOINT_FEES() {
            return DROPPOINT_FEES;
        }

        public void setDROPPOINT_FEES(String DROPPOINT_FEES) {
            this.DROPPOINT_FEES = DROPPOINT_FEES;
        }

        public String getLOAD_FEES() {
            return LOAD_FEES;
        }

        public void setLOAD_FEES(String LOAD_FEES) {
            this.LOAD_FEES = LOAD_FEES;
        }

        public String getSITE_SURCHARGE() {
            return SITE_SURCHARGE;
        }

        public void setSITE_SURCHARGE(String SITE_SURCHARGE) {
            this.SITE_SURCHARGE = SITE_SURCHARGE;
        }

        public String getFUEL_SURCHARGE() {
            return FUEL_SURCHARGE;
        }

        public void setFUEL_SURCHARGE(String FUEL_SURCHARGE) {
            this.FUEL_SURCHARGE = FUEL_SURCHARGE;
        }

        public String getRETURN_FEES() {
            return RETURN_FEES;
        }

        public void setRETURN_FEES(String RETURN_FEES) {
            this.RETURN_FEES = RETURN_FEES;
        }

        public String getDELIVER_FEES() {
            return DELIVER_FEES;
        }

        public void setDELIVER_FEES(String DELIVER_FEES) {
            this.DELIVER_FEES = DELIVER_FEES;
        }

        public String getPRESS_NIGHT() {
            return PRESS_NIGHT;
        }

        public void setPRESS_NIGHT(String PRESS_NIGHT) {
            this.PRESS_NIGHT = PRESS_NIGHT;
        }

        public String getOTHER_FEES() {
            return OTHER_FEES;
        }

        public void setOTHER_FEES(String OTHER_FEES) {
            this.OTHER_FEES = OTHER_FEES;
        }

        public String getAMOUNT_PRICE() {
            return AMOUNT_PRICE;
        }

        public void setAMOUNT_PRICE(String AMOUNT_PRICE) {
            this.AMOUNT_PRICE = AMOUNT_PRICE;
        }

        public String getFEESCOUNT() {
            return FEESCOUNT;
        }

        public void setFEESCOUNT(String FEESCOUNT) {
            this.FEESCOUNT = FEESCOUNT;
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
    }

    public static class OtherCost{
       private String ORD_NO;//		订单号
       private String FEE_TYPE;//		费用类型
       private String OTHER_FEES;//		其他费
       private String FEE_DESC;//		费用说明

        public String getORD_NO() {
            return ORD_NO;
        }

        public void setORD_NO(String ORD_NO) {
            this.ORD_NO = ORD_NO;
        }

        public String getFEE_TYPE() {
            return FEE_TYPE;
        }

        public void setFEE_TYPE(String FEE_TYPE) {
            this.FEE_TYPE = FEE_TYPE;
        }

        public String getOTHER_FEES() {
            return OTHER_FEES;
        }

        public void setOTHER_FEES(String OTHER_FEES) {
            this.OTHER_FEES = OTHER_FEES;
        }

        public String getFEE_DESC() {
            return FEE_DESC;
        }

        public void setFEE_DESC(String FEE_DESC) {
            this.FEE_DESC = FEE_DESC;
        }
    }


}
