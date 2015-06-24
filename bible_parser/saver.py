import sqlite3


class BibleSqliteSaver(object):
    def __init__(self, bible, db_name):
        self.bible = bible
        self.conn = sqlite3.connect(db_name)

    def save(self):
        self._init_tables()
        for book in self.bible.books:
            book_id = self.conn.execute("INSERT INTO 'books' (name) values ('%s')" % book.name).lastrowid
            chapter_number = 0
            for chapter in book.chapters:
                chapter_number += 1
                chapter_id = self.conn.execute("INSERT INTO 'chapters' (name, book_id, number) values ('%s', '%d', '%d')" %
                                               (chapter.name, book_id, chapter_number)).lastrowid
                header_number = 0
                verse_number = 0
                for header in chapter.headers:
                    header_number += 1
                    header_id = self.conn.execute("INSERT INTO 'headers' (name, chapter_id, number) values ('%s', '%d', '%d')" %
                                                  (header.name, chapter_id, header_number)).lastrowid
                    for verse in header.verses:
                        verse_number += 1
                        try:
                            self.conn.execute("INSERT INTO 'verses' (verse, header_id, chapter_id, number) values ('%s', '%d', '%d', '%d')" %
                                              (verse.text.replace("'", "\""), header_id, chapter_id, verse_number))
                        except sqlite3.OperationalError as e:
                            print "Book %s chapter %d verse %s" %(book.name, chapter_id, verse.text)
                            raise e

        self.conn.commit()
        self.conn.close()

    def _init_tables(self):
        self.conn.execute("DROP TABLE IF EXISTS 'books'")
        self.conn.execute("DROP TABLE IF EXISTS 'chapters'")
        self.conn.execute("DROP TABLE IF EXISTS 'headers'")
        self.conn.execute("DROP TABLE IF EXISTS 'verses'")

        self.conn.execute('''CREATE TABLE books
            (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)''')
        self.conn.execute(''' CREATE TABLE chapters
            (id INTEGER PRIMARY KEY, number INTEGER, name TEXT, book_id INTEGER)''')
        self.conn.execute(''' CREATE TABLE headers
            (id INTEGER PRIMARY KEY AUTOINCREMENT, number INTEGER, name TEXT, chapter_id v)''')
        self.conn.execute(''' CREATE TABLE verses
            (id INTEGER PRIMARY KEY, number INTEGER, verse TEXT, header_id INTEGER, chapter_id INTEGER)''')