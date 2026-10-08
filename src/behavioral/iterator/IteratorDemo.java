package behavioral.iterator;

import java.util.Iterator;

/*
> 반복자
- 자료 구조와 같이 접근이 잦은 객체에 대해 동일한 인터페이스를 사용하도록 하는 패턴
- 내부 표현 방법의 노출 없이 순차적인 접근 가능
*/
public class IteratorDemo {
    public static void main(String[] args) {
        BookShelf shelf = new BookShelf(3);
        shelf.append(new Book("Java의 정석"));
        shelf.append(new Book("클린 코드"));
        shelf.append(new Book("이펙티브 자바"));

        // 방법 1: Iterator 직접 사용
        Iterator<Book> it = shelf.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().name());
        }
        // 방법 2: Iterable 이라서 for-each 도 가능
        // for (Book b : shelf) System.out.println(b.name());
    }
}

record Book(String name) { }

// ConcreteAggregate : 내부는 배열이지만 클라이언트는 알 필요 없음
class BookShelf implements Iterable<Book> {
    private final Book[] books;
    private int last = 0;

    BookShelf(int max) { books = new Book[max]; }

    void append(Book book) { books[last++] = book; }
    Book getBookAt(int index) { return books[index]; }
    int getLength() { return last; }

    @Override
    public Iterator<Book> iterator() { return new BookShelfIterator(this); }
}

// ConcreteIterator
class BookShelfIterator implements Iterator<Book> {
    private final BookShelf shelf;
    private int index = 0;

    BookShelfIterator(BookShelf shelf) { this.shelf = shelf; }

    @Override public boolean hasNext() { return index < shelf.getLength(); }
    @Override public Book next() { return shelf.getBookAt(index++); }
}
