package com.kaidongyuan.app.kdydriver.bean;

import java.io.Serializable;
import java.util.List;

public class ProvinceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String province_code;

	public String province;

	public List<CityBean> citys;

	public String ProvinceId;

	public String Name;
}
