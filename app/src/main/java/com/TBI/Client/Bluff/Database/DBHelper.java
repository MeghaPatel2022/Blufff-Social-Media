package com.TBI.Client.Bluff.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "BluffMaster.db";
    public static final String O_DATA = "O_Rating";
    public static final String O_FRIENDS = "O_Post";
    public static final String O_POSTS = "O_Stocks";
    public static final String O_OTHER = "O_Stories";
    public static final String P_ID = "P_Id";
    public static final String U_ID = "UId";
    public static final String P_DESC = "P_Desc";
    public static final String P_LOCATION = "P_Location";
    public static final String P_LAT = "P_Lat";
    public static final String P_Lang = "P_Lang";
    public static final String U_NAME = "U_Name";
    public static final String U_FULL_NAME = "U_Full_Name";
    public static final String U_PF_PHOTO = "U_Pf_Photo";
    public static final String P_IMAGE = "P_Image";
    public static final String P_FILE_TYPE = "P_File_Image";
    public static final String P_IMAGES = "P_Images";
    public static final String P_COMMENTS = "P_Comments";
    public static final String P_M_IMAGES = "P_M_Images";
    public static final String P_BOOKMARKED = "P_Bookmarked";
    public static final String P_M_COMMENTS = "P_M_Comments";
    public static final String P_TIME_DURATON = "P_Time_Duration";
    public static final String P_HEIGHT = "P_Height";
    public static final String P_WIDTH = "P_Width";
    public static final String USER_ID = "User_Id";
    public static final String DESC = "Description";
    public static final String IMAGE = "Image";
    public static final String M_IMAGE = "M_Image";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Table_Other_User Table Columns names
    private static final String O_ID = "Id";
    private static final String OU_ID = "_Id";
    // Table_User_Post Table Columns names
    private static final String ID = "Id";
    // Table_Rating_User TableColumns names
    private static final String R_ID = "Id";
    private static final String R_U_ID = "U_Id";
    private static final String R_NAME = "U_Name";
    private static final String R_PHOTO = "Photo";
    private static final String R_VALUE = "Value";
    private static final String R_ACTIVITY = "Activity";
    // Table_Saved_Post Table Columns names
    private static final String S_T_ID = "Id";
    private static final String S_ID = "S_Id";
    // Table_User_Details Table Columns names
    private static final String USER_ID1 = "_Id";
    private static final String USER_ID2 = "User_Id";
    private static final String DATA = "User_Details";
    private static final String OTHER = "Other_User_Details";
    private static final String PROFESSION = "Profession";
    private static final String FRIENDS = "Friends";
    // User Post Details Table Name
    public static String TABLE_USER_POST = "Table_User_Post";
    public static String TABLE_OTHER_USER = "Table_Other_User";
    public static String TABLE_OTHER_USER_POST = "Table_Other_User_Post";
    public static String TABLE_SAVED_POST = "Table_Saved_Post";
    public static String TABLE_USER_DETAILS = "Table_User_Details";
    public static String TABLE_RATE_USER = "Table_Rating_User";
    Context context;


    //2nd table for appointment
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_DATABASE_OTHER_USER = "CREATE TABLE " + TABLE_OTHER_USER + "(" +
                O_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OU_ID + " TEXT," +
                O_DATA + " TEXT," +
                O_FRIENDS + " TEXT," +
                O_POSTS + " TEXT," +
                O_OTHER + " TEXT);";

        String CREATE_DATABASE_USER_POST = "CREATE TABLE " + TABLE_USER_POST + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                P_ID + " INTEGER," +
                U_ID + " INTEGER," +
                P_DESC + " TEXT," +
                P_LOCATION + " TEXT," +
                P_LAT + " TEXT," +
                P_Lang + " TEXT," +
                U_NAME + " TEXT," +
                U_FULL_NAME + " TEXT," +
                U_PF_PHOTO + " TEXT," +
                P_IMAGE + " TEXT," +
                P_FILE_TYPE + " TEXT," +
                P_IMAGES + " TEXT," +
                P_COMMENTS + " TEXT," +
                P_M_IMAGES + " INTEGER," +
                P_BOOKMARKED + " INTEGER," +
                P_M_COMMENTS + " INTEGER," +
                P_TIME_DURATON + " TEXT," +
                P_HEIGHT + " TEXT," +
                P_WIDTH + " TEXT);";

        String CREATE_DATABASE_OTHER_USER_POST = "CREATE TABLE " + TABLE_OTHER_USER_POST + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                P_ID + " INTEGER," +
                U_ID + " INTEGER," +
                P_DESC + " TEXT," +
                P_LOCATION + " TEXT," +
                P_LAT + " TEXT," +
                P_Lang + " TEXT," +
                U_NAME + " TEXT," +
                U_FULL_NAME + " TEXT," +
                U_PF_PHOTO + " TEXT," +
                P_IMAGE + " TEXT," +
                P_FILE_TYPE + " TEXT," +
                P_IMAGES + " TEXT," +
                P_COMMENTS + " TEXT," +
                P_M_IMAGES + " INTEGER," +
                P_BOOKMARKED + " INTEGER," +
                P_M_COMMENTS + " INTEGER," +
                P_TIME_DURATON + " TEXT," +
                P_HEIGHT + " TEXT," +
                P_WIDTH + " TEXT);";

        String CREATE_DATABASE_SAVED_POST = "CREATE TABLE " + TABLE_SAVED_POST + "(" +
                S_T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                S_ID + " INTEGER," +
                USER_ID + " INTEGER," +
                DESC + " TEXT," +
                IMAGE + " TEXT," +
                M_IMAGE + " INTEGER);";

        String CREATE_DATABASE_USER_DETIALS = "CREATE TABLE " + TABLE_USER_DETAILS + "(" +
                USER_ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_ID2 + " TEXT," +
                DATA + " TEXT," +
                OTHER + " TEXT," +
                PROFESSION + " TEXT," +
                FRIENDS + " TEXT);";

        String CREATE_RATE_USER = "CREATE TABLE " + TABLE_RATE_USER + "(" +
                R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                R_U_ID + " INTEGER," +
                R_NAME + " TEXT," +
                R_PHOTO + " TEXT," +
                R_VALUE + " FLOAT," +
                R_ACTIVITY + " TEXT);";


        db.execSQL(CREATE_DATABASE_USER_POST);
        db.execSQL(CREATE_DATABASE_OTHER_USER_POST);
        db.execSQL(CREATE_DATABASE_OTHER_USER);
        db.execSQL(CREATE_DATABASE_SAVED_POST);
        db.execSQL(CREATE_DATABASE_USER_DETIALS);
        db.execSQL(CREATE_RATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OTHER_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_POST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OTHER_USER_POST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_POST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATE_USER);
        // Create tables again
        onCreate(db);
    }

    // Delete all table
    public void delete_table() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OTHER_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_POST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OTHER_USER_POST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_POST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATE_USER);
        // Create tables again
        onCreate(db);
    }

    // Methods For Table_Other_USer
    public boolean insertOtherUSer(String _id, String data, String friends, String post, String other) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OU_ID, _id);
        values.put(DATA, data);
        values.put(OTHER, friends);
        values.put(PROFESSION, post);
        values.put(FRIENDS, other);

        long result = db.insert(TABLE_OTHER_USER, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateOtherUSer(String _id, String data, String friends, String post, String other) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(OU_ID, _id);
        values.put(DATA, data);
        values.put(OTHER, friends);
        values.put(PROFESSION, post);
        values.put(FRIENDS, other);

        db.update(TABLE_OTHER_USER, values, OU_ID + " = ?", new String[]{String.valueOf(_id)});
        return true;
    }

    public Cursor getAllOtherUSer() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_OTHER_USER, null);
        return res;
    }

    public boolean chceckOtherUSer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String Query = "SELECT * FROM '" + TABLE_OTHER_USER
                + "' WHERE " + OU_ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    // Methods For Table_User_Post
    public boolean InsertData_Post(int id, int user_id, String description, String location, String lat, String lang, String username
            , String full_name, String photo, String image, String file_type, String images, String comments, int multiple_images, int bookmarked,
                                   int more_comments, String time_duration, String height, String width) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(P_ID, id);
        values.put(U_ID, user_id);
        values.put(P_DESC, description);
        values.put(P_LOCATION, location);
        values.put(P_LAT, lat);
        values.put(P_Lang, lang);
        values.put(U_NAME, username);
        values.put(U_FULL_NAME, full_name);
        values.put(U_PF_PHOTO, photo);
        values.put(P_IMAGE, image);
        values.put(P_FILE_TYPE, file_type);
        values.put(P_IMAGES, images);
        values.put(P_COMMENTS, comments);
        values.put(P_M_IMAGES, multiple_images);
        values.put(P_BOOKMARKED, bookmarked);
        values.put(P_M_COMMENTS, more_comments);
        values.put(P_TIME_DURATON, time_duration);
        values.put(P_HEIGHT, height);
        values.put(P_WIDTH, width);

        long result = db.insert(TABLE_USER_POST, null, values);
        db.close();
        return result != -1;
    }

    public boolean UpdateData_Post(int id, int user_id, String description, String location, String lat, String lang, String username
            , String full_name, String photo, String image, String file_type, String images, String comments, int multiple_images, int bookmarked,
                                   int more_comments, String time_duration, String height, String width) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(U_ID, user_id);
        values.put(P_DESC, description);
        values.put(P_LOCATION, location);
        values.put(P_LAT, lat);
        values.put(P_Lang, lang);
        values.put(U_NAME, username);
        values.put(U_FULL_NAME, full_name);
        values.put(U_PF_PHOTO, photo);
        values.put(P_IMAGE, image);
        values.put(P_IMAGE, file_type);
        values.put(P_IMAGES, images);
        values.put(P_COMMENTS, comments);
        values.put(P_M_IMAGES, multiple_images);
        values.put(P_BOOKMARKED, bookmarked);
        values.put(P_M_COMMENTS, more_comments);
        values.put(P_TIME_DURATON, time_duration);
        values.put(P_HEIGHT, height);
        values.put(P_WIDTH, width);

        db.update(TABLE_USER_POST, values, P_ID + " = ?", new String[]{String.valueOf(id)});
        return true;
    }

    public Cursor getAll_Records() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_USER_POST, null);
        return res;
    }

    public boolean Check_Contact_Exist(int id, int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
//        String Query = "SELECT * FROM 'Table_Contacts' WHERE Number = '8000304001' AND Name = 'A'";
//        Log.e("LLLLL :", number + "    " + name);
        String Query = "SELECT * FROM '" + TABLE_USER_POST
                + "' WHERE " + P_ID + " = '" + id
                + "' AND " + U_ID + " = '" + user_id + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    // Methods For Table_Other_User_Post
    public boolean insertOtherUserPost(int id, int user_id, String description, String location, String lat, String lang, String username
            , String full_name, String photo, String image,String file_type, String images, String comments, int multiple_images, int bookmarked,
                                       int more_comments, String time_duration, String height, String width) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(P_ID, id);
        values.put(U_ID, user_id);
        values.put(P_DESC, description);
        values.put(P_LOCATION, location);
        values.put(P_LAT, lat);
        values.put(P_Lang, lang);
        values.put(U_NAME, username);
        values.put(U_FULL_NAME, full_name);
        values.put(U_PF_PHOTO, photo);
        values.put(P_IMAGE, image);
        values.put(P_FILE_TYPE, file_type);
        values.put(P_IMAGES, images);
        values.put(P_COMMENTS, comments);
        values.put(P_M_IMAGES, multiple_images);
        values.put(P_BOOKMARKED, bookmarked);
        values.put(P_M_COMMENTS, more_comments);
        values.put(P_TIME_DURATON, time_duration);
        values.put(P_HEIGHT, height);
        values.put(P_WIDTH, width);

        long result = db.insert(TABLE_OTHER_USER_POST, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateOtherUserPost(int id, int user_id, String description, String location, String lat, String lang, String username
            , String full_name, String photo, String image,String file_type, String images, String comments, int multiple_images, int bookmarked,
                                       int more_comments, String time_duration, String height, String width) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(U_ID, user_id);
        values.put(P_DESC, description);
        values.put(P_LOCATION, location);
        values.put(P_LAT, lat);
        values.put(P_Lang, lang);
        values.put(U_NAME, username);
        values.put(U_FULL_NAME, full_name);
        values.put(U_PF_PHOTO, photo);
        values.put(P_IMAGE, image);
        values.put(P_FILE_TYPE, file_type);
        values.put(P_IMAGES, images);
        values.put(P_COMMENTS, comments);
        values.put(P_M_IMAGES, multiple_images);
        values.put(P_BOOKMARKED, bookmarked);
        values.put(P_M_COMMENTS, more_comments);
        values.put(P_TIME_DURATON, time_duration);
        values.put(P_HEIGHT, height);
        values.put(P_WIDTH, width);

        db.update(TABLE_OTHER_USER_POST, values, P_ID + " = ?", new String[]{String.valueOf(id)});
        return true;
    }

    public Cursor getAllOtgherPost() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_OTHER_USER_POST, null);
        return res;
    }

    public boolean chceckPostExits(int id, int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
//        String Query = "SELECT * FROM 'Table_Contacts' WHERE Number = '8000304001' AND Name = 'A'";
//        Log.e("LLLLL :", number + "    " + name);
        String Query = "SELECT * FROM '" + TABLE_OTHER_USER_POST
                + "' WHERE " + P_ID + " = '" + id
                + "' AND " + U_ID + " = '" + user_id + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    // Methods For Table_Saved_Post
    public boolean insertSavedPost(int id, int user_id, String description, String image, int multiple_image) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(S_ID, id);
        values.put(USER_ID, user_id);
        values.put(DESC, description);
        values.put(IMAGE, image);
        values.put(M_IMAGE, multiple_image);

        long result = db.insert(TABLE_SAVED_POST, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateSavedPost(int id, int user_id, String description, String image, int multiple_image) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(S_ID, id);
        values.put(USER_ID, user_id);
        values.put(DESC, description);
        values.put(IMAGE, image);
        values.put(M_IMAGE, multiple_image);

        db.update(TABLE_SAVED_POST, values, S_ID + " = ?", new String[]{String.valueOf(id)});
        return true;
    }

    public Cursor getAllSavedPost() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_SAVED_POST, null);
        return res;
    }

    public boolean chceckSavedPostExits(int id, int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
//        String Query = "SELECT * FROM 'Table_Contacts' WHERE Number = '8000304001' AND Name = 'A'";
//        Log.e("LLLLL :", number + "    " + name);
        String Query = "SELECT * FROM '" + TABLE_SAVED_POST
                + "' WHERE " + S_ID + " = '" + id
                + "' AND " + USER_ID + " = '" + user_id + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    // Methods For Table_User_Details
    public boolean insertUserDetails(String _id, String data, String Others, String profession, String friend) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ID2, _id);
        values.put(DATA, data);
        values.put(OTHER, Others);
        values.put(PROFESSION, profession);
        values.put(FRIENDS, friend);

        long result = db.insert(TABLE_USER_DETAILS, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateUserDetails(String _id, String data, String Others, String profession, String friend) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_ID2, _id);
        values.put(DATA, data);
        values.put(OTHER, Others);
        values.put(PROFESSION, profession);
        values.put(FRIENDS, friend);

        db.update(TABLE_USER_DETAILS, values, USER_ID2 + " = ?", new String[]{String.valueOf(_id)});
        return true;
    }

    public Cursor getAllUserDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_USER_DETAILS, null);
        return res;
    }

    public boolean chceckUserDetails(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String Query = "SELECT * FROM '" + TABLE_USER_DETAILS
                + "' WHERE " + USER_ID2 + " = '" + id + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    // Methods For Table_Rating_User
    public boolean insertUserRateDetails(int _id, String name, String photo, Float value, String activity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(R_U_ID, _id);
        values.put(R_NAME, name);
        values.put(R_PHOTO, photo);
        values.put(R_VALUE, value);
        values.put(R_ACTIVITY, activity);

        long result = db.insert(TABLE_RATE_USER, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateUserRateDetails(int _id, String name, String photo, Float value, String activity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(R_NAME, name);
        values.put(R_PHOTO, photo);
        values.put(R_VALUE, value);
        values.put(R_ACTIVITY, activity);

        db.update(TABLE_RATE_USER, values, R_U_ID + " = ?", new String[]{String.valueOf(_id)});
        return true;
    }

    public Cursor getAllUserRateDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_RATE_USER, null);
        return res;
    }

    public boolean chceckUserRateDetails(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String Query = "SELECT * FROM '" + TABLE_RATE_USER
                + "' WHERE " + R_U_ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
