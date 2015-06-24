package com.example.kg_bible.new_techs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class NSListViewAdapter extends ArrayAdapter{
    static final int HEADER_ROW_INDEX = -1;

    class NSListSection{
        private View headerView = null;
        private int rowCount = 0;

        public NSListSection(View headerView, int rowCount){
            this.headerView = headerView;
            this.rowCount = rowCount;
        }

        public int getTotalRowCount(){
            return rowCount+(headerView==null?0:1);
        }
    }

    private final NSListViewDataSource dataSource;
    private List<NSListSection> sections;
    private List<View> rowViews;
    private List<NSIndexPath> indexPaths;
    private int totalRowCount;

    public NSListViewAdapter(Context context, NSListViewDataSource dataSource){
        super(context, 0);
        this.dataSource = dataSource;
        this.prepareRows();
    }

    private void prepareRows() {
        sections = new ArrayList<NSListSection>();
        indexPaths = new ArrayList<NSIndexPath>();
        totalRowCount = 0;
        for (int section=0; section<dataSource.getSectionsCount(); section++){
            View headerView = dataSource.getHeaderViewForSection(section);
            int rowCountForSection = dataSource.getRowCountForSection(section);
            NSListSection listSection = new NSListSection(headerView, rowCountForSection);
            sections.add(listSection);
            totalRowCount+=listSection.getTotalRowCount();
            if(headerView != null)
                indexPaths.add(new NSIndexPath(section, HEADER_ROW_INDEX));
            for(int rowIndex=0;rowIndex<rowCountForSection;rowIndex++)
                indexPaths.add(new NSIndexPath(section, rowIndex));
        }
    }

    @Override
    public int getCount() {
        return totalRowCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NSIndexPath indexPath = indexPaths.get(position);
        if(indexPath.row == HEADER_ROW_INDEX)
            return sections.get(indexPath.section).headerView;
        return dataSource.getViewForRowAtIndexPath(indexPath);
    }

    @Override
    public void notifyDataSetChanged() {
        prepareRows();
        super.notifyDataSetChanged();
    }
}
