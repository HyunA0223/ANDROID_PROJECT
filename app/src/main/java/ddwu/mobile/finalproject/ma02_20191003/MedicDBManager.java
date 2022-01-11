package ddwu.mobile.finalproject.ma02_20191003;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class MedicDBManager {
    MedicDBHelper medicDBHelper = null;
    Cursor cursor = null;
    MedicDto md;

    public MedicDBManager(Context context) {
        medicDBHelper = new MedicDBHelper(context);
    }

    public ArrayList<MedicDto> getAllMedic() {
        ArrayList medicList = new ArrayList();
        SQLiteDatabase db = medicDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + MedicDBHelper.TABLE_NAME, null);

        while(cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex(MedicDBHelper.COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(MedicDBHelper.COL_NAME));
            String date = cursor.getString(cursor.getColumnIndex(MedicDBHelper.COL_DATE));
            String time = cursor.getString(cursor.getColumnIndex(MedicDBHelper.COL_TIME));
            String image = cursor.getString(cursor.getColumnIndex(MedicDBHelper.COL_IMAGE));
            String memo = cursor.getString(cursor.getColumnIndex(MedicDBHelper.COL_MEMO));

            md = new MedicDto();
            md.set_id(id);  md.setName(name);   md.setDate(date);
            md.setTime(time);   md.setImage(image); md.setMemo(memo);
            medicList.add (md);
        }

        cursor.close();
        medicDBHelper.close();

        return medicList;
    }

    public boolean addMedic(MedicDto md) {
        SQLiteDatabase db = medicDBHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(medicDBHelper.COL_NAME, md.getName());
        value.put(medicDBHelper.COL_DATE, md.getDate());
        value.put(medicDBHelper.COL_TIME, md.getTime());
        value.put(medicDBHelper.COL_IMAGE, md.getImage());
        value.put(medicDBHelper.COL_MEMO, md.getMemo());

        long count = db.insert(medicDBHelper.TABLE_NAME, null, value);
        medicDBHelper.close();
        if (count > 0) return true;
        return false;
    }

    public MedicDto searchMedic(long id) {
        SQLiteDatabase db = medicDBHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(" SELECT * FROM " + MedicDBHelper.TABLE_NAME + " WHERE _id=" + id + "; ", null);

        while(cursor.moveToNext()) {
            long memoid = cursor.getInt(cursor.getColumnIndex(MedicDBHelper.COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(MedicDBHelper.COL_NAME));
            String date = cursor.getString(cursor.getColumnIndex(MedicDBHelper.COL_DATE));
            String time = cursor.getString(cursor.getColumnIndex(MedicDBHelper.COL_TIME));
            String image = cursor.getString(cursor.getColumnIndex(MedicDBHelper.COL_IMAGE));
            String memo = cursor.getString(cursor.getColumnIndex(MedicDBHelper.COL_MEMO));

            md = new MedicDto();
            md.set_id(memoid);  md.setName(name);   md.setDate(date);
            md.setTime(time);   md.setImage(image); md.setMemo(memo);
        }

        cursor.close();
        medicDBHelper.close();

        return md;
    }

    public boolean removeMedic(long id) {
        SQLiteDatabase db = medicDBHelper.getWritableDatabase();

        String whereClause = medicDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(id)};

        int result = db.delete(medicDBHelper.TABLE_NAME, whereClause, whereArgs);

        if (result > 0) return true;
        medicDBHelper.close();
        return false;
    }

    public boolean updateMedic(MedicDto md) {
        SQLiteDatabase db = medicDBHelper.getWritableDatabase();

        ContentValues row = new ContentValues();
        row.put(medicDBHelper.COL_NAME, md.getName());
        row.put(medicDBHelper.COL_DATE, md.getDate());
        row.put(medicDBHelper.COL_TIME, md.getTime());
        row.put(medicDBHelper.COL_IMAGE, md.getImage());
        row.put(medicDBHelper.COL_MEMO, md.getMemo());

        String whereClause = medicDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(md.get_id())};

        long count = db.update(medicDBHelper.TABLE_NAME, row, whereClause, whereArgs);
        medicDBHelper.close();
        if (count > 0) return true;
        return false;
    }

    public long searchId(MedicDto md) {
        SQLiteDatabase db = medicDBHelper.getReadableDatabase();
        long memoid = -1;

        String[] columns = {medicDBHelper.COL_ID};
        String selection = medicDBHelper.COL_IMAGE + "=?";
        String[] selectArgs = new String[] {md.getImage()};

        Cursor cursor = db.query(medicDBHelper.TABLE_NAME, columns, selection, selectArgs,
                null, null, null, null);

        while (cursor.moveToNext()) {
            memoid = cursor.getLong(cursor.getColumnIndex(medicDBHelper.COL_ID));
        }

        cursor.close();
        medicDBHelper.close();

        return memoid;
    }
}
