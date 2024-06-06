CREATE TABLE IF NOT EXISTS accounts (
    id SERIAL PRIMARY KEY,
    created_at BIGINT,
    updated_at BIGINT,
    is_active BOOLEAN DEFAULT TRUE,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    authority VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    created_at BIGINT,
    updated_at BIGINT,
    is_active BOOLEAN DEFAULT TRUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    year_of_birth VARCHAR(4),
    phone VARCHAR(20),
    country_code VARCHAR(10),
    city_code VARCHAR(10),
    zip_code VARCHAR(10),
    account_id INTEGER,
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE TABLE IF NOT EXISTS hotels (
    id SERIAL PRIMARY KEY,
    created_at BIGINT,
    updated_at BIGINT,
    is_active BOOLEAN DEFAULT TRUE,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    no_of_rooms INTEGER DEFAULT 0,
    no_of_available_rooms INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS hotel_rooms (
    id SERIAL PRIMARY KEY,
    created_at BIGINT,
    updated_at BIGINT,
    is_active BOOLEAN DEFAULT TRUE,
    code VARCHAR(255) NOT NULL,
    no_of_bedrooms INTEGER NOT NULL,
    no_of_beds INTEGER NOT NULL,
    no_of_bathrooms INTEGER NOT NULL,
    price DOUBLE PRECISION,
    no_of_guests INTEGER,
    hotel_id INTEGER,
    FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);

CREATE TABLE IF NOT EXISTS hotel_reservations (
    id SERIAL PRIMARY KEY,
    created_at BIGINT,
    updated_at BIGINT,
    is_active BOOLEAN DEFAULT TRUE,
    check_in_date DATE,
    check_out_date DATE,
    no_of_guests INTEGER,
    special_requests VARCHAR(255),
    total_price DOUBLE PRECISION,
    cancelled_at BIGINT,
    user_id INTEGER,
    hotel_id INTEGER,
    hotel_room_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (hotel_id) REFERENCES hotels(id),
    FOREIGN KEY (hotel_room_id) REFERENCES hotel_rooms(id)
);
