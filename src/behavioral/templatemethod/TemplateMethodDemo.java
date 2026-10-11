package behavioral.templatemethod;

/*
> 템플릿 메소드
- 상위 클래스에서 골격을 정의히고, 하위 클래스에서 세부 처리를 구체화하는 구조의 패턴
- 유사한 서브 클래스를 묶어 공통된 내용을 상위 클래스에서 정의함으로써 코드의 양을 줄이고 유지보수를 용이하게 해줌
*/
public class TemplateMethodDemo {
    public static void main(String[] args) {
        DataProcessor csv = new CsvProcessor();
        DataProcessor json = new JsonProcessor();

        csv.process();
        System.out.println("--");
        json.process();
    }
}

// AbstractClass
abstract class DataProcessor {

    // 템플릿 메서드: 알고리즘의 골격 (final로 순서 변경 방지)
    public final void process() {
        read();
        parse();
        if (needValidation()) {   // Hook 메서드
            validate();
        }
        save();
    }

    protected abstract void read();    // 하위 클래스가 구현
    protected abstract void parse();
    protected abstract void save();

    protected boolean needValidation() { return true; }   // Hook: 기본값 제공, 필요 시 재정의
    protected void validate() { System.out.println("[공통] 데이터 검증"); }   // 공통 단계
}

// ConcreteClass 1
class CsvProcessor extends DataProcessor {
    protected void read()  { System.out.println("[CSV] 파일 읽기"); }
    protected void parse() { System.out.println("[CSV] 쉼표로 분리하여 파싱"); }
    protected void save()  { System.out.println("[CSV] DB 저장"); }
}

// ConcreteClass 2
class JsonProcessor extends DataProcessor {
    protected void read()  { System.out.println("[JSON] API 응답 읽기"); }
    protected void parse() { System.out.println("[JSON] 객체로 역직렬화"); }
    protected void save()  { System.out.println("[JSON] DB 저장"); }

    @Override
    protected boolean needValidation() { return false; }   // 검증 단계 생략 (Hook 활용)
}
