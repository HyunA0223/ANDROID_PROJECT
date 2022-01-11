package ddwu.mobile.finalproject.ma02_20191003;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PhaDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "pha_db";
    public final static String TABLE_NAME = "pha_table";
    public final static String COL_ID = "_id";
    public final static String COL_NAME = "name";
    public final static String COL_ADDRESS = "address";
    public final static String COL_PHONE = "phone";
    public final static String COL_XPOS = "xpos";
    public final static String COL_YPOS = "ypos";

    public PhaDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {   // 첫 호출 시에만 실행
        db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement," +
                COL_NAME + " TEXT, " + COL_ADDRESS + " TEXT, " + COL_PHONE + " TEXT, " + COL_XPOS + " TEXT, " + COL_YPOS + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
    }
}
