package control.user.com.usercontrol.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DbEntryService{

    public static String TAG = "DbEntryService";


    public static SQLiteDatabase getDB(){
        SQLiteDatabase database = Database.getDatabase();
        if (database == null || !database.isOpen()) {
            database = Database.getSqoh().getWritableDatabase();
        }
        return database;
    }

    /*Save operations
        *******************************
        */
    public static Long saveAccount(String name, String mail, String pass){

        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.ACCOUNT_MAIL, mail);
            values.put(DbConstants.ACCOUNT_NAME, name);
            values.put(DbConstants.ACCOUNT_PASSWORD, pass);

            long id = getDB().insert(DbConstants.ACCOUNT_TABLE_NAME, null, values);
            Log.e(TAG, "Account saved to database. Id: " + name);
            return id;
        } catch (Exception e) {
            Log.e(TAG, "Account cannot be saved to database. Exception: " + e.getMessage());
            return null;
        }
    }



    /*
    Get operations
    **************************************************************
     */

    public static ArrayList<HashMap<String, String>> getAllAccounts(){

        try {
            String selectQuery = "SELECT * FROM " + DbConstants.ACCOUNT_TABLE_NAME + " order by " +
                    DbConstants.ACCOUNT_ID + " asc";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String,
                    String>>();

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }

            return messageList;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return new ArrayList<HashMap<String, String>>();
        }
    }


    /*Remove operations
    *******************************************************************
     */

    public static void removeAccountById(String accId){
        try {
            getDB().delete(DbConstants.ACCOUNT_TABLE_NAME, DbConstants.ACCOUNT_ID + " = '" +
                    accId + "' ", null);

            Log.e(TAG, "Account deleted. ID: " + accId);
        } catch (Exception e) {
            Log.e(TAG, "Account cannot be deleted. ID: " + accId);
        }
    }

    public static void updateAccount(int id, String name, String mail, String pass){
        try {
            ContentValues values = new ContentValues();

            values.put(DbConstants.ACCOUNT_MAIL, mail);
            values.put(DbConstants.ACCOUNT_NAME, name);
            values.put(DbConstants.ACCOUNT_PASSWORD, pass);

            getDB().update(DbConstants.ACCOUNT_TABLE_NAME, values, DbConstants.ACCOUNT_ID + " = '" +
                    id + "'", null);
            Log.e(TAG, "Audio updated. Id: " + id);
        } catch (Exception e) {
            Log.e(TAG, "Audio cannot be updated. Exception: " + e.getMessage());
        }
    }


    public static boolean login(String email, String pass){
        try {
            String selectQuery = "SELECT * FROM " + DbConstants.ACCOUNT_TABLE_NAME + " where " + DbConstants
                    .ACCOUNT_MAIL + " = '" + email + "' and " + DbConstants.ACCOUNT_PASSWORD + " = '" + pass + "'";

            Cursor cursor = getDB().rawQuery(selectQuery, null);
            ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String,
                    String>>();

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }

            return !messageList.isEmpty();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return false;
        }
    }
}
