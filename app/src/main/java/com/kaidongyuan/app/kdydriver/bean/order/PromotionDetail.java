package com.kaidongyuan.app.kdydriver.bean.order;

public class PromotionDetail implements java.io.Serializable {

	public long IDX;
	public long ENT_IDX;
	public long ORDER_IDX;
	public String PRODUCT_TYPE;  //NR：正常品  GF：赠品
	public long PRODUCT_IDX;
	public String PRODUCT_NO;
	public String PRODUCT_NAME;
	public int LINE_NO;

	public int PO_QTY;
	public String PO_UOM;
	public double PO_WEIGHT;
	public double PO_VOLUME;

	public double ORG_PRICE;
	public double ACT_PRICE;

	public String SALE_REMARK;

	public String MJ_REMARK;
	public double MJ_PRICE;

	public String LOTTABLE01;
	public String LOTTABLE02;
	public String LOTTABLE03;
	public String LOTTABLE04;
	public String LOTTABLE05;
	public String LOTTABLE06;
	public String LOTTABLE07;
	public String LOTTABLE08;
	public String LOTTABLE09;
	public String LOTTABLE10;

	public long OPERATOR_IDX;
	public String ADD_DATE;
	public String EDIT_DATE;
	public String PRODUCT_URL;
}
