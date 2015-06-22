class Bible(object):
    books = []

    def __init__(self, name):
        self.name = name


class BibleBook(object):
    chapters = []

    def __init__(self, name):
        self.name = name


class BibleChapter(object):
    headers = []

    def __init__(self, name, number):
        self.name = name
        self.number = number


class BibleHeader(object):
    verses = []

    def __init__(self, name):
        self.name = name


class BibleVerse(object):

    def __init__(self, text, number):
        self.text = text
        self.number = number
