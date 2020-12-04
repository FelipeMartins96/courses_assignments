import os

from cs50 import SQL
from flask import Flask, flash, jsonify, redirect, render_template, request, session
from flask_session import Session
from tempfile import mkdtemp
from werkzeug.exceptions import default_exceptions, HTTPException, InternalServerError
from werkzeug.security import check_password_hash, generate_password_hash

from helpers import apology, login_required, lookup, usd

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
app.jinja_env.filters["usd"] = usd

# Configure session to use filesystem (instead of signed cookies)
app.config["SESSION_FILE_DIR"] = mkdtemp()
app.config["SESSION_PERMANENT"] = False
app.config["SESSION_TYPE"] = "filesystem"
Session(app)

# Configure CS50 Library to use SQLite database
db = SQL("sqlite:///finance.db")

# Make sure API key is set
if not os.environ.get("API_KEY"):
    raise RuntimeError("API_KEY not set")


@app.route("/")
@login_required
def index():
    """Show portfolio of stocks"""
    sum = 0

    # Query for user stocks
    rows = db.execute(
        "SELECT * FROM database WHERE id = :id ORDER BY symbol ASC",
        id=session["user_id"]
    )

    # Quote for stocks prices

    for row in rows:
        quote = lookup(row["symbol"])
        row.update(quote)
        total = row["price"] * row["shares"]
        sum += total
        row["total"] = usd(total)
        row["price"] = usd(row["price"])

    # Query for user cash
    cash = db.execute("SELECT cash FROM users WHERE id = :id", id=session["user_id"])
    sum += cash[0]["cash"]
    cash = usd(cash[0]["cash"])

    # Render index template using jinja
    return render_template("index.html", portfolio=rows, cash=cash, total=usd(sum))


@app.route("/buy", methods=["GET", "POST"])
@login_required
def buy():
    """Buy shares of stock"""

    # User reached route via POST (as by submitting a form via POST)
    if request.method == "POST":

        # Ensure symbol was submitted
        if not request.form.get("symbol"):
            return apology("must provide a symbol", 400)

        # Lookup quote for symbol
        quote = lookup(request.form.get("symbol"))
        if not quote:
            return apology("invalid symbol", 400)

        # Checks if quantity of shares is a positive integer
        if not request.form.get("shares").isdigit():
            return apology("Invalid quantity of shares", 400)
        qty = int(request.form.get("shares"))

        # Query database and checks if user has enough cash
        cash = db.execute("SELECT cash FROM users WHERE id = :id", id=session["user_id"])
        balance = cash[0]["cash"] - (quote["price"] * qty)
        if balance >= 0:
            # Update user balance
            db.execute(
                "UPDATE users SET cash = :balance WHERE id = :id", balance=balance,
                id=session["user_id"]
            )

            # Checks if user already have stocks
            shares = db.execute(
                "SELECT shares FROM portfolio WHERE id = :id AND symbol = :sym",
                id=session["user_id"], sym=quote["symbol"]
            )

            # If he already have stocks update values, else insert new value
            if len(shares):
                db.execute(
                    "UPDATE portfolio SET shares = shares + :qty WHERE id = :id AND symbol = :sym",
                    qty=qty, id=session["user_id"], sym=quote["symbol"]
                )
            else:
                db.execute(
                    "INSERT INTO portfolio (id, symbol, shares) VALUES(:id, :sym, :qty)",
                    id=session["user_id"], sym=quote["symbol"], qty=qty
                )

            # Add trasaction to database
            db.execute(
                "INSERT INTO transactions (user_id, symbol, shares, price, operation)"
                " VALUES (:id, :sym, :shares, :price, 'BUY')", id=session["user_id"],
                sym=quote["symbol"], shares=qty, price=quote["price"]
            )
        else:
            return apology("Insuficient funds", 403)

        # Redirect user to home page
        return redirect("/")

    # User reached route via GET (as by clicking a link or via redirect)
    else:
        return render_template("buy.html")


@app.route("/check", methods=["GET"])
def check():
    """Return true if username available, else false, in JSON format"""

    # Query Database For Username
    query = db.execute("SELECT id FROM users WHERE username = :username",
                       username=request.args.get("username"))

    if len(query) != 1:
        return jsonify(True)
    else:
        return jsonify(False)


@app.route("/history")
@login_required
def history():
    """Show history of transactions"""

    # Query for user's transactions
    rows = db.execute(
        "SELECT * FROM transactions WHERE user_id = :id ORDER BY transaction_id DESC",
        id=session["user_id"]
    )

    for row in rows:
        total = row["price"] * row["shares"]
        row["total"] = usd(total)
        row["price"] = usd(row["price"])

    return render_template("history.html", transactions=rows)


@app.route("/login", methods=["GET", "POST"])
def login():
    """Log user in"""

    # Forget any user_id
    session.clear()

    # User reached route via POST (as by submitting a form via POST)
    if request.method == "POST":

        # Ensure username was submitted
        if not request.form.get("username"):
            return apology("must provide username", 403)

        # Ensure password was submitted
        elif not request.form.get("password"):
            return apology("must provide password", 403)

        # Query database for username
        rows = db.execute("SELECT * FROM users WHERE username = :username",
                          username=request.form.get("username"))

        # Ensure username exists and password is correct
        if len(rows) != 1 or not check_password_hash(rows[0]["hash"], request.form.get("password")):
            return apology("invalid username and/or password", 403)

        # Remember which user has logged in
        session["user_id"] = rows[0]["id"]

        # Redirect user to home page
        return redirect("/")

    # User reached route via GET (as by clicking a link or via redirect)
    else:
        return render_template("login.html")


@app.route("/logout")
def logout():
    """Log user out"""

    # Forget any user_id
    session.clear()

    # Redirect user to login form
    return redirect("/")


@app.route("/quote", methods=["GET", "POST"])
@login_required
def quote():
    """Get stock quote."""

    # User reached route via POST (as by submitting a form via POST)
    if request.method == "POST":

        # Ensure symbol was submitted
        if not request.form.get("symbol"):
            return apology("must provide a symbol", 400)

        # Lookup quote for symbol
        quote = lookup(request.form.get("symbol"))
        if not quote:
            return apology("invalid symbol", 400)

        return render_template("quoted.html", name=quote["name"], symbol=quote["symbol"],
                               price=usd(quote["price"]))

    # User reached route via GET (as by clicking a link or via redirect)
    else:
        return render_template("quote.html")


@app.route("/register", methods=["GET", "POST"])
def register():
    """Register user"""

    # Forget any user_id
    session.clear()

    # User reached route via POST (as by submitting a form via POST)
    if request.method == "POST":

        # Ensure username was submitted
        if not request.form.get("username"):
            return apology("must provide username", 400)

        # Ensure password was submitted
        elif not request.form.get("password"):
            return apology("must provide password", 400)

        # Ensure password confirmation was submitted
        elif not request.form.get("confirmation"):
            return apology("must provide password confirmation", 400)

        # Ensure password and confirmation match
        elif request.form.get("password") != request.form.get("confirmation"):
            return apology("Passwords must match", 400)

        # Hash password
        hash = generate_password_hash(request.form.get("password"))

        # Insert user in database
        id = db.execute("INSERT INTO users (username,hash) VALUES(:username, :hash)",
                        username=request.form.get("username"), hash=hash)

        # Ensure username not already registered
        if not id:
            return apology("Username already registered", 400)
        # Logs in and redirects to home page if registered successfully
        else:
            session["user_id"] = id
            return redirect("/")

    # User reached route via GET (as by clicking a link or via redirect)
    else:
        return render_template("register.html")


@app.route("/sell", methods=["GET", "POST"])
@login_required
def sell():
    """Sell shares of stock"""

    # User reached route via POST (as by submitting a form via POST)
    if request.method == "POST":

        # Ensure symbol was submitted
        if not request.form.get("symbol"):
            return apology("must provide a symbol", 400)

        # Checks if quantity of shares is a positive integer
        if not request.form.get("shares").isdigit():
            return apology("Invalid quantity of shares", 400)
        qty = int(request.form.get("shares"))

        # Query database and checks if user has enough shares
        stock = db.execute("SELECT shares FROM portfolio WHERE id = :id AND symbol = :sym",
                           id=session["user_id"], sym=request.form.get("symbol"))
        balance = stock[0]["shares"] - qty

        # If user has enough shares
        if balance >= 0:
            # Quote the stock and calculate the transaction value
            quote = lookup(request.form.get("symbol"))
            total = quote["price"] * qty

            # Updates portfolio
            if balance > 0:
                db.execute(
                    "UPDATE portfolio SET shares = shares - :qty WHERE id = :id AND symbol = :sym",
                    qty=qty, id=session["user_id"], sym=quote["symbol"]
                )
            elif balance == 0:
                db.execute(
                    "DELETE FROM portfolio WHERE id = :id AND symbol = :sym",
                    id=session["user_id"], sym=quote["symbol"]
                )
            else:
                return apology("error", 400)

            # Update his cash amount
            db.execute("UPDATE users SET cash = cash + :total WHERE id = :id",
                       id=session["user_id"], total=total)

            # Add trasaction to database
            db.execute(
                "INSERT INTO transactions (user_id, symbol, shares, price, operation)"
                " VALUES (:id, :sym, :shares, :price, 'SELL')", id=session["user_id"],
                sym=quote["symbol"], shares=qty, price=quote["price"]
            )

        else:
            return apology("Insuficient shares", 400)

        # Redirect user to home page
        return redirect("/")

    # User reached route via GET (as by clicking a link or via redirect)
    else:
        rows = db.execute("SELECT symbol FROM portfolio WHERE id = :id ORDER BY symbol ASC",
                          id=session["user_id"])
        print(rows)
        return render_template("sell.html", stocks=rows)


@app.route("/account", methods=["GET", "POST"])
@login_required
def account():
    """Change Password"""

    # User reached route via POST (as by submitting a form via POST)
    if request.method == "POST":

        # Ensure username was submitted
        if not request.form.get("old_password"):
            return apology("must provide password", 403)

        # Ensure password was submitted
        elif not request.form.get("new_password"):
            return apology("must provide new password", 403)

        # Ensure password confirmation was submitted
        elif not request.form.get("confirmation"):
            return apology("must provide password confirmation", 403)

        # Ensure password and confirmation match
        elif request.form.get("new_password") != request.form.get("confirmation"):
            return apology("Passwords must match", 403)

        # Query database for username
        rows = db.execute("SELECT * FROM users WHERE id = :id",
                          id=session["user_id"])

        # Ensure password is correct
        if not check_password_hash(rows[0]["hash"], request.form.get("old_password")):
            return apology("Wrong Password", 403)

        # Update Password
        hash = generate_password_hash(request.form.get("new_password"))
        db.execute("UPDATE users SET hash = :hash WHERE id = :id",
                   id=session["user_id"], hash=hash)

        return redirect("/")

    # User reached route via GET (as by clicking a link or via redirect)
    else:
        return render_template("account.html")


def errorhandler(e):
    """Handle error"""
    if not isinstance(e, HTTPException):
        e = InternalServerError()
    return apology(e.name, e.code)


# Listen for errors
for code in default_exceptions:
    app.errorhandler(code)(errorhandler)

