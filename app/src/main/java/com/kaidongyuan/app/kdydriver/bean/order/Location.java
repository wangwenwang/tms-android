package com.kaidongyuan.app.kdydriver.bean.order;
// default package


import java.util.Date;

/**
 *客户地址信息
 */
public class Location implements java.io.Serializable {
	public String id;
	public String userIdx;
	public Double CORDINATEX;
	public Double CORDINATEY;
	public String ADDRESS;
	public Date CREATETIME;

	@Override
	public String toString() {
		return "CORDINATEX:"+CORDINATEX+"\t,CORDINATEY"+CORDINATEY;
	}
}