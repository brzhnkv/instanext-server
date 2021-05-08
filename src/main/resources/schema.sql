CREATE TABLE client
(
    id int NOT NULL,
    username varchar(50) NOT NULL,
    token varchar(100) DEFAULT NULL,
    client_data bytea DEFAULT NULL,
    cookie_data bytea DEFAULT NULL,
    PRIMARY KEY (id)
)