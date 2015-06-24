package com.example.kg_bible.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.example.kg_bible.R;
import com.example.kg_bible.models.BibleVerse;

public class VerseViewMaker {
    private final BibleVerse bibleVerse;
    private final Context context;

    public VerseViewMaker(Context context, BibleVerse bibleVerse){
        this.context = context;
        this.bibleVerse = bibleVerse;
    }

    public View getView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.verse, null);
        TextView numberTextView = (TextView) view.findViewById(R.id.verse_number);
        numberTextView.setText(bibleVerse.number+"");

        TextView verseTextView = (TextView) view.findViewById(R.id.verse_text);
        verseTextView.setText((bibleVerse.number<10?"    ":"      ")+ bibleVerse.text);
        return view;
    }
}
