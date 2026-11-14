-- Voice database schema, rebuilt from the queries in the DAO classes.
DROP SCHEMA IF EXISTS voice4 CASCADE;
CREATE SCHEMA voice4;
SET search_path TO voice4;

CREATE TABLE "user" (
    user_id       SERIAL PRIMARY KEY,
    first_name    VARCHAR(256) NOT NULL,
    last_name     VARCHAR(256) NOT NULL,
    phone_number  VARCHAR(20)  NOT NULL UNIQUE,
    email         VARCHAR(256) NOT NULL UNIQUE,
    password_hash VARCHAR(256) NOT NULL,
    points        INT     NOT NULL DEFAULT 0,
    total_points  INT     NOT NULL DEFAULT 0,
    anonymous     BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE user_session (
    session_id SERIAL PRIMARY KEY,
    token      VARCHAR(500),
    issue_date TIMESTAMP,
    user_id    INT NOT NULL UNIQUE REFERENCES "user"(user_id) ON DELETE CASCADE
);

CREATE TABLE administrator (
    adminid INT PRIMARY KEY REFERENCES "user"(user_id) ON DELETE CASCADE
);

CREATE TABLE ideaproposal (
    ideaid      SERIAL PRIMARY KEY,
    title       TEXT NOT NULL,
    description TEXT NOT NULL,
    date        TIMESTAMP,
    location    TEXT,
    proposedby  INT REFERENCES "user"(user_id) ON DELETE SET NULL,
    ideastatus  INT NOT NULL DEFAULT 0
);

CREATE TABLE project (
    projectid     SERIAL PRIMARY KEY,
    ideaid        INT,
    title         TEXT,
    description   TEXT,
    date          DATE,
    location      TEXT,
    proposedby    INT REFERENCES "user"(user_id) ON DELETE SET NULL,
    deadline      TIMESTAMP,
    pointstoaward INT NOT NULL DEFAULT 0,
    projectstatus INT NOT NULL DEFAULT 0,
    categories    TEXT
);

CREATE TABLE question (
    questionid SERIAL PRIMARY KEY,
    projectid  INT NOT NULL REFERENCES project(projectid) ON DELETE CASCADE,
    title      TEXT,
    answers    TEXT
);

CREATE TABLE vote (
    vote_id    SERIAL PRIMARY KEY,
    userid     INT NOT NULL REFERENCES "user"(user_id) ON DELETE CASCADE,
    questionid INT NOT NULL REFERENCES question(questionid) ON DELETE CASCADE,
    choice     TEXT
);

CREATE TABLE news (
    newsid      SERIAL PRIMARY KEY,
    title       TEXT NOT NULL,
    description TEXT,
    userid      INT REFERENCES "user"(user_id) ON DELETE SET NULL
);

CREATE TABLE benefit (
    item_id     SERIAL PRIMARY KEY,
    name        VARCHAR(256),
    description TEXT,
    points      INT,
    category    VARCHAR(256),
    attributes  JSONB
);

CREATE TABLE benefittransaction (
    bt_id        SERIAL PRIMARY KEY,
    user_id      INT REFERENCES "user"(user_id) ON DELETE CASCADE,
    selection    JSONB,
    benefit_name VARCHAR(256),
    value        INT
);
