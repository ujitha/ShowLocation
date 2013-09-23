package com.example.showlocation;

public class Contact {

	private int id;
	private String PhoneNumber;
	private String Name;

	public Contact() {

	}

	public Contact(int id, String phoneNum, String name) {
		this.id = id;
		this.PhoneNumber = phoneNum;
		this.Name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

}
