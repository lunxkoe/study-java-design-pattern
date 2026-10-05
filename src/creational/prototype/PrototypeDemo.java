package creational.prototype;

import java.util.ArrayList;
import java.util.List;

/*
> 프로토타입 - 복제하는 방식으로 객체를 생성
- 원본 객체를 복제하는 방법으로 객체를 생성하는 패턴
- 일반적인 방법으로 객체를 생성하며, 비용이 큰 경우 주로 이용
    - 객체 생성 비용(DB 조회, 복잡한 초기화)이 크거나 생성 과정이 복잡해서 복제가 더 싸고 편할 때
    - 클래스가 런타임에 결정되는 경우
    - 상태가 약간씩만 다른 객체가 많이 필요할 때
*/
public class PrototypeDemo {

    public static void main(String[] args) {
        Document original = new Document("보고서 템플릿");
        original.addPage("1페이지");

        Document copy = original.clone();   // new 없이 복제
        copy.setTitle("2024 보고서");
        copy.addPage("2페이지");

        System.out.println(original);
        System.out.println(copy);
    }
}

class Document implements Cloneable {

    private String title;
    private List<String> pages = new ArrayList<>();

    Document(String title) {
        this.title = title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    void addPage(String page) {
        pages.add(page);
    }

    @Override
    public Document clone() {
        try {
            Document copy = (Document) super.clone(); // 얕은 복사
            copy.pages = new ArrayList<>(this.pages); // 가변 필드는 깊은 복사
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Document{title=" + title + ", pages=" + pages + "}";
    }
}
