package com.example.kg_bible.db;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "kg_bible.db";
    public static String DATABASE_PATH;
    private static final int DATABASE_VERSION = 1;
    private final SQLiteDelegate delegate;

    private SQLiteDatabase dataBase;
    private final Context dbContext;

    public SQLiteHelper(Context context, SQLiteDelegate delegate) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.dbContext = context;
        this.delegate = delegate;

        PackageInfo p = null;
        try {
            p = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        DATABASE_PATH = p.applicationInfo.dataDir + "/databases/";

        delegate.databaseOpening();
        prepareDatabase();
    }

    private void prepareDatabase() {
        new DownloadFilesTask().execute();
    }

    private class DownloadFilesTask extends AsyncTask<Void, Void, Void> {
        String error = null;
        protected Void doInBackground(Void... urls) {
            if (checkDataBase()) {
                openDataBase();
            } else {
                try {
                    getReadableDatabase();
                    copyDataBase();
                    close();
                    openDataBase();
                    Log.d("DB:", "Initial database is created");


                } catch (IOException e) {
                    error = "Error database copying";
                }
            }
            return null;
        }

        protected void onPostExecute(Void s) {
            if(error != null)
                delegate.databaseOpenError(error);
            else
                delegate.databaseOpened(dataBase);
        }
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = dbContext.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private void openDataBase() throws SQLException {
        String dbPath = DATABASE_PATH + DATABASE_NAME;
        dataBase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        boolean exist = false;
        try {
            String dbPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteCantOpenDatabaseException e) {
            Log.d("DB", "database does't exist");
        } catch (SQLiteException e) {
            Log.d("DB", "database does't exist");
        }

        if (checkDB != null) {
            exist = true;
            checkDB.close();
        }
        return exist;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}