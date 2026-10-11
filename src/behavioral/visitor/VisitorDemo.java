package behavioral.visitor;

import java.util.List;

/*
> 방문자
- 각 클래스들의 데이터 구조에서 처리 기능을 분리하여 별도의 클래스로 구성
- 분리된 처리 기능은 각 클래스를 방문하여 수행
*/
public class VisitorDemo {
    public static void main(String[] args) {
        List<Item> cart = List.of(
                new Book(20000),
                new Electronics(100000),
                new Electronics(50000)
        );

        TaxVisitor taxVisitor = new TaxVisitor();
        DiscountVisitor discountVisitor = new DiscountVisitor();

        for (Item item : cart) {
            item.accept(taxVisitor);        // 새 연산(Visitor)을 붙여도 Item은 수정 불필요
            item.accept(discountVisitor);
        }

        System.out.println("총 세금: " + taxVisitor.getTotal() + "원");
        System.out.println("총 할인: " + discountVisitor.getTotal() + "원");
    }
}

// Element
interface Item {
    void accept(Visitor visitor);
}

// ConcreteElement : accept 에서 자기 자신(this)을 넘기는 것이 핵심 (더블 디스패치)
class Book implements Item {
    private final int price;
    Book(int price) { this.price = price; }
    int getPrice() { return price; }
    public void accept(Visitor visitor) { visitor.visit(this); }
}
class Electronics implements Item {
    private final int price;
    Electronics(int price) { this.price = price; }
    int getPrice() { return price; }
    public void accept(Visitor visitor) { visitor.visit(this); }
}

// Visitor
interface Visitor {
    void visit(Book book);
    void visit(Electronics electronics);
}

// ConcreteVisitor 1 : 세금 계산 (도서는 면세, 전자제품은 10%)
class TaxVisitor implements Visitor {
    private int total = 0;
    public void visit(Book book) { /* 면세 */ }
    public void visit(Electronics e) { total += e.getPrice() / 10; }
    int getTotal() { return total; }
}

// ConcreteVisitor 2 : 할인 계산 (도서 10%, 전자제품 5%)
class DiscountVisitor implements Visitor {
    private int total = 0;
    public void visit(Book book) { total += book.getPrice() / 10; }
    public void visit(Electronics e) { total += e.getPrice() / 20; }
    int getTotal() { return total; }
}
