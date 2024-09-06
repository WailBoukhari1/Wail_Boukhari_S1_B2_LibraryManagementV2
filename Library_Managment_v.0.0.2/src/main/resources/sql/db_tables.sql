-- Create the books table
CREATE TABLE books (
    id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publication_date DATE NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    pages INTEGER NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    due_date DATE,
    reserved_by VARCHAR(50)
);

-- Create the magazines table
CREATE TABLE magazines (
    id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publication_date DATE NOT NULL,
    issn VARCHAR(20) UNIQUE NOT NULL,
    issue_number INTEGER NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    due_date DATE,
    reserved_by VARCHAR(50)
);

-- Create the scientific_journals table
CREATE TABLE scientific_journals (
    id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publication_date DATE NOT NULL,
    issn VARCHAR(20) UNIQUE NOT NULL,
    research_field VARCHAR(100) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    due_date DATE,
    reserved_by VARCHAR(50)
);

-- Create the university_theses table
CREATE TABLE university_theses (
    id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publication_date DATE NOT NULL,
    university VARCHAR(100) NOT NULL,
    degree VARCHAR(50) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    due_date DATE,
    reserved_by VARCHAR(50)
);

-- Create the students table
CREATE TABLE students (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    student_id VARCHAR(20) UNIQUE NOT NULL,
    major VARCHAR(100) NOT NULL
);

-- Create the professors table
CREATE TABLE professors (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    department VARCHAR(100) NOT NULL,
    research_area VARCHAR(100) NOT NULL
);