import cs50
import csv

from flask import Flask, jsonify, redirect, render_template, request

# Configure application
app = Flask(__name__)

# Reload templates when they are changed
app.config["TEMPLATES_AUTO_RELOAD"] = True


@app.after_request
def after_request(response):
    """Disable caching"""
    response.headers["Cache-Control"] = "no-cache, no-store, must-revalidate"
    response.headers["Expires"] = 0
    response.headers["Pragma"] = "no-cache"
    return response


@app.route("/", methods=["GET"])
def get_index():
    return redirect("/form")


@app.route("/form", methods=["GET"])
def get_form():
    return render_template("form.html")


@app.route("/form", methods=["POST"])
def post_form():
    # validating form input
    if not request.form.get("username"):
        return render_template("error.html", message="Missing Username")
    if not request.form.get("code"):
        return render_template("error.html", message="Missing Friend Code")
    if not request.form.get("phone"):
        return render_template("error.html", message="Missing Phone Number")
    if not request.form.get("team"):
        return render_template("error.html", message="Missing Team")
    if not request.form.get("bv") and not request.form.get("ufpe"):
        return render_template("error.html", message="Missing Username")

    # writing csv
    with open("survey.csv", "a") as file:
        writer = csv.DictWriter(file, fieldnames=["username", "code", "phone", "team", "bv", "ufpe"])
        # writing form input as rows in csv file
        writer.writerow({"username": request.form.get("username"), "code": request.form.get("code"), "phone": request.form.get(
            "phone"), "team": request.form.get("team"), "bv": request.form.get("bv"), "ufpe": request.form.get("ufpe")})
    return redirect("/sheet")


@app.route("/sheet", methods=["GET"])
def get_sheet():
    # reading csv
    with open("survey.csv", "r") as file:
        reader = csv.DictReader(file)
        submissions = list(reader)
        print(submissions)
    return render_template("sheet.html", submissions=submissions)

