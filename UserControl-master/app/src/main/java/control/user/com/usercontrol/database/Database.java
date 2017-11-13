package control.user.com.usercontrol.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper{

    private static SQLiteDatabase database;
    private static SQLiteOpenHelper sqoh;

    public Database(Context context){
        super(context, DbConstants.USER_DATABASE_NAME, null, DbConstants.CMP_DB_VERSION);
        sqoh = this;
        this.setWriteAheadLoggingEnabled(true);
        database = getWritableDatabase();
    }

    public static SQLiteDatabase getDatabase(){
        return database;
    }

    public static SQLiteOpenHelper getSqoh(){
        return sqoh;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        sqoh = this;

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}
