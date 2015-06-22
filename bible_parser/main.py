# coding=utf-8
from bible_parser import BibleParser
from saver import BibleSqliteSaver

parser = BibleParser("Кыргыз Библия", directory="/Users/alisher/projects/kg_bible/bible_parser/raw")
bible = parser.parse()
saver = BibleSqliteSaver(bible, db_name="kg_bible.db")
saver.save()
