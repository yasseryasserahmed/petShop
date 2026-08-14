CREATE TABLE species (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(50)   NOT NULL UNIQUE,
    description   VARCHAR(255),
    created_at    TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP     NOT NULL DEFAULT now()
);

CREATE TABLE pets (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(100)  NOT NULL,
    species_id    BIGINT        NOT NULL REFERENCES species(id),
    birth_date    DATE,
    owner_id      BIGINT        NOT NULL,
    created_at    TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP     NOT NULL DEFAULT now()
);

CREATE INDEX idx_pets_owner_id ON pets (owner_id);
CREATE INDEX idx_pets_species_id ON pets (species_id);

-- Seed common species so the API is usable immediately without a manual
-- setup step. Adding new ones later is a normal POST /species call.
INSERT INTO species (name, description) VALUES
    ('Dog', 'Domestic dog'),
    ('Cat', 'Domestic cat'),
    ('Bird', 'Domestic bird'),
    ('Rabbit', 'Domestic rabbit'),
    ('Reptile', 'Domestic reptile');
