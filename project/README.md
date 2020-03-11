# CS50 Ad Watcher
## Felipe Bezerra Martins, Recife, Brazil
I've recently been in the market looking to buy a car, and on that search, I saw myself repeatedly refreshing the classifieds website waiting for price updates and new ads to show up. For this project, I sought to improve this task by creating a notification system for listing updates and also gathering the available data in a way that facilitates further analysis which will aid in the negotiation of the sale.

The solution developed works by gathering data from the car listing website, storing it in a SQL database and displaying interactively on a website, it was made to work for car ads on http://www.olx.com.br, a brazilian listings website, and it's separated into two codes. How it works is explained below.

# main.py
This python script works by searching the website tasked for listings and requesting the addresses found, then it extracts the ad data from the requested HTML and stores it using a SQL database. This script is also responsible for notification via email for new listing and price updates found.

It uses python requests library to request the website HTML, BeautifulSoup and json library to extract the data from the HTML and SMTPlib for sending email notifications. This program needs a settings.txt file containing the URL it needs to search and the email for notification in a json data format.

It's supposed to be used with a task scheduler to constantly update the database, It's not supposed to work continuously as the database won't be fully readable as it's updating.

## requirements:
```
bs4
cs50
requests
smtplib
json
time
re
```

# application.py
This file serves a website using the flask framework, It displays the data stored on the SQL database using an interactive table. There is two pages the index where the data is displayed in form of a table, where you can also look at a individual listing registered updates and a settings page where it's possible to change the link which is being monitored, the email which will receive the notifications and activate or deactivate the notification.

The URL as mentioned need to be an http://www.olx.com.br query restricted to the car listings category.

## requirements:
```
os
cs50
flask
werkzeug
json
```