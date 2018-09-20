package com.kaidongyuan.app.kdydriver.bean.order;


public class UploadProduct implements java.io.Serializable {
	String product_no;
	String IDX;
	int count;

	public UploadProduct(){

	}

	public UploadProduct(String product_no, String IDX, int count){
		this.product_no = product_no;
		this.count = count;
		this.IDX = IDX;
	}

	public String getIDX() {
		return IDX;
	}

	public void setIDX(String IDX) {
		this.IDX = IDX;
	}

	public String getProduct_no() {
		return product_no;
	}

	public void setProduct_no(String product_no) {
		this.product_no = product_no;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
