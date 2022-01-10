CREATE TABLE file (
    id                      VARCHAR NOT NULL,
    name                    VARCHAR NOT NULL,
    path                    VARCHAR NOT NULL,
    size                    BIGINT NOT NULL,
    content_type            VARCHAR NOT NULL,
    PRIMARY KEY(id)
);
