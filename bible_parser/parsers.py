# coding=utf-8
import re
from models import BibleBook, BibleChapter, BibleHeader, BibleVerse


class BibleBookParser(object):
    last_index = 0

    def __init__(self, raw_book):
        self.raw_book = raw_book

    def parse(self):
        name = self._parse_name()
        chapters = self._parse_chapters()
        book = BibleBook(name)
        book.chapters = chapters
        return book

    def _parse_name(self):
        pattern = re.compile("<h1>(.*)</h1>")
        match = re.search(pattern, self.raw_book)
        assert match is not None, "Cannot find book title"
        self.last_index = match.regs[0][1]
        name = self._fix_kg_letters(match.groups()[0])
        return name

    def _parse_chapters(self):
        chapters = []
        i = 1
        while True:
            chapter = self._parse_next_chapter(i)
            if chapter is None:
                break
            chapters.append(chapter)
            i += 1
        return chapters

    def _parse_next_chapter(self, number):
        pattern = re.compile("<h2>(.*)</h2>")
        match = pattern.search(self.raw_book, self.last_index)
        if match is not None:
            self.last_index = match.regs[0][1]
            name = self._fix_kg_letters(match.groups()[0])
            chapter = BibleChapter(name, number)
            chapter.headers = self._parse_headers_and_verses()
            return chapter
        return None

    def _parse_headers_and_verses(self):
        headers = []
        while True:
            header = self._parse_next_header()
            if header is None:
                break
            headers.append(header)
        return headers

    def _parse_next_header(self):
        verses = []
        name = ""
        header_pattern = re.compile("<h3>(.*)</h3>")
        verse_pattern = re.compile("<p>(.*)\n")
        while True:
            h3_index = self.index_of("<h3>", self.last_index)
            p_index = self.index_of("<p>", self.last_index)
            td_index = self.index_of("<td>", self.last_index)
            h2_index = self.index_of("<h2>", self.last_index)
            end_index = td_index if td_index < h2_index else h2_index
            if h3_index <= p_index and h3_index < end_index:
                if len(name) > 0:
                    break
                match = header_pattern.search(self.raw_book, self.last_index)
                if match is not None:
                    self.last_index = match.regs[0][1]
                    name = self._fix_kg_letters(match.groups()[0])

            elif p_index < h3_index and p_index < end_index:
                match = verse_pattern.search(self.raw_book, self.last_index)
                if match is not None:
                    verse_raw = self._fix_kg_letters(match.groups()[0])

                    header_match = header_pattern.search(verse_raw)
                    if header_match is not None:
                        if len(name) > 0:
                            break
                        self.last_index = match.regs[0][1]
                        name = self._fix_kg_letters(match.groups()[0])
                        continue

                    self.last_index = match.regs[0][1]
                    verses.append(self._parse_verse(verse_raw))

            else:
                break
        if len(verses) > 0:
            header = BibleHeader(name)
            header.verses = verses
            return header
        return None

    def _parse_verse(self, verse_raw):
        pattern = re.compile("(\d+)\s+(.+)$")
        match = pattern.search(verse_raw)
        assert match is not None, "Verse \n>(%s)\n>near (%s) cannot be parsed" % (verse_raw, self.raw_book[self.last_index-20:self.last_index+20])
        number = match.groups()[0]
        text = self._fix_kg_letters(match.groups()[1])
        return BibleVerse(text, number)

    def _fix_kg_letters(self, text):
        result = text.replace("&#1256;", "Ө")
        result = result.replace("&#1257;", "ө")
        result = result.replace("&#1198;", "Ү")
        result = result.replace("&#1199;", "ү")
        result = result.replace("&#1186;", "Ң")
        result = result.replace("&#1187;", "ң")
        return result

    def index_of(self, substr, last_index):
        try:
            return self.raw_book.index(substr, last_index)
        except ValueError:
            return 999999999