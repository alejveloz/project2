CREATE TABLE Item(id int PRIMARY KEY, 
                  name varchar(100) NOT NULL,
                  buy_price decimal(8,2),
                  first_bid decimal(8,2) NOT NULL,
                  started timestamp NOT NULL,
                  ends timestamp NOT NULL,
                  description varchar(4000) NOT NULL,
                  number_of_bids int NOT NULL,
                  currently decimal(8,2) NOT NULL);

CREATE TABLE User(id varchar(50) PRIMARY KEY,
                  rating int NOT NULL,
                  location varchar(50),
                  country varchar(50));

CREATE TABLE Category(name varchar(30));

CREATE TABLE Bid(iid int,
                 time timestamp,
                 uid varchar(50),
                 amount decimal(8,2),
                 PRIMARY KEY (iid, time),
                 FOREIGN KEY(iid) references Item(id),
                 FOREIGN KEY(uid) references User(id));

CREATE TABLE ItemSeller(iid int PRIMARY KEY,
                        uid varchar(50),
                        FOREIGN KEY(iid) references Item(id),
                        FOREIGN KEY(uid) references User(id));

CREATE TABLE ItemCategory(iid int,
                          category varchar(30),
                          FOREIGN KEY(iid) references Item(id),
                          FOREIGN KEY(category) references Category(name));
