CREATE TABLE Users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    type VARCHAR(50) NOT NULL,
    student_id VARCHAR(50),
    department VARCHAR(255),
    CHECK (type IN ('Student', 'Professor')),
    CHECK ((type = 'Student' AND student_id IS NOT NULL AND department IS NULL) OR 
           (type = 'Professor' AND department IS NOT NULL AND student_id IS NULL))
);
CREATE TABLE Documents (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    publication_year INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    research_field VARCHAR(255),
    university VARCHAR(255),
    field VARCHAR(255),
    CHECK (type IN ('Book', 'Magazine', 'ScientificJournal', 'UniversityThesis')),
    CHECK ((type = 'ScientificJournal' AND research_field IS NOT NULL AND university IS NULL AND field IS NULL) OR
           (type = 'UniversityThesis' AND university IS NOT NULL AND field IS NOT NULL AND research_field IS NULL) OR
           (type IN ('Book', 'Magazine') AND research_field IS NULL AND university IS NULL AND field IS NULL))
);
CREATE TABLE Loans (
    id UUID PRIMARY KEY,
    document_id UUID NOT NULL,
    user_id UUID NOT NULL,
    loan_date DATE NOT NULL,
    return_date DATE,
    FOREIGN KEY (document_id) REFERENCES Documents(id),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);
CREATE TABLE Reservations (
    id UUID PRIMARY KEY,
    document_id UUID NOT NULL,
    user_id UUID NOT NULL,
    reservation_date DATE NOT NULL,
    FOREIGN KEY (document_id) REFERENCES Documents(id),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);