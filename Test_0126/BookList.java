package Test_0126;

public class BookList {
    private Book[] books = new Book[100];
    private int size = 0;

    public BookList() {
        books[0] = new Book("三国演义", "罗贯中", 100, "古典小说");
        books[1] = new Book("西游记", "吴承恩", 110, "古典小说");
        books[2] = new Book("红楼梦", "曹雪芹", 120, "古典小说");
        size = 3;
    }

    // 对其中的某一本书进行操作.
    public Book getBook(int index) {
        return books[index];
    }

    public void setBook(int index, Book book) {
        books[index] = book;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
