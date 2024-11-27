CREATE TABLE IF NOT EXISTS login (
    name VARCHAR(20),
    addr VARCHAR(20),
    phno VARCHAR(10),
    id VARCHAR(20) PRIMARY KEY,
    pwd VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS books (
    title VARCHAR(20),
    author VARCHAR(20),
    version VARCHAR(20),
    publisher VARCHAR(20),
    cost INT(10)
);

CREATE TABLE IF NOT EXISTS details (
    id VARCHAR(20),
    title VARCHAR(20),
    amount INT(10),
    cno VARCHAR(12)
);
