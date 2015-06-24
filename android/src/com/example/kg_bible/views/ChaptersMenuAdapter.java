package com.example.kg_bible.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.kg_bible.R;
import com.example.kg_bible.models.BibleBook;

public class ChaptersMenuAdapter extends ArrayAdapter{
    private BibleBook book;

    public ChaptersMenuAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void setBook(BibleBook book) {
        this.book = book;
    }

    @Override
    public int getCount() {
        if(book == null)
            return 0;
        return book.chapters.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chapter_menu_item, null);

        TextView textView = (TextView) view.findViewById(R.id.chapter_menu_item_book_name);
        textView.setText(book.chapters.get(position).number+"");
        return view;
    }
}
