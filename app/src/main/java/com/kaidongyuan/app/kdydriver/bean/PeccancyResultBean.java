package com.kaidongyuan.app.kdydriver.bean;

import java.io.Serializable;
import java.util.List;

public class PeccancyResultBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public String error_code;

	public String reason;

	public List<ProvinceBean> info;

	public int result;

	public String province;

	public String city;

	public String hphm;

	public String hpzl;

	public List<PeccancyBean> lists;

}
