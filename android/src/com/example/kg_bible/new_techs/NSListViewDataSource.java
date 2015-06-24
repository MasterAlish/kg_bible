package com.example.kg_bible.new_techs;

import android.view.View;

public interface NSListViewDataSource {
    int getSectionsCount();
    View getHeaderViewForSection(int section);
    int getRowCountForSection(int section);
    View getViewForRowAtIndexPath(NSIndexPath indexPath);
}
