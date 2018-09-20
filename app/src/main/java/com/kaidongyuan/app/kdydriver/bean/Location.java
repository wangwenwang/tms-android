package com.kaidongyuan.app.kdydriver.bean;

public class Location {

	
	public int _id;
	
	public String lat;
	
	public String lng;
	
	public String addDate;
	
	public int status;

	public Location() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Location(String lat, String lng, String addDate, int status) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.addDate = addDate;
		this.status = status;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "lat=" + lat + "  lng=" + lng + "  addDate=" + addDate + "  status=" + status;
	}
	
}
