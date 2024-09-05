-- Users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    user_type VARCHAR(20) NOT NULL
);

-- Students table (extends Users)
CREATE TABLE students (
    user_id INTEGER PRIMARY KEY REFERENCES users(id),
    student_id VARCHAR(20) NOT NULL UNIQUE,
    major VARCHAR(50) NOT NULL
);

-- Professors table (extends Users)
CREATE TABLE professors (
    user_id INTEGER PRIMARY KEY REFERENCES users(id),
    employee_id VARCHAR(20) NOT NULL UNIQUE,
    department VARCHAR(50) NOT NULL
);

-- Documents table
CREATE TABLE documents (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    publication_date DATE NOT NULL,
    type VARCHAR(20) NOT NULL
);

-- Books table (extends Documents)
CREATE TABLE books (
    document_id INTEGER PRIMARY KEY REFERENCES documents(id),
    isbn VARCHAR(20) NOT NULL UNIQUE,
    page_count INTEGER NOT NULL,
    borrowed BOOLEAN NOT NULL DEFAULT FALSE,
    reserved BOOLEAN NOT NULL DEFAULT FALSE
);

-- Theses table (extends Documents)
CREATE TABLE theses (
    document_id INTEGER PRIMARY KEY REFERENCES documents(id),
    university VARCHAR(100) NOT NULL,
    domain VARCHAR(50) NOT NULL,
    borrowed BOOLEAN NOT NULL DEFAULT FALSE
);

-- Scientific Journals table (extends Documents)
CREATE TABLE scientific_journals (
    document_id INTEGER PRIMARY KEY REFERENCES documents(id),
    issn VARCHAR(9) NOT NULL UNIQUE,
    research_domain VARCHAR(50) NOT NULL,
    borrowed BOOLEAN NOT NULL DEFAULT FALSE,
    reserved BOOLEAN NOT NULL DEFAULT FALSE
);

-- Magazines table (extends Documents)
CREATE TABLE magazines (
    document_id INTEGER PRIMARY KEY REFERENCES documents(id),
    issn VARCHAR(9) NOT NULL UNIQUE,
    issue_number INTEGER NOT NULL,
    borrowed BOOLEAN NOT NULL DEFAULT FALSE
);