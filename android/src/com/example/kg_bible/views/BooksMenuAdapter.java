package com.example.kg_bible.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.kg_bible.R;
import com.example.kg_bible.models.Bible;

public class BooksMenuAdapter extends ArrayAdapter{
    private Bible bible = null;

    public BooksMenuAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void setBible(Bible bible) {
        this.bible = bible;
    }

    @Override
    public int getCount() {
        if(bible == null)
            return 0;
        return bible.books.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.book_menu_item, null);

        TextView textView = (TextView) view.findViewById(R.id.book_menu_item_book_name);
        textView.setText(bible.books.get(position).name);
        return view;
    }
}
