INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (
     1,
     'Good Book',
     'Yuri',
     '1111111111111',
     12.00,
     'Good simple book',
     'http://example.com/cover3.jpg',
    false
);

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1);
