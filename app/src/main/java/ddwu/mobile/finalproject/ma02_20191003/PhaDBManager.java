package ddwu.mobile.finalproject.ma02_20191003;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class PhaDBManager {

    PhaDBHelper helper = null;
    Cursor cursor = null;
    PhaInfoDto pha;

    public PhaDBManager(Context context) {
        helper = new PhaDBHelper(context);
    }

    public ArrayList<PhaInfoDto> getAllPha() {
        ArrayList list = new ArrayList();
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + PhaDBHelper.TABLE_NAME, null);

        while(cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex(PhaDBHelper.COL_ID));
            String phaName = cursor.getString(cursor.getColumnIndex(PhaDBHelper.COL_NAME));
            String phaAddress = cursor.getString(cursor.getColumnIndex(PhaDBHelper.COL_ADDRESS));
            String phaPhone = cursor.getString(cursor.getColumnIndex(PhaDBHelper.COL_PHONE));
            String xpos = cursor.getString(cursor.getColumnIndex(PhaDBHelper.COL_XPOS));
            String ypos = cursor.getString(cursor.getColumnIndex(PhaDBHelper.COL_YPOS));

            pha = new PhaInfoDto();
            pha.set_id(id); pha.setName(phaName);
            pha.setAddress(phaAddress); pha.setPhone(phaPhone);
            pha.setXpos(xpos);  pha.setYpos(ypos);

            list.add (pha);
        }

        cursor.close();
        helper.close();

        return list;
    }

    public boolean addPha(PhaInfoDto pid) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(helper.COL_NAME, pid.getName());
        value.put(helper.COL_ADDRESS, pid.getAddress());
        value.put(helper.COL_PHONE, pid.getPhone());
        value.put(helper.COL_XPOS, pid.getXpos());
        value.put(helper.COL_YPOS, pid.getYpos());

        long count = db.insert(helper.TABLE_NAME, null, value);
        helper.close();
        if (count > 0) return true;
        return false;
    }

    public boolean removePha(String address) {
        SQLiteDatabase db = helper.getWritableDatabase();

        String whereClause = helper.COL_ADDRESS + "=?";
        String[] whereArgs = new String[] {String.valueOf(address)};

        int result = db.delete(helper.TABLE_NAME, whereClause, whereArgs);

        if (result > 0) return true;
        return false;
    }

    public boolean btnChecked(String address) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + helper.COL_ADDRESS + " FROM " + helper.TABLE_NAME, null);

        boolean checked = false;

        while (cursor.moveToNext()) {
            if (cursor.getString(0).equals(address)) {
                checked = true;
                break;
            }
        }
        return checked;
    }
}
