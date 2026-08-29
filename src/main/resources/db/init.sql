-- Table: police_acc
CREATE TABLE police (
    police_id VARCHAR(50) PRIMARY KEY,
    full_name VARCHAR(100),
    department VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO police_acc (police_id, full_name, department, email) VALUES
('P001', 'Ravi Kumar', 'Traffic', 'ravi.kumar@police.gov.in'),
('P002', 'Sita Sharma', 'Cyber Crime', 'sita.sharma@police.gov.in'),
('P003', 'Aakash Singh', 'Criminal Investigation', 'aakash.singh@police.gov.in'),
('P004', 'Neha Reddy', 'Traffic', 'neha.reddy@police.gov.in'),
('P005', 'Vikram Joshi', 'Cyber Crime', 'vikram.joshi@police.gov.in');


CREATE TABLE police_acc (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100),
    police_id VARCHAR(50),
    department VARCHAR(100),
    official_email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    vehicle_number VARCHAR(20) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE qr_details (
    unique_id VARCHAR(50) PRIMARY KEY,
    full_name VARCHAR(100),
    age INT,
    gender VARCHAR(10),
    blood_group VARCHAR(10),
    nearest_police_station VARCHAR(100),
    emergency_contact VARCHAR(15),
    aadhaar VARCHAR(20) UNIQUE,
    vehicle_no VARCHAR(20) UNIQUE,
    address VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
