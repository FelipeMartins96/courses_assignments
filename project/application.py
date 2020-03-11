import os
from cs50 import SQL
from flask import Flask, flash, jsonify, redirect, render_template, request, session
from werkzeug.exceptions import default_exceptions, HTTPException, InternalServerError
import json

# Configure application
app = Flask(__name__)

# Ensure templates are auto-reloaded
app.config["TEMPLATES_AUTO_RELOAD"] = True

# Ensure responses aren't cached
@app.after_request
def after_request(response):
    response.headers["Cache-Control"] = "no-cache, no-store, must-revalidate"
    response.headers["Expires"] = 0
    response.headers["Pragma"] = "no-cache"
    return response

# Custom filter
# app.jinja_env.filters["brl"] = brl

# Configure CS50 Library to use SQLite database
db = SQL("sqlite:///data.db")

@app.route("/")
def index():
    """Show active ads"""
    # Query for user stocks
    rows = db.execute(
        "SELECT * FROM database WHERE active = 1")

    # Render index template using jinja
    return render_template("index.html", ads=rows)


@app.route("/history")
def history():
    rows = db.execute(
        "SELECT * FROM database WHERE listId = :listid", listid = request.args.get("listid")
        )
    """Show history of transactions"""
    return render_template("index.html", ads=rows)

@app.route("/settings", methods=["GET", "POST"])
def login():
    """Changes Settings"""
    with open('settings.txt', 'r+') as file:
        settings = json.loads(file.read())
        # User reached route via POST (as by submitting a form via POST)
        if request.method == "POST":


            # Ensure link was submitted
            if not request.form.get("link"):
                return apology("must provide link", 403)


            print(request.form.get("receiveupdate"))
            settings["new_url"] = request.form.get("link")
            settings["email"] = request.form.get("email")
            # Ensure email was submitted
            if request.form.get("receiveupdate"):
                settings["notification"] = True
            else:
                settings["notification"] = False

            file.seek(0)
            file.write(json.dumps(settings))
            file.truncate()

            # Redirect user to settings page
            return redirect("/settings")

        # User reached route via GET (as by clicking a link or via redirect)
        else:
            return render_template("settings.html", settings=settings)

def errorhandler(e):
    """Handle error"""
    if not isinstance(e, HTTPException):
        e = InternalServerError()
    return apology(e.name, e.code)


# Listen for errors
for code in default_exceptions:
    app.errorhandler(code)(errorhandler)
