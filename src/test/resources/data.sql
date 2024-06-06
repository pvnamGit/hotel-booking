-- Insert into accounts
INSERT INTO accounts (email, password, authority, created_at, updated_at, is_active) VALUES
('admin@example.com', 'password', 'ADMIN', extract(epoch from now()) * 1000, extract(epoch from now()) * 1000, TRUE),
('user1@example.com', 'password', 'USER', extract(epoch from now()) * 1000, extract(epoch from now()) * 1000, TRUE),
('user2@example.com', 'password', 'USER', extract(epoch from now()) * 1000, extract(epoch from now()) * 1000, TRUE);

-- Insert into users
INSERT INTO users (first_name, last_name, year_of_birth, phone, country_code, city_code, zip_code, account_id, created_at, updated_at, is_active) VALUES
('John', 'Doe', '1985', '1234567890', 'US', 'NY', '10001', 1, extract(epoch from now()) * 1000, extract(epoch from now()) * 1000, TRUE),
('Jane', 'Smith', '1990', '0987654321', 'US', 'CA', '90001', 2, extract(epoch from now()) * 1000, extract(epoch from now()) * 1000, TRUE),
('Jim', 'Beam', '1988', '1122334455', 'US', 'TX', '73301', 3, extract(epoch from now()) * 1000, extract(epoch from now()) * 1000, TRUE);

-- Insert into hotels
INSERT INTO hotels (name, address, city, country, no_of_rooms, no_of_available_rooms, created_at, updated_at, is_active) VALUES
('Test Hotel 1', '123 Test St', 'Test City', 'Test Country', 100, 50, extract(epoch from now()) * 1000, extract(epoch from now()) * 1000, TRUE),
('Test Hotel 2', '456 Sample Rd', 'Sample City', 'Sample Country', 80, 30, extract(epoch from now()) * 1000, extract(epoch from now()) * 1000, TRUE);

-- Insert into hotel_rooms
INSERT INTO hotel_rooms (code, no_of_bedrooms, no_of_beds, no_of_bathrooms, price, no_of_guests, hotel_id, created_at, updated_at, is_active) VALUES
('Room_1', 1, 1, 1, 100.00, 2, 1, extract(epoch from now()) * 1000, extract(epoch from now()) * 1000, TRUE),
('Room_2', 2, 2, 1, 150.00, 4, 1, extract(epoch from now()) * 1000, extract(epoch from now()) * 1000, TRUE),
('Room_3', 1, 1, 1, 100.00, 2, 2, extract(epoch from now()) * 1000, extract(epoch from now()) * 1000, TRUE);

-- Insert into hotel_reservations
INSERT INTO hotel_reservations (user_id, hotel_id, hotel_room_id, created_at, updated_at, is_active) VALUES
(1, 1, 1, extract(epoch from now()) * 1000, extract(epoch from now()) * 1000, TRUE),
(2, 1, 2, extract(epoch from now()) * 1000, extract(epoch from now()) * 1000, TRUE),
(3, 2, 3, extract(epoch from now()) * 1000, extract(epoch from now()) * 1000, TRUE);