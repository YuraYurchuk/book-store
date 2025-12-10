INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES
    (1, 'Good Book 1', 'Yuri', '1111111111111', 12.00, 'Good simple book 1', 'http://example.com/cover1.jpg', false),
    (2, 'Good Book 2', 'Yuri', '2222222222222', 15.50, 'Good simple book 2', 'http://example.com/cover2.jpg', false),
    (3, 'Good Book 3', 'Yuri', '3333333333333', 20.00, 'Good simple book 3', 'http://example.com/cover3.jpg', false),
    (4, 'Good Book 4', 'Dmytro', '4444444444444', 15.50, 'Good simple book 4', 'http://example.com/cover4.jpg', false),
    (5, 'Good Book 5', 'Dmytro', '5555555555555', 20.00, 'Good simple book 5', 'http://example.com/cover5.jpg', false);

INSERT INTO books_categories (book_id, category_id)
VALUES
    (1, 1),
    (2, 1),
    (3, 1),
    (4, 1),
    (5, 1);
