package structural.proxy;

import java.lang.reflect.Proxy;

/*
> 프록시 ("대리자", "접근 제어", "지연 생성(Lazy)")
- 복잡한 시스템의 개발하기 쉽도록 클래스나 객체들을 조합하는 패턴으로 "대리자"라고 불림
- 내부에서는 객체 간의 복잡한 관게를 단순하게 정리해주고
- 외부에서는 객체의 세부적인 내용을 숨겨 주는 역할을 수행함
- 지연 생성
*/
public class ProxyDemo {

    public static void main(String[] args) {
        Image image = new ProxyImage("photo.jpg");
        System.out.println("이미지 객체 생성 완료 (아직 로딩 안 함)");

        System.out.println("-- 첫 번째 display --");
        image.display();   // 이때 처음 실제 로딩

        System.out.println("-- 두 번째 display --");
        image.display();   // 이미 로딩됨 -> 재사용
    }
}

interface Image {
    void display();
}

class RealImage implements Image {

    private final String fileName;

    RealImage(String fileName) {
        this.fileName = fileName;
        System.out.println("[RealImage]" + fileName + " 디스크에서 로딩...");
    }

    @Override
    public void display() {
        System.out.println(fileName + " 화면에 표시");
    }
}

class ProxyImage implements Image {

    private final String fileName;
    private RealImage realImage;

    ProxyImage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(fileName);
        }
        realImage.display();
    }
}
