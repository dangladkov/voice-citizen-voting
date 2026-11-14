"""Fill a local Voice install with demo data through its own REST API.

Runs against http://localhost:8080. Accounts and passwords are local demo values only.
"""
import json
import os
import subprocess
import urllib.request

BASE = "http://localhost:8080/api/"
PASSWORD = "VoiceDemo2023!"


def call(path, body=None, cookie=None, method=None):
    data = json.dumps(body).encode() if body is not None else None
    req = urllib.request.Request(BASE + path, data=data, method=method or ("POST" if data else "GET"))
    if data is not None:
        req.add_header("Content-Type", "application/json")
    if cookie:
        req.add_header("Cookie", "Authentication=" + cookie)
    with urllib.request.urlopen(req) as r:
        raw = r.read().decode()
        return json.loads(raw) if raw.strip().startswith(("{", "[")) or raw.strip().isdigit() else raw


def signup(name, surname, phone, email):
    msg = call("user/sign-up", {"name": name, "surname": surname, "phoneNumber": phone, "email": email, "pwd": PASSWORD})
    return "token:/%s/userID:/%d" % (msg["token"], msg["userID"]), msg["userID"]


def sql(statement):
    subprocess.run(["psql", "-h", "127.0.0.1", "-U", "voice", "-d", "voice", "-qc", statement],
                   env={**os.environ, "PGPASSWORD": "voice-local"}, check=True)


# The first account to sign up becomes the administrator.
admin, admin_id = signup("Dan", "Gladkov", "+31600000001", "admin@voice.local")
citizens = []
for i, (first, last) in enumerate([("Sanne", "de Vries"), ("Lars", "Bakker"), ("Emma", "Visser"),
                                   ("Noah", "Smit"), ("Julia", "Meijer"), ("Daan", "Mulder"),
                                   ("Sophie", "de Boer"), ("Milan", "Bos")]):
    citizens.append(signup(first, last, "+3161000000%d" % i, "%s@voice.local" % first.lower()))

SUPPORT = [{"text": "Do you support this project?", "choices": [{"text": "Support"}, {"text": "Oppose"}]}]
projects = [
    ("Greenfest: Environmental Fun!",
     "A lively festival promoting environmental protection through engaging activities and exhibits, with sack races and workshops for all ages.",
     "Volkspark, Enschede", "Social|Environment|", "2027-06-30", SUPPORT, 40),
    ("Underground aquarium for new fish",
     "Introducing an innovative municipal project: an underground public aquarium inspired by the recent discovery of a captivating new fish species.",
     "Oude Markt, Enschede", "Culture|Tourism|Leisure|", "2027-07-15",
     [{"text": "Which entrance location do you prefer?", "choices": [{"text": "Oude Markt"}, {"text": "Van Heekplein"}, {"text": "Station square"}]},
      {"text": "Should entry be free for residents?", "choices": [{"text": "Yes"}, {"text": "No"}]}], 150),
    ("Statue park renovation",
     "Renovating the statue park with new paths, lighting and benches so it becomes a pleasant place to walk in the evening.",
     "Hengelosestraat, Enschede", "Culture|Infrastructure|", "2023-06-01", SUPPORT, 60),
    ("Community picnic area",
     "A shared picnic area with tables and a barbecue spot next to the park, maintained together with the neighbourhood.",
     "Ledeboerpark, Enschede", "Social|Leisure|", "2023-05-20",
     [{"text": "What should the picnic area include?", "choices": [{"text": "Barbecue spot"}, {"text": "Playground"}, {"text": "Both"}]}], 30),
    ("Bottle rocket workshop for kids",
     "Weekend science workshops where children build and launch water bottle rockets with volunteers from the university.",
     "Campus, Enschede", "Education|Youth|", "2027-05-01", SUPPORT, 25),
]
project_ids = []
for title, desc, loc, cats, deadline, questions, points in projects:
    pid = call("project/submit", {"ideaID": -1, "title": title, "description": desc, "location": loc,
                                   "date": "2023-06-15", "categories": cats, "votingDeadline": deadline,
                                   "questions": questions, "pointsToAward": points}, admin)
    project_ids.append(pid)

# Votes from citizens, so results charts have data.
for n, (cookie, _) in enumerate(citizens):
    for pid in project_ids:
        project = call("project/%d" % pid, cookie=cookie)
        answers = []
        for k, q in enumerate(project["questions"]):
            options = [a for a in q["answers"].split("|") if a]
            answers.append({"votingQuestionID": q["questionID"], "choice": options[(n + k + pid) % len(options)]})
        if (n + pid) % 4 != 0:  # not everyone votes on everything
            call("project/%d/cast" % pid, {"answers": answers}, cookie)

ideas = [
    ("More bike parking at the station", "The racks at Enschede station are always full. Add a second covered bike parking next to the bus platform.", "Stationsplein, Enschede"),
    ("Outdoor chess tables", "Place a few concrete chess tables in the park so people can play outside in summer.", "Volkspark, Enschede"),
    ("Monthly repair cafe", "A monthly repair cafe in the library where volunteers help fix clothes, bikes and electronics.", "Library, Enschede"),
    ("Night bus to the campus", "A late night bus line between the city centre and the university campus on weekends.", "Campus, Enschede"),
]
for i, (title, desc, loc) in enumerate(ideas):
    call("ideas/submit-idea", {"title": title, "description": desc, "location": loc, "date": "2023-06-%02d" % (10 + i),
                               "proposerUserID": citizens[i][1]}, citizens[i][0])

news = [
    ("Voting is open for Greenfest", "Residents can now vote on the Greenfest proposal. Voting closes at the end of June."),
    ("Statue park renovation approved", "The statue park renovation passed the vote. Construction starts after the summer."),
    ("New rewards in the shop", "Spend your Voice points on a Voice T-shirt, a museum day pass or a free swim."),
]
for title, desc in news:
    call("news/submit", {"title": title, "description": desc}, admin)

for title, desc, pts, cat in [("Voice T-shirt", "Official Voice T-shirt, organic cotton.", 200, "Merchandise"),
                              ("Museum day pass", "One day entry to the Rijksmuseum Twenthe.", 300, "Culture"),
                              ("Free swim", "One free entry to the municipal swimming pool.", 120, "Sport")]:
    call("shop/add", {"title": title, "description": desc, "points": pts, "category": cat, "attributes": []}, admin)

# Finished projects and leaderboard variety (the app never refreshes statuses itself).
sql("UPDATE voice4.project SET projectstatus = 1 WHERE deadline < now();")
sql("UPDATE voice4.ideaproposal SET ideastatus = 1 WHERE title = 'Monthly repair cafe';")
sql("UPDATE voice4.ideaproposal SET ideastatus = 2 WHERE title = 'Night bus to the campus';")
for i, pts in enumerate([1240, 980, 860, 730, 610, 455, 390, 220]):
    sql("UPDATE voice4.user SET points = %d, total_points = %d WHERE phone_number = '+3161000000%d';" % (pts // 2, pts, i))
sql("UPDATE voice4.user SET anonymous = true WHERE phone_number = '+31610000003';")
sql("UPDATE voice4.user SET points = 1500, total_points = 1500 WHERE user_id = %d;" % admin_id)
print("seeded: %d projects, %d ideas, %d news, 3 benefits, %d users" % (len(project_ids), len(ideas), len(news), len(citizens) + 1))
