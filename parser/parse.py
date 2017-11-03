#!/usr/bin/env python
# -*- coding: utf-8 -*- 

from bs4 import BeautifulSoup
import re
import sqlite3
import psycopg2
from urllib.request import urlopen
from urllib.request import Request
from urllib.request import HTTPError

BASE_URL='http://www.viedemerde.fr'
DATABASE_PATH = '/Users/antoinesauray/Projects/Scala/test-backend-iadvize/vdm.db'

dic = {}

def query(url):
    req = Request(url, headers={'User-Agent' : "Magic Browser"}) 
    con = urlopen(req)
    return BeautifulSoup(con.read(), "html.parser")

def get_month_number(month):
    if month == 'janvier': 
        return '01'
    elif month == 'février':
        return '02'
    elif month == 'mars':
        return '03'
    elif month == 'avril':
        return '04'
    elif month =='mai':
        return '05'
    elif month == 'juin':
        return '06'
    elif month == 'juillet':
        return '07'
    elif month == 'août':
        return '08'
    elif month == 'septembre':
        return '09'
    elif month == 'octobre':
        return '10'
    elif month == 'novembre':
        return '11'
    elif month == 'décembre':
        return '12'
    else:
        print("failed for " + month)
        return '__'

def get_all_without_duplicates(count, insert_in_db=True):
    page_number = 0
    i = 0
    ret = list()
    if insert_in_db:
        try:
            conn = psycopg2.connect("dbname='docker' user='docker' host='127.0.0.1' password='docker' port='5554'")
            c = conn.cursor()
        except:
            print("Connection failed")
            exit(1)
    while i < count:
        soup = query(BASE_URL+'/?page='+str(page_number))
        for a in soup.find_all('a'):
            href = a['href']
            id = re.findall(r'\d+', href)
            if len(id)>0 and id[0] not in dic:
                obj = {}
                dic[id[0]] = obj    
                if href.startswith('/article/'):
                    try:
                        soup = query(BASE_URL + href)
                        author = ''
                        content = ''
                        date = ''
                        for b in soup.find_all('i', {'class': "fa fa-female"}):
                            split = b.parent.contents[2].replace('/', '').replace('\n', '').split(' ')
                            day_of_month = str(split[2])
                            if int(split[2]) < 10:
                                day_of_month = '0' + day_of_month
                            date = split[4] +'-' + get_month_number(split[3]) + '-'+day_of_month+'T'+split[5]+':00'
                            obj['date'] = date
                        for meta in soup.findAll("meta"):
                            metaname = meta.get('name', '').lower()
                            metaprop = meta.get('content', '')
                            if metaname == 'author':
                                obj['author'] = metaprop
                                author = metaprop
                            elif metaname == 'description':
                                obj['content'] = metaprop
                                content = metaprop
                        if author != '' and content != '' and date != '':
                            ret.append(obj)
                            if insert_in_db:
                                c.execute("INSERT INTO posts(author, content, created_at) VALUES (%(author)s, %(content)s, %(created_at)s)", {"author": author, "content": content, "created_at": date})
                                conn.commit()
                            i += 1
                            print(str(float(i)/float(count)*100) +"%")
                        if i == count:
                            break
                    except HTTPError:
                        pass
        page_number+=1
    if insert_in_db:
        conn.close()
    return ret


get_all_without_duplicates(200, True)
print("finished")
    
