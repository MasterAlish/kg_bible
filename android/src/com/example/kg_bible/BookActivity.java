package com.example.kg_bible;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.kg_bible.db.BibleReader;
import com.example.kg_bible.db.SQLiteDelegate;
import com.example.kg_bible.db.SQLiteHelper;
import com.example.kg_bible.models.Bible;
import com.example.kg_bible.models.BibleBook;
import com.example.kg_bible.models.BibleChapter;
import com.example.kg_bible.models.BibleVerse;
import com.example.kg_bible.new_techs.NSIndexPath;
import com.example.kg_bible.new_techs.NSListViewAdapter;
import com.example.kg_bible.new_techs.NSListViewDataSource;
import com.example.kg_bible.views.HeaderViewMaker;
import com.example.kg_bible.views.VerseViewMaker;

public class BookActivity extends Activity implements SQLiteDelegate, NSListViewDataSource {
    public static Bible bible;
    private BibleChapter currentChapter;
    private ProgressDialog progress;
    private TextView bookNameView, bookVersionView;
    private ListView versesList;
    private NSListViewAdapter versesAdapter;
    private BibleReader bibleReader;
    private SwipeDetector swipeDetector;
    private SQLiteHelper helper;
    private int currentChapterIndex = 0;
    private int currentBookIndex = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book);
        initUI();
        progress = new ProgressDialog(this);
        helper = new SQLiteHelper(this, this);
    }

    private void initUI() {
        bookNameView = (TextView) findViewById(R.id.book_title_book_name);
        bookVersionView = (TextView) findViewById(R.id.book_title_book_version);
        versesList = (ListView) findViewById(R.id.book_content_list_view);
        versesAdapter = new NSListViewAdapter(this, this);
        versesList.setAdapter(versesAdapter);
        swipeDetector = new SwipeDetector();
        versesList.setOnTouchListener(swipeDetector);
        versesList.setOnItemClickListener(listener);
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            if (swipeDetector.swipeDetected()) {
                if (swipeDetector.getAction() == SwipeDetector.Action.RightToLeft) {
                    nextChapter();
                }else if (swipeDetector.getAction() == SwipeDetector.Action.LeftToRight) {
                    previousChapter();
                }
            }
        }
    };

    private void previousChapter(){
        if(currentChapterIndex>0) {
            currentChapterIndex--;
        }else if(currentBookIndex>0){
            currentBookIndex--;
            BibleBook book = bible.books.get(currentBookIndex);
            book.chapters = bibleReader.getBlankChapters(book);
            currentChapterIndex = book.chapters.size()-1;
        }else{
            return;
        };
        versesList.setLayoutAnimation(getLeftRightAnimationController(-1.0f));
        loadBibleChapter(currentBookIndex, currentChapterIndex);
    }

    private void nextChapter(){
        BibleBook book = bible.books.get(currentBookIndex);
        if(currentChapterIndex<book.chapters.size()-1) {
            currentChapterIndex++;
        }else if(currentBookIndex<bible.books.size()-1){
            currentBookIndex++;
            currentChapterIndex = 0;
        }else{
            return;
        }
        versesList.setLayoutAnimation(getLeftRightAnimationController(1.0f));
        loadBibleChapter(currentBookIndex, currentChapterIndex);
    }

    private LayoutAnimationController getLeftRightAnimationController(float initX) {
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, initX, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(300);

        return new LayoutAnimationController(animation, 0.02f);
    }

    @Override
    public void databaseOpening() {
        progress.setTitle("Loading");
        progress.setMessage("Database initialization...");
        progress.show();
    }

    @Override
    public void databaseOpened(SQLiteDatabase db) {
        progress.dismiss();
        bibleReader = new BibleReader(db);
        bible = bibleReader.getBlankBible();
        loadBibleChapter(currentBookIndex, currentChapterIndex);
    }

    private void loadBibleChapter(int book_index, int chapter_index) {
        BibleBook book = bible.books.get(book_index);
        book.chapters = bibleReader.getBlankChapters(book);
        currentChapter = book.chapters.get(chapter_index);
        currentChapter.headers = bibleReader.getBlankHeaders(currentChapter);
        currentChapter.verses = bibleReader.getVerses(currentChapter);
        currentChapter.spreadVersesByHeaders();
        versesAdapter.notifyDataSetChanged();
        versesList.setSelectionAfterHeaderView();
    }

    @Override
    public void databaseOpenError(String error) {
        progress.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT);
    }

    @Override
    public int getSectionsCount() {
        if(currentChapter == null)
            return 0;
        return currentChapter.headers.size();
    }

    @Override
    public View getHeaderViewForSection(int section) {
        return new HeaderViewMaker(this, currentChapter.headers.get(section)).getView();
    }

    @Override
    public int getRowCountForSection(int section) {
        if (currentChapter == null)
            return 0;
        return currentChapter.headers.get(section).verses.size();
    }

    @Override
    public View getViewForRowAtIndexPath(NSIndexPath indexPath) {
        BibleVerse bibleVerse = currentChapter.headers.get(indexPath.section).verses.get(indexPath.row);
        return new VerseViewMaker(this, bibleVerse).getView();
    }
}
