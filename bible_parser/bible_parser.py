import os
from models import *
from parsers import BibleBookParser


class BibleParser(object):
    books = []

    def __init__(self, name, directory):
        self.directory = directory
        self.name = name

    def parse(self):
        self._assert_files_exist()
        self._parse_book_files()
        bible = Bible(self.name)
        bible.books = self.books
        return bible

    def _assert_files_exist(self):
        assert self.directory is not None, "Directory was not set"
        for book_file_path in self._range_book_files():
            assert os.path.exists(book_file_path), "File %s doesn't exist" % book_file_path

    def _range_book_files(self):
        if self.directory[-1] != "/":
            self.directory += "/"

        for i in range(1, 67):
            yield "%s%02d_.html" % (self.directory, i)

    def _parse_book_files(self):
        for book_file_path in self._range_book_files():
            book_file = open(book_file_path)
            book_raw = book_file.read()
            print "Start parsing %s " % book_file_path
            self.books.append(self._parse_book(book_raw))

    def _parse_book(self, book_raw):
        book_parser = BibleBookParser(book_raw)
        return book_parser.parse()

