CREATE TABLE Item(id int PRIMARY KEY, 
                  name varchar(100) NOT NULL,
                  buy_price double,
                  first_bid double NOT NULL,
                  started date NOT NULL,
                  ends date NOT NULL,
                  description varchar(4000) NOT NULL,
                  number_of_bids int NOT NULL,
                  currently double NOT_NULL)
                  ENGINE=INNODB;

CREATE TABLE User(id varchar(50) PRIMARY KEY,
                  rating int NOT NULL,
                  location varchar(50),
                  country varchar(50)
                  ENGINE=INNODB;

CREATE TABLE Category(name varchar(30))ENGINE=INNODB;

CREATE TABLE Bid(iid int,
                 time date,
                 uid varchar(50),
                 amount double,
                 PRIMARY KEY (iid, time)
                 FOREIGN KEY(iid) references Item(id),
                 FOREIGN KEY(uid) references User(id))
                 ENGINE=INNODB;

CREATE TABLE ItemSeller(iid int PRIMARY KEY,
                        uid varchar(5),
                        FOREIGN KEY(iid) references Item(id),
                        FOREIGN KEY(uid) references User(id))
                        ENGINE=INNODB;

CREATE TABLE ItemCategory(iid int,
                          category varchar(30),
                          FOREIGN KEY(iid) references Item(id),
                          FOREIGN KEY(category references Category(name))
                          ENGINE=INNODB;
