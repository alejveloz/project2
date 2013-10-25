-- A movie's ID should be unique, must have a title, and a positive id
CREATE TABLE Movie(id int PRIMARY KEY, 
                   title varchar(100) NOT NULL, 
                   year int, 
                   rating varchar(10), 
                   company varchar(50),
                   CHECK(id>0)) 
                   ENGINE=INNODB;

-- An actor's ID should be unique, must have first/last name, must have dob, dod must be after dob, and a positive id
CREATE TABLE Actor(id int PRIMARY KEY, 
                   last varchar(20) NOT NULL, 
                   first varchar(20) NOT NULL, 
                   sex varchar(6), 
                   dob date NOT NULL, 
                   dod date,
                   CHECK((dod = "" OR (dod != "" AND dod >= dob) AND id > 0)))
                   ENGINE=INNODB;

-- A director's ID should be unique, must have first/last name, must have dob, dod must be after dob, and a positive id
CREATE TABLE Director(id int PRIMARY KEY,
                      last varchar(20) NOT NULL,
                      first varchar(20) NOT NULL, 
                      dob date NOT NULL, 
                      dod date,
                      CHECK((dod = "" OR (dod != "" AND dod >= dob) AND id > 0)))
                      ENGINE=INNODB;

-- A MovieGenre relation must reference a valid mid in Movie
CREATE TABLE MovieGenre(mid int, 
                        genre varchar(20),
                        FOREIGN KEY(mid) references Movie(id))
                        ENGINE=INNODB;

-- A MovieDirector relation must reference a valid mid in Movie and did in Director
CREATE TABLE MovieDirector(mid int, 
                           did int,
                           FOREIGN KEY(mid) references Movie(id),
                           FOREIGN KEY(did) references Director(id))
                           ENGINE=INNODB;

-- A MovieActor relation must reference a valid mid in Movie and aid in Actor
CREATE TABLE MovieActor(mid int, 
                        aid int, 
                        role varchar(50),
                        FOREIGN KEY(mid) references Movie(id),
                        FOREIGN KEY(aid) references Actor(id))
                        ENGINE=INNODB;

-- A Review relation must reference a valid mid in Movie, rating must be between 0 and 5
CREATE TABLE Review(name varchar(20), 
                    time timestamp, 
                    mid int, 
                    rating int, 
                    comment varchar(500),
                    FOREIGN KEY(mid) references Movie(id),
                    CHECK(rating>=0 AND rating<=5))
                    ENGINE=INNODB;

-- A MaxPersonID id must never be null
CREATE TABLE MaxPersonID(id int NOT NULL) ENGINE=INNODB;

--A MaxMovieID id must never be null
CREATE TABLE MaxMovieID(id int NOT NULL) ENGINE=INNODB;