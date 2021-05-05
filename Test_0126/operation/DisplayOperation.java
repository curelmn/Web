package Test_0126.operation;

import Test_0126.BookList;

public class DisplayOperation implements IOperation{
    @Override
    public void work(BookList bookList) {
        System.out.println("显示书籍列表");
    }
}
