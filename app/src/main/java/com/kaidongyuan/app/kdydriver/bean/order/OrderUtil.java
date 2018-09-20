package com.kaidongyuan.app.kdydriver.bean.order;

import com.kaidongyuan.app.kdydriver.constants.Constants;

/**
 * Created by Administrator on 2016/1/7.
 */
public class OrderUtil {

    /**
     *
     * @param product 需要转换的产品实体类
     * @param PRODUCT_TYPE 产品类型默认NR
     * @param LINE_NO 行号，标记先后顺序
     * @param PO_QTY 数量
     * @param OPERATOR_IDX 操作者IDX，也就是登录用户的ID
     * @return
     */
    public static PromotionDetail getPromotionDetailByProduct(Product product, String PRODUCT_TYPE, int LINE_NO, int PO_QTY, long OPERATOR_IDX, double ACT_PRICE){
        if (product == null)return null;
        PromotionDetail p = new PromotionDetail();
        p.ENT_IDX = Constants.ENT_IDX;
        p.PRODUCT_TYPE = PRODUCT_TYPE;
        p.PRODUCT_IDX = product.getIDX();
        p.PRODUCT_NO = product.getPRODUCT_NO();
        p.PRODUCT_NAME = product.getPRODUCT_NAME();
        p.PRODUCT_NAME = product.getPRODUCT_NAME();
        p.PRODUCT_URL = product.getPRODUCT_URL();
        p.LINE_NO = LINE_NO;
        p.PO_QTY = PO_QTY;
        p.ORG_PRICE = product.getPRODUCT_PRICE();
        p.OPERATOR_IDX = OPERATOR_IDX;
        p.ACT_PRICE = ACT_PRICE;
        return p;
    }


    /**
     * 获取付款方式
     * @param key
     * @return
     */
    public static String getPaymentType(String key){
        switch (key){
            case "FPAD":
                return "预付";

            case "FDAP":
                return  "到付";

            case "MP":
                return  "月结";
            default:return "未知";
        }
    }

    // 在详情页不需要 \t\t
    public static String getPromotionRemark(String org, boolean isDetail){
        String[] remarks = org.split("\\+\\|\\+");
        String result = "";
        for (int i = 0; i < remarks.length; i ++){
            result += remarks[i];
            if (i != remarks.length - 1 && !remarks[i].equals("")){
                result += "\n";
                if (!isDetail)result+="\t\t";
            }
        }
//        return org.replaceAll("\\+\\|\\+", "\n");
        return result;
    }
}
