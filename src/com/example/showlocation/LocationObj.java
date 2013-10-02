package com.example.showlocation;
import java.io.Serializable;;

public class LocationObj implements Serializable{

	private int id;
	private String sender;
	private String Latitude;
	private String Longitude;
	private String Date;
	
	public LocationObj()
	{
		sender=null;
		Latitude=null;
		Longitude=null;
		Date=null;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocationObj(String sender,String lat,String lon,String date)
	{
		this.Date=date;
		this.sender=sender;
		this.Latitude=lat;
		this.Longitude=lon;
	}
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	
	
}
