-- Run this in MySQL Workbench or terminal before starting the server

-- 1. Create the database
CREATE DATABASE IF NOT EXISTS resolvenet;
USE resolvenet;

-- 2. Users table
CREATE TABLE IF NOT EXISTS users (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(50)  NOT NULL,
    role     VARCHAR(20)  NOT NULL DEFAULT 'USER'   -- 'USER' or 'ADMIN'
);

-- 3. Complaints table
CREATE TABLE IF NOT EXISTS complaints (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL,
    title       VARCHAR(100) NOT NULL,
    description TEXT         NOT NULL,
    priority    VARCHAR(10)  NOT NULL DEFAULT 'LOW',    -- LOW, MEDIUM, HIGH
    status      VARCHAR(20)  NOT NULL DEFAULT 'PENDING' -- PENDING, IN_PROGRESS, RESOLVED
);

-- 4. Sample data to test with
INSERT INTO users (username, password, role) VALUES
('admin',   'admin123', 'ADMIN'),
('alice',   'alice123', 'USER'),
('bob',     'bob123',   'USER');

INSERT INTO complaints (username, title, description, priority, status) VALUES
('alice', 'No water supply', 'Water has not come for 2 days in Block A', 'HIGH',   'PENDING'),
('bob',   'Street light broken', 'Light near gate 3 is broken since Monday', 'MEDIUM', 'IN_PROGRESS');
