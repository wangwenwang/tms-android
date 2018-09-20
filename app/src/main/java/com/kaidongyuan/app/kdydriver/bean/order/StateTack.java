package com.kaidongyuan.app.kdydriver.bean.order;
// default package


/**
 * 订单结点
 */
public class StateTack implements java.io.Serializable {
	private String STATE_TIME;
	private String ORDER_STATE;

	public String getSTATE_TIME() {
		return STATE_TIME;
	}

	public void setSTATE_TIME(String STATE_TIME) {
		this.STATE_TIME = STATE_TIME;
	}

	public String getORDER_STATE() {
		return ORDER_STATE;
	}

	public void setORDER_STATE(String ORDER_STATE) {
		this.ORDER_STATE = ORDER_STATE;
	}
}