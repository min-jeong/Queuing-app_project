package com.ktds.queuing_app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.ktds.queuing_app.vo.QueuingVO;

/**
 * Created by 206-013 on 2016-07-08.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 2;

    private Context context;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;

    }

    /**
     * If not definition DataBase, action one time.
     * DB create duty
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE USER_TABLE( ");
        sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" REG_ID TEXT DEFAULT '0', ");
        sb.append(" BRANCH_ID TEXT DEFAULT '0') ");

        // sql 실행
        db.execSQL(sb.toString());
        Toast.makeText(context, "Table Created", Toast.LENGTH_SHORT).show();
    }

    /**
     * MyApplication version upgrade or Table Construct is modified action.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if( oldVersion == 1 && newVersion == 2 ) {
//            StringBuffer sb = new StringBuffer();
//            sb.append(" ALTER TABLE USER_TABLE ADD REG_ID TEXT ");
//
//            db.execSQL(sb.toString());
//        }
//        Toast.makeText(context, "Version 올라감..", Toast.LENGTH_SHORT).show();
    }

    // previously tests
    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }

    public void setQueuingInfo(QueuingVO queuingVO) {
        //1. write DB instance brief.
        SQLiteDatabase db = getWritableDatabase();

        //2. person data insert.
        StringBuffer sb = new StringBuffer();

        sb.append(" INSERT INTO USER_TABLE ( ");
        sb.append(" REG_ID, BRANCH_ID ) ");
        // ?형태로 집어넣기.
        sb.append(" VALUES (?, ?)");
        db.execSQL(sb.toString(), new Object[] { queuingVO.getRegId(), queuingVO.getBranchId() });

        Toast.makeText(context, "Inserts", Toast.LENGTH_SHORT).show();
    }

    public QueuingVO getQueuingInfo( QueuingVO queuingVO) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, REG_ID, BRANCH_ID FROM USER_TABLE");

        // read only DB instance .
        SQLiteDatabase db = getReadableDatabase();


        // Selecting
        Cursor cursor = db.rawQuery(sb.toString(), null);


        Log.d("Results", cursor.getColumnCount()+"");

        while ( cursor.moveToNext() ) {
            queuingVO.set_id(cursor.getInt(0));
            queuingVO.setRegId(cursor.getString(1));
            queuingVO.setBranchId(cursor.getString(2));
        }
        return queuingVO;
    }
}
