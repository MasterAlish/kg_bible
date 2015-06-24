package com.example.kg_bible.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.kg_bible.models.*;

import java.util.ArrayList;
import java.util.List;

public class BibleReader {
    private final SQLiteDatabase db;

    public BibleReader(SQLiteDatabase db){
        this.db = db;
    }

    public Bible readFullBible(){
        Bible bible = new Bible();
        bible.name = "Ыйык Жазуу";
        bible.books = readBooks();

        return bible;
    }

    private List<BibleBook> readBooks() {
        List<BibleBook> books = new ArrayList<BibleBook>();
        for(BibleBook book: getBlankBooks()){
            book.chapters = readChapters(book);
            books.add(book);
        }

        return books;
    }

    private List<BibleChapter> readChapters(BibleBook book) {
        List<BibleChapter> chapters = new ArrayList<BibleChapter>();
        for(BibleChapter chapter: getBlankChapters(book)){
            chapter.headers = readHeaders(chapter);
            chapter.verses = collectVerses(chapter.headers);
            chapters.add(chapter);
        }

        return chapters;
    }

    private List<BibleHeader> readHeaders(BibleChapter chapter) {
        List<BibleHeader> headers = new ArrayList<BibleHeader>();
        for(BibleHeader header: getBlankHeaders(chapter)){
            header.verses = readVerses(header);
            headers.add(header);
        }

        return headers;
    }

    private List<BibleVerse> readVerses(BibleHeader header) {
        List<BibleVerse> verses = new ArrayList<BibleVerse>();
        for(BibleVerse verse: getVerses(header)){
            verses.add(verse);
        }

        return verses;
    }

    public Bible getBlankBible(){
        Bible bible = new Bible();
        bible.name = "Ыйык Жазуу";
        bible.books = getBlankBooks();

        return bible;
    }

    public List<BibleVerse> getVerses(BibleHeader header) {
        List<BibleVerse> verses = new ArrayList<BibleVerse>();
        Cursor cursor = db.query("verses", new String[]{"id", "verse"},
                " header_id = ? ", // The columns for the WHERE clause
                new String[]{""+header.id}, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while(cursor.moveToNext()) {
            BibleVerse verse= new BibleVerse();
            verse.id = cursor.getInt(cursor.getColumnIndex("id"));
            verse.text = cursor.getString(cursor.getColumnIndex("verse"));
            verses.add(verse);
        }
        cursor.close();
        return verses;
    }

    public List<BibleVerse> getVerses(BibleChapter chapter) {
        List<BibleVerse> verses = new ArrayList<BibleVerse>();
        Cursor cursor = db.query("verses", new String[]{"id", "verse", "number", "header_id"},
                " chapter_id = ? ", // The columns for the WHERE clause
                new String[]{""+chapter.id}, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while(cursor.moveToNext()) {
            BibleVerse verse= new BibleVerse();
            verse.id = cursor.getInt(cursor.getColumnIndex("id"));
            verse.number = cursor.getInt(cursor.getColumnIndex("number"));
            verse.text = cursor.getString(cursor.getColumnIndex("verse"));
            verse.header_id = cursor.getInt(cursor.getColumnIndex("header_id"));
            verses.add(verse);
        }
        cursor.close();
        return verses;
    }

    public List<BibleHeader> getBlankHeaders(BibleChapter chapter) {
        List<BibleHeader> headers = new ArrayList<BibleHeader>();
        Cursor cursor = db.query("headers", new String[]{"id", "name", "number"},
                " chapter_id = ? ", // The columns for the WHERE clause
                new String[]{""+chapter.id}, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while(cursor.moveToNext()) {
            BibleHeader header= new BibleHeader();
            header.id = cursor.getInt(cursor.getColumnIndex("id"));
            header.name = cursor.getString(cursor.getColumnIndex("name"));
            header.number = cursor.getInt(cursor.getColumnIndex("number"));
            headers.add(header);
        }
        cursor.close();
        return headers;
    }

    public List<BibleChapter> getBlankChapters(BibleBook book) {
        List<BibleChapter> chapters = new ArrayList<BibleChapter>();
        Cursor cursor = db.query("chapters", new String[]{"id", "name", "number"},
                " book_id = ? ", // The columns for the WHERE clause
                new String[]{""+book.id}, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while(cursor.moveToNext()) {
            BibleChapter chapter= new BibleChapter();
            chapter.id = cursor.getInt(cursor.getColumnIndex("id"));
            chapter.name = cursor.getString(cursor.getColumnIndex("name"));
            chapter.number = cursor.getInt(cursor.getColumnIndex("number"));
            chapters.add(chapter);
        }
        cursor.close();
        return chapters;
    }

    public List<BibleBook> getBlankBooks() {
        List<BibleBook> books = new ArrayList<BibleBook>();
        Cursor cursor = db.query("books", new String[]{"id", "name"},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while(cursor.moveToNext()) {
            BibleBook book= new BibleBook();
            book.id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            book.name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();;
            books.add(book);
        }
        cursor.close();
        return books;
    }

    public List<BibleVerse> collectVerses(List<BibleHeader> headers) {
        List<BibleVerse> verses = new ArrayList<BibleVerse>();
        for(BibleHeader header:headers)
            verses.addAll(header.verses);
        return verses;
    }
}
