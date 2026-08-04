CREATE TABLE owners (
    id            BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR(100)  NOT NULL,
    last_name     VARCHAR(100)  NOT NULL,
    email         VARCHAR(150)  NOT NULL UNIQUE,
    phone         VARCHAR(20),
    created_at    TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP     NOT NULL DEFAULT now()
);

CREATE INDEX idx_owners_email ON owners (email);
CREATE INDEX idx_owners_last_name ON owners (last_name);
