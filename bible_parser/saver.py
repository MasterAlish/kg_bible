import sqlite3


class BibleSqliteSaver(object):
    def __init__(self, bible, db_name):
        self.bible = bible
        self.conn = sqlite3.connect(db_name)

    def save(self):
        self._init_tables()
        for book in self.bible.books:
            book_id = self.conn.execute("INSERT INTO 'books' (name) values ('%s')" % book.name).lastrowid
            for chapter in book.chapters:
                chapter_id = self.conn.execute("INSERT INTO 'chapters' (name, book_id) values ('%s', '%d')" %
                                               (chapter.name, book_id)).lastrowid
                for header in chapter.headers:
                    header_id = self.conn.execute("INSERT INTO 'headers' (name, chapter_id) values ('%s', '%d')" %
                                                  (header.name, chapter_id)).lastrowid
                    for verse in header.verses:
                        try:
                            self.conn.execute("INSERT INTO 'verses' (verse, header_id, chapter_id) values ('%s', '%d', '%d')" %
                                              (verse.text.replace("'", "\""), header_id, chapter_id))
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
            (id INTEGER PRIMARY KEY, name TEXT, book_id INTEGER)''')
        self.conn.execute(''' CREATE TABLE headers
            (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, chapter_id v)''')
        self.conn.execute(''' CREATE TABLE verses
            (id INTEGER PRIMARY KEY, verse TEXT, header_id INTEGER, chapter_id INTEGER)''')