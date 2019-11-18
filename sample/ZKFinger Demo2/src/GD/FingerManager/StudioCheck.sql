Use StudioCheck;
CREATE TABLE Staff(
    id INT NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    sno VARCHAR(30) NOT NULL,
    grade VARCHAR(10),
    permission INT NOT NULL,
    tutor VARCHAR(50),
    team VARCHAR(50),
    status INT,
    password VARCHAR(50)
);

CREATE TABLE AttendanceRecord(
    id INT  NOT NULL PRIMARY KEY,
    intime DATETIME DEFAULT CURRENT_TIMESTAMP,
    outime DATETIME ,
    lastime FLOAT DEFAULT 0.0,
    FOREIGN KEY (id) REFERENCES Staff (id)
);

CREATE TABLE IlegalList(
    id INT  NOT NULL PRIMARY KEY,
    occurtime DATETIME,
    behavior VARCHAR(100),
    FOREIGN KEY (id) REFERENCES Staff (id)
);

CREATE TABLE FingerBase(
    id INT  NOT NULL  PRIMARY KEY,
    template VARCHAR (2048),
    memoryid INT DEFAULT 0,
    FOREIGN KEY (id) REFERENCES Staff (id)
);

CREATE TABLE AttendanceStatistics(
    id INT  NOT NULL PRIMARY KEY,
    week INT,
    totaltime INT,
    comment VARCHAR(30),
    FOREIGN KEY (id) REFERENCES Staff (id)
)

