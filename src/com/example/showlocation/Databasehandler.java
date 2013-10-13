package com.example.showlocation;
//Author Ujitha Iroshan
//This handles the all Database CRUDE operations in Friend and Location tables

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Databasehandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "friendsManager";

	// Contacts table name
	private static final String TABLE_CONTACTS = "friends";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "Name";
	private static final String KEY_PH_NO = "phoneNumber";

	private static final String TABLE_LOCATIONS = "locations";

	private static final String ID = "id";
	private static final String SENDER = "sender";
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";
	private static final String DATE = "date";

	public Databasehandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_FRIENDS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_PH_NO + " TEXT" + ")";
		db.execSQL(CREATE_FRIENDS_TABLE);

		String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATIONS + "("
				+ ID + " INTEGER PRIMARY KEY," + SENDER + " TEXT," + LATITUDE
				+ " TEXT," + LONGITUDE + " TEXT," + DATE + " TEXT" + ")";
		
	
		db.execSQL(CREATE_LOCATION_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

		// Drop older friends table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		// Drop older locatons table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
		// Create tables again
		onCreate(db);
	}

	public void addContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, contact.getName()); // Contact Name
		values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone

		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
	}

	public void addLocation(LocationObj loc) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SENDER, loc.getSender());
		values.put(LATITUDE, loc.getLatitude());
		values.put(LONGITUDE, loc.getLongitude());
		values.put(DATE, loc.getDate());
		
		db.insert(TABLE_LOCATIONS, null, values);
		db.close();
	}

	public Contact getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
				KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		// return contact
		return contact;
	}

	public LocationObj getLocation(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_LOCATIONS, new String[] { ID, SENDER,
				LATITUDE, LONGITUDE, DATE }, ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		LocationObj location = new LocationObj(cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4));

		return location;
	}

	public List<Contact> getAllContacts() {
		List<Contact> contactList = new ArrayList<Contact>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Contact contact = new Contact();
				contact.setId(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(1));
				contact.setPhoneNumber(cursor.getString(2));
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;

	}
	
	public List<LocationObj> getAllLocations(){
		
		List<LocationObj> locationList=new ArrayList<LocationObj>();
		String selectAllQuery="SELECT * FROM "+ TABLE_LOCATIONS;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectAllQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				LocationObj location=new LocationObj();
				location.setId(Integer.parseInt(cursor.getString(0)));
				location.setSender(cursor.getString(1));
				location.setLatitude(cursor.getString(2));
				location.setLongitude(cursor.getString(3));
				location.setDate(cursor.getString(4));
				
				locationList.add(location);
			} while (cursor.moveToNext());
		}
		
		return locationList;
		
	}

	public int updateContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, contact.getName());
		values.put(KEY_PH_NO, contact.getPhoneNumber());

		// updating row
		return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getId()) });

	}

	public void deleteContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getId()) });
		db.close();
	}
	
	public void deleteLocation(LocationObj locaton)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_LOCATIONS, ID +" =?",new String[] {String.valueOf(locaton.getId())});
		db.close();
	}

	public boolean checkContact(String num) {
		SQLiteDatabase db = this.getReadableDatabase();

		String Query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE "
				+ KEY_PH_NO + "='" + num + "'";
		Cursor cursor = db.rawQuery(Query, null);

		if (cursor.getCount() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Contact getContact(String num) {
		SQLiteDatabase db = this.getReadableDatabase();

		String Query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE "
				+ KEY_PH_NO + "='" + num + "'";
		Cursor cursor = db.rawQuery(Query, null);

		if (cursor != null)
			cursor.moveToFirst();

		Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
				cursor.getString(2), cursor.getString(1));
		// return contact
		return contact;

	}

}
