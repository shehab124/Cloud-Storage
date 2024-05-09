CREATE TABLE IF NOT EXISTS USERS (
  userid INT PRIMARY KEY auto_increment,
  username VARCHAR(20) UNIQUE,
  salt VARCHAR,
  password VARCHAR,
  firstname VARCHAR(20),
  lastname VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS NOTES (
    noteid INT PRIMARY KEY auto_increment,
    notetitle VARCHAR(20),
    notedescription VARCHAR (1000),
    userid INT,
    foreign key (userid) references USERS(userid)
);

CREATE TABLE IF NOT EXISTS FILES (
    fileId INT PRIMARY KEY auto_increment,
    filename VARCHAR,
    contenttype VARCHAR,
    filesize VARCHAR,
    userid INT,
    filedata BLOB,
    foreign key (userid) references USERS(userid)
);

CREATE TABLE IF NOT EXISTS CREDENTIALS (
    credentialid INT PRIMARY KEY auto_increment,
    url VARCHAR(100),
    username VARCHAR (30),
    keys VARCHAR,
    password VARCHAR,
    userid INT,
    foreign key (userid) references USERS(userid)
);

INSERT INTO USERS ( username, salt, password, firstname, lastname)
VALUES ('chehab', 'UlMiOVKnkN7JjvC79fEGpw==', 'aU2Ku2+Q0EAfohmara7drh4CX8iwT9G0pfbYQnV6vXdcMGpOqNgtcmXuRQnVbxTNhg8luymX9c9lng0elxwi5fbHABptCqlHUFmvaZ/ctOGNAJAw71BmT8zKk4zFW42ZlTHTcxDLajjDZrnYwpkxYONODa1nZFAHyYIehQVTXOwkhUSsITqddeE++HYpF5RXjeec4wTYEIGP2S+dvfPouzkPW0v0GSyceK9EWWtBYPE6XJ9sg2rE4xxNBHgBAO0EFuFJCAi9zQtxwjukABkA7Vk6XN0FI5CDFx+Fn2QgskUoS+5JRNIaQQP5aFTI85qm51uBBG4Z8qhp6vfdUXO8Ew==', 'chehab', 'gamal');
