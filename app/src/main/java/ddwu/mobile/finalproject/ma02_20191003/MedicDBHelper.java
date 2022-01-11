package ddwu.mobile.finalproject.ma02_20191003;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MedicDBHelper extends SQLiteOpenHelper {

    final static String TAG = "MedicDBHelper";

    final static String DB_NAME = "medic_db";
    public final static String TABLE_NAME = "medic_table";

    public final static String COL_ID = "_id";
    public final static String COL_NAME = "name";
    public final static String COL_DATE = "date";
    public final static String COL_TIME = "time";
    public final static String COL_IMAGE = "image";
    public final static String COL_MEMO = "memo";

    public MedicDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement," +
                COL_NAME + " TEXT, " + COL_DATE + " TEXT, " + COL_TIME + " TEXT, " + COL_IMAGE +  " TEXT, " + COL_MEMO +" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
