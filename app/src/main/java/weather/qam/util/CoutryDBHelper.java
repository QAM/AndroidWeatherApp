package weather.qam.util;

import android.database.*;
import android.database.sqlite.*;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

/**
 * Created by qam on 1/29/15.
 */
public class CoutryDBHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION_NUMBER = 1;
    private static final String DB_COLUMN_1_NAME = "country_name";
    private String DB_TABLE_NAME;
    private String DB_CREATE_SCRIPT;

    private SQLiteDatabase sqliteDBInstance = null;

    public CoutryDBHelper(Context context, String dbName, String tableName)
    {
        super(context, dbName, null, DB_VERSION_NUMBER);
        DB_TABLE_NAME = tableName;
        DB_CREATE_SCRIPT = "create table " + DB_TABLE_NAME +
                " (_id integer primary key autoincrement, country_name text not null);)";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO: Implement onUpgrade
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDBInstance)
    {
        Log.i("onCreate", "Creating the database...");
        sqliteDBInstance.execSQL(DB_CREATE_SCRIPT);
    }

    public void openDB() throws SQLException
    {
        Log.i("openDB", "Checking sqliteDBInstance...");
        if(this.sqliteDBInstance == null)
        {
            Log.i("openDB", "Creating sqliteDBInstance...");
            this.sqliteDBInstance = this.getWritableDatabase();
        }else{
            if(!this.sqliteDBInstance.isOpen())
                this.sqliteDBInstance = this.getWritableDatabase();
        }
    }

    public void closeDB()
    {
        if(this.sqliteDBInstance != null && this.sqliteDBInstance.isOpen())
                this.sqliteDBInstance.close();
    }

    public long insertCountry(String countryName)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_COLUMN_1_NAME, countryName);
        Log.i(this.toString() + " - insertCountry", "Inserting: " + countryName);
        return this.sqliteDBInstance.insert(DB_TABLE_NAME, null, contentValues);
    }

    public boolean removeCountry(String countryName)
    {
        int result = this.sqliteDBInstance.delete(DB_TABLE_NAME, "country_name='" + countryName + "'", null);

        if(result > 0)
            return true;
        else
            return false;
    }

    public long updateCountry(String oldCountryName, String newCountryName)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_COLUMN_1_NAME, newCountryName);
        return this.sqliteDBInstance.update(DB_TABLE_NAME, contentValues, "country_name='" + oldCountryName + "'", null);
    }

    public String[] getAllCountries()
    {
        Cursor cursor = this.sqliteDBInstance.query(DB_TABLE_NAME, new String[] {DB_COLUMN_1_NAME}, null, null, null, null, null);

        if(cursor.getCount() >0)
        {
            String[] str = new String[cursor.getCount()];
            int i = 0;

            while (cursor.moveToNext())
            {
                str[i] = cursor.getString(cursor.getColumnIndex(DB_COLUMN_1_NAME));
                i++;
            }
            return str;
        }
        else
        {
            return new String[] {};
        }
    }

    public String[] getCountries(String search)
    {
        Cursor cursor = this.sqliteDBInstance.query(DB_TABLE_NAME, new String[] {DB_COLUMN_1_NAME}, DB_COLUMN_1_NAME + " like '%" + search + "%'", null, null, null, null);

        if(cursor.getCount() >0)
        {
            String[] str = new String[cursor.getCount()];
            int i = 0;

            while (cursor.moveToNext())
            {
                str[i] = cursor.getString(cursor.getColumnIndex(DB_COLUMN_1_NAME));
                i++;
            }
            return str;
        }
        else
        {
            return new String[] {};
        }
    }

    public int getCount(){
        Cursor cursor = this.sqliteDBInstance.query(DB_TABLE_NAME, new String[] {DB_COLUMN_1_NAME}, null, null, null, null, null);
        return cursor.getCount();
    }

    public Cursor getCursor(){
        return this.sqliteDBInstance.query(DB_TABLE_NAME, new String[] {DB_COLUMN_1_NAME}, null, null, null, null, null);
    }
}
