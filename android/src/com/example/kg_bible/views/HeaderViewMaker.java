package com.example.kg_bible.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.example.kg_bible.R;
import com.example.kg_bible.models.BibleHeader;

public class HeaderViewMaker {
    private final Context context;
    private final BibleHeader bibleHeader;

    public HeaderViewMaker(Context context, BibleHeader bibleHeader) {
        this.context = context;
        this.bibleHeader = bibleHeader;
    }

    public View getView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.header, null);

        TextView headerTextView = (TextView) view.findViewById(R.id.header_text);
        headerTextView.setText(bibleHeader.name);
        return view;
    }
}
