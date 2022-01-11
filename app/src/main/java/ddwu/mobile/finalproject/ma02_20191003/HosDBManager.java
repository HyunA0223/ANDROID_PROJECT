package ddwu.mobile.finalproject.ma02_20191003;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class HosDBManager {
    HosDBHelper helper = null;
    Cursor cursor = null;
    HosInfoDto hid;

    public HosDBManager(Context context) {
        helper = new HosDBHelper(context);
    }

    public ArrayList<HosInfoDto> getAllHos() {
        ArrayList list = new ArrayList();
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + HosDBHelper.TABLE_NAME, null);

        while(cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex(HosDBHelper.COL_ID));
            String code = cursor.getString(cursor.getColumnIndex(HosDBHelper.COL_CODE));
            String hosName = cursor.getString(cursor.getColumnIndex(HosDBHelper.COL_NAME));
            String hosAddress = cursor.getString(cursor.getColumnIndex(HosDBHelper.COL_ADDRESS));
            String hosPhone = cursor.getString(cursor.getColumnIndex(HosDBHelper.COL_PHONE));
            String xpos = cursor.getString(cursor.getColumnIndex(HosDBHelper.COL_XPOS));
            String ypos = cursor.getString(cursor.getColumnIndex(HosDBHelper.COL_YPOS));

            hid = new HosInfoDto();
            hid.set_id(id); hid.setName(hosName); hid.setCode(code);
            hid.setAddress(hosAddress); hid.setPhone(hosPhone);
            hid.setXPos(xpos);  hid.setYPos(ypos);

            list.add (hid);
        }

        cursor.close();
        helper.close();

        return list;
    }

    public boolean addHos(HosInfoDto hid) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(helper.COL_NAME, hid.getName());
        value.put(helper.COL_CODE, hid.getCode());
        value.put(helper.COL_ADDRESS, hid.getAddress());
        value.put(helper.COL_PHONE, hid.getPhone());
        value.put(helper.COL_XPOS, hid.getXPos());
        value.put(helper.COL_YPOS, hid.getYPos());

        long count = db.insert(helper.TABLE_NAME, null, value);
        helper.close();
        if (count > 0) return true;
        return false;
    }

    public boolean removeHos(String address) {
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
