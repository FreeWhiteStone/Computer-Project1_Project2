package control.user.com.usercontrol.database;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DbTableService{

    public static SQLiteOpenHelper sqoh;
    public static String TAG = "DATABASE";

    private static SQLiteDatabase getDB(){
        SQLiteDatabase database = Database.getDatabase();
        if (database == null || !database.isOpen()) {
            database = Database.getSqoh().getWritableDatabase();
        }
        return database;
    }

    public static void createAccountTable(){
        try {

            String createTable = "CREATE TABLE IF NOT EXISTS " + DbConstants.ACCOUNT_TABLE_NAME + "("
                    + DbConstants.ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + DbConstants.ACCOUNT_NAME + " TEXT,"
                    + DbConstants.ACCOUNT_MAIL + " TEXT ,"
                    + DbConstants.ACCOUNT_PASSWORD + " TEXT)";
            getDB().execSQL(createTable);
            getDB().close();
            Log.e(TAG, DbConstants.ACCOUNT_TABLE_NAME + " table created. ");
        } catch (Exception e) {
            Log.e(TAG, DbConstants.ACCOUNT_TABLE_NAME + " table cannot be created. Exception: " + e
                    .getMessage());
        }
    }

    public static void dropTable(String tableName){
        try {
            String DROP_TABLE = "DROP TABLE IF EXISTS " + tableName;
            getDB().execSQL(DROP_TABLE);
            getDB().close();
            Log.e(TAG, "Table '" + tableName + "' dropped");
        } catch (SQLException e) {
            Log.e(TAG, "Table '" + tableName + "'cannot be dropped. Exception: " + e.getMessage());
        }
    }
}
