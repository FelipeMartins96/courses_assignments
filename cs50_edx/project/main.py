from bs4 import BeautifulSoup
from cs50 import SQL
import requests
import smtplib
import json
import time
import re

class Adlisting():
    listId = None
    brand = None
    version = None
    regdate = None
    mileage = None
    price = None
    adDate = None
    state = None
    zipcode = None
    gearbox = None
    history = None
    link = None
    old_price = 0

updated_ads = []

gmail_user = '**********'
gmail_password = '*********'
headers = {'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36'}

# Configure CS50 Library to use SQLite database
db = SQL("sqlite:///data.db")

def send_notification(email_send_address):
    #email properties
    sent_from = gmail_user
    subject = 'Alert for updated ads'
    email_text = ""

    for update in updated_ads:
        if update.history == 1:
            email_text = email_text + """
            -------------------------------------------------------------------------------------------
            Price chaged from R${0} to R${1}
            {2} {3}, {4}
            {5} km, R${1}
            {6}
            """.format(update.old_price, update.price, update.brand, update.version, update.regdate, update.mileage, update.link)
        else:
            email_text = email_text + """
            -------------------------------------------------------------------------------------------
            New ad
            {0} {1}, {2}
            {3} km, R${4}
            {5}
            """.format(update.brand, update.version, update.regdate, update.mileage, update.price, update.link)

    print(email_text)
    #email send request
    try:
        server = smtplib.SMTP_SSL('smtp.gmail.com', 465)
        server.ehlo()
        server.login(gmail_user, gmail_password)
        server.sendmail(sent_from, email_send_address, email_text)
        server.close()

        print ('Email sent!')
    except Exception as e:
        print(e)
        print ('Something went wrong...')

def store(url):
    source = requests.get(url, headers=headers)
    soup = BeautifulSoup(source.text, 'html.parser')
    page_data = soup.find(string = re.compile('window.dataLayer')).split(' = ')[1]
    page_data = json.loads(page_data)[0]

    ad = Adlisting()

    ad.listId = page_data['page']['adDetail'].get('listId')
    ad.brand = page_data['page']['adDetail'].get('brand')
    ad.version = page_data['page']['adDetail'].get('version')
    ad.regdate = page_data['page']['adDetail'].get('regdate')
    ad.mileage = page_data['page']['adDetail'].get('mileage')
    ad.price = page_data['page']['adDetail'].get('price')
    ad.adDate = page_data['page']['detail'].get('adDate')
    ad.state = page_data['page']['adDetail'].get('state')
    ad.zipcode = page_data['page']['detail'].get('zipcode')
    ad.gearbox = page_data['page']['adDetail'].get('gearbox')
    ad.doors = page_data['page']['adDetail'].get('doors')
    ad.history = 0
    ad.link = url

    # Checks if entry already exists
    query = db.execute(
        "SELECT * FROM database WHERE listId = :listId ORDER BY id DESC", listId = ad.listId
        )


    if len(query):
        if len(query) > 1:
            ad.history = 1
        if str(query[0].get('price')) == ad.price and str(query[0].get('mileage')) == ad.mileage:
            db.execute(
                "UPDATE database SET active = 1 WHERE id = :id", id = query[0].get('id')
                )
            return
        if str(query[0].get('price')) != ad.price:
            ad.old_price = query[0].get('price')

    db.execute(
        "INSERT INTO database (listId, brand, version, regdate, mileage, price, adDate, state, zipcode, gearbox, doors, link, active, history)"
        "VALUES (:listId, :brand, :version, :regdate, :mileage, :price, :adDate, :state, :zipcode, :gearbox, :doors, :link, 1, :history)",
        listId = ad.listId, brand = ad.brand, version = ad.version, regdate = ad.regdate, mileage = ad.mileage, price = ad.price,
        adDate = ad.adDate, state = ad.state, zipcode = ad.zipcode, gearbox = ad.gearbox, doors = ad.doors, link = ad.link, history = ad.history
        )

    updated_ads.append(ad)




def scrape(url):
    source = requests.get(url, headers=headers)
    soup = BeautifulSoup(source.text, 'html.parser')
    for link in soup.find_all('a', class_='OLXad-list-link'):
        time.sleep(1)
        store(link.get('href'))
    next_link = soup.find('a', rel = 'next')
    if next_link:
        scrape(next_link.get('href'))


if __name__ == '__main__':
    to = None
    notification = False
    with open('settings.txt', 'r+') as file:
        settings = json.loads(file.read())
        to = settings['email']
        notification = settings['notification']
        if settings['current_url'] != settings['new_url']:
            db.execute(
                "DROP TABLE database"
                )
            db.execute(
                "CREATE TABLE 'database' ('id' integer PRIMARY KEY AUTOINCREMENT NOT NULL,"
                "'timestamp' timestamp DEFAULT CURRENT_TIMESTAMP, 'listId' integer, 'brand'"
                "varchar(32), 'version' varchar(128), 'regdate' integer, 'mileage' integer,"
                "'price' integer, 'adDate' timestamp, 'state' char(2), 'zipcode' char(8),"
                "'gearbox' char(16), 'doors' char(8), 'link' varchar(256), 'active' boolean)"
                )
            settings['current_url'] = settings['new_url']
            notification = False
            file.seek(0)
            file.write(json.dumps(settings))
            file.truncate()
        else:
            db.execute(
            "UPDATE database set active = 0"
            )

    scrape(settings['current_url'])

    if len(updated_ads) and notification:
        send_notification(to)