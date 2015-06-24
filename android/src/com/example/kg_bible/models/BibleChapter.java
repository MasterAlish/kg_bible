package com.example.kg_bible.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BibleChapter {
    public int id;
    public String name;
    public int number;
    public List<BibleHeader> headers;
    public List<BibleVerse> verses;

    public void spreadVersesByHeaders() {
        HashMap<Integer, BibleHeader> headersById = new HashMap<Integer, BibleHeader>();
        for(BibleHeader header:headers) {
            headersById.put(header.id, header);
            header.verses = new ArrayList<BibleVerse>();
        }

        for(BibleVerse verse: verses){
            headersById.get(verse.header_id).verses.add(verse);
        }
    }
}
