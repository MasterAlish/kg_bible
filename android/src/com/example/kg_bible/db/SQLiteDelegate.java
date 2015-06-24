package com.example.kg_bible.db;

import android.database.sqlite.SQLiteDatabase;

public interface SQLiteDelegate {
    void databaseOpening();
    void databaseOpened(SQLiteDatabase db);
    void databaseOpenError(String error);
}
