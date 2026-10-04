# 정보처리기사 디자인 패턴 23가지 — 자바 구현 정리

정보처리기사 필기(소프트웨어 설계)에서 다루는 GoF(Gang of Four) 디자인 패턴 **23개 전체**를 정리했습니다.
각 패턴마다 **시험 정의 → 언제 쓰는지 → 추가 설명(구조/역할) → 시험 포인트 → 실행 가능한 자바 예제 + 실행 결과** 순서로 구성했습니다.

> **실행 방법**
> - 모든 예제는 **Java 17**에서 동작하도록 작성했습니다 (record 정도만 사용, Java 21 전용 문법 없음).
> - 각 코드 블록은 **독립된 파일 1개**입니다. 맨 위의 `XxxDemo` 클래스가 `main`을 가지고 있으니 `XxxDemo.java`로 저장하세요.
> - 실행: `java XxxDemo.java` (단일 파일 소스 실행) 또는 IntelliJ에서 `main` 실행.
> - 콘솔에서 한글이 깨지면 IDE에서 실행하거나 `chcp 65001` 후 `java -Dfile.encoding=UTF-8 XxxDemo.java`로 실행해 보세요.

---

## 목차

| 분류 | 패턴 |
|---|---|
| **생성(Creational) 5개** | 1. Abstract Factory · 2. Builder · 3. Factory Method · 4. Prototype · 5. Singleton |
| **구조(Structural) 7개** | 6. Adapter · 7. Bridge · 8. Composite · 9. Decorator · 10. Facade · 11. Flyweight · 12. Proxy |
| **행위(Behavioral) 11개** | 13. Chain of Responsibility · 14. Command · 15. Interpreter · 16. Iterator · 17. Mediator · 18. Memento · 19. Observer · 20. State · 21. Strategy · 22. Template Method · 23. Visitor |

- **생성 패턴**: 객체를 *어떻게 만들 것인가* (생성 과정을 캡슐화, 클라이언트가 구체 클래스를 몰라도 됨)
- **구조 패턴**: 클래스/객체를 *어떻게 조합할 것인가* (더 큰 구조를 만들 때 유연성·효율성 확보)
- **행위 패턴**: 객체 간 *책임과 상호작용을 어떻게 나눌 것인가* (알고리즘, 역할 분담)

---

# Part 1. 생성(Creational) 패턴

---

## 1. Abstract Factory (추상 팩토리)

**시험 정의**
구체적인 클래스를 지정하지 않고, 서로 관련되거나 의존적인 객체들의 **집합(제품군, Family)** 을 생성하기 위한 인터페이스를 제공하는 패턴.

**언제 사용하나**
- 제품군(예: Windows 스타일 버튼+체크박스 / Mac 스타일 버튼+체크박스)을 **일관되게** 만들어야 할 때
- 시스템이 제품 생성 방식과 독립적이어야 하고, 제품군을 통째로 교체할 수 있어야 할 때

**추가 설명**
- 역할: `AbstractFactory`(제품군 생성 인터페이스), `ConcreteFactory`(특정 제품군 생성), `AbstractProduct`(제품 인터페이스), `ConcreteProduct`(실제 제품).
- Factory Method가 "제품 **하나**"를 서브클래스가 결정한다면, Abstract Factory는 "관련된 제품 **여러 개(군)**"를 만든다.
- 단점: 새로운 **종류**의 제품(예: 슬라이더)을 추가하려면 팩토리 인터페이스와 모든 구체 팩토리를 수정해야 한다.

**시험 포인트**: "관련된 객체들의 군(Family)", "구체적인 클래스에 의존하지 않고", "Kit(키트) 패턴"이라고도 함.

```java
// AbstractFactoryDemo.java
public class AbstractFactoryDemo {
    public static void main(String[] args) {
        runApp(new WindowsFactory());
        runApp(new MacFactory());
    }

    // 클라이언트는 구체 클래스(WindowsButton 등)를 전혀 모른다
    static void runApp(GUIFactory factory) {
        Button button = factory.createButton();
        Checkbox checkbox = factory.createCheckbox();
        button.paint();
        checkbox.paint();
    }
}

// AbstractProduct
interface Button { void paint(); }
interface Checkbox { void paint(); }

// AbstractFactory
interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

// ConcreteProduct - Windows 제품군
class WindowsButton implements Button {
    public void paint() { System.out.println("[Windows] 버튼 그리기"); }
}
class WindowsCheckbox implements Checkbox {
    public void paint() { System.out.println("[Windows] 체크박스 그리기"); }
}

// ConcreteProduct - Mac 제품군
class MacButton implements Button {
    public void paint() { System.out.println("[Mac] 버튼 그리기"); }
}
class MacCheckbox implements Checkbox {
    public void paint() { System.out.println("[Mac] 체크박스 그리기"); }
}

// ConcreteFactory
class WindowsFactory implements GUIFactory {
    public Button createButton() { return new WindowsButton(); }
    public Checkbox createCheckbox() { return new WindowsCheckbox(); }
}
class MacFactory implements GUIFactory {
    public Button createButton() { return new MacButton(); }
    public Checkbox createCheckbox() { return new MacCheckbox(); }
}
```

**실행 결과**
```
[Windows] 버튼 그리기
[Windows] 체크박스 그리기
[Mac] 버튼 그리기
[Mac] 체크박스 그리기
```

---

## 2. Builder (빌더)

**시험 정의**
복합 객체의 **생성 과정과 표현 방법을 분리**하여, 동일한 생성 절차에서 서로 다른 표현 결과를 만들 수 있게 하는 패턴.

**언제 사용하나**
- 객체 생성 절차(단계)는 같은데 **결과물의 모습이 다양**할 때
- 생성자 인자가 너무 많아서(텔레스코핑 생성자) 가독성이 떨어질 때
- 복잡한 객체를 단계별로 조립해야 할 때

**추가 설명**
- 역할: `Builder`(단계별 생성 인터페이스), `ConcreteBuilder`(실제 조립 + 결과 반환), `Director`(생성 순서 지휘), `Product`(결과물).
- 아래는 시험에 나오는 **GoF 정통 구조(Director 포함)** 입니다.
- 실무에서는 `new Computer.Builder().cpu("i7").ram("16GB").build()` 같은 **메서드 체이닝 빌더**(Effective Java 스타일, Lombok `@Builder`)를 더 많이 쓰는데, "생성 과정과 표현의 분리"라는 아이디어는 같습니다.

**시험 포인트**: "복합 객체", "생성 과정과 표현 방법의 분리", `Director`.

```java
// BuilderDemo.java
public class BuilderDemo {
    public static void main(String[] args) {
        Director director = new Director();

        House woodHouse = director.construct(new WoodHouseBuilder());
        House stoneHouse = director.construct(new StoneHouseBuilder());

        System.out.println(woodHouse);
        System.out.println(stoneHouse);
    }
}

// Product
class House {
    String foundation;
    String structure;
    String roof;

    @Override
    public String toString() {
        return "House{foundation=" + foundation + ", structure=" + structure + ", roof=" + roof + "}";
    }
}

// Builder
interface HouseBuilder {
    void buildFoundation();
    void buildStructure();
    void buildRoof();
    House getResult();
}

// ConcreteBuilder 1
class WoodHouseBuilder implements HouseBuilder {
    private final House house = new House();
    public void buildFoundation() { house.foundation = "자갈 기초"; }
    public void buildStructure()  { house.structure = "목재 프레임"; }
    public void buildRoof()       { house.roof = "기와 지붕"; }
    public House getResult()      { return house; }
}

// ConcreteBuilder 2
class StoneHouseBuilder implements HouseBuilder {
    private final House house = new House();
    public void buildFoundation() { house.foundation = "콘크리트 기초"; }
    public void buildStructure()  { house.structure = "석재 벽"; }
    public void buildRoof()       { house.roof = "슬레이트 지붕"; }
    public House getResult()      { return house; }
}

// Director : 생성 "순서"만 책임진다
class Director {
    House construct(HouseBuilder builder) {
        builder.buildFoundation();
        builder.buildStructure();
        builder.buildRoof();
        return builder.getResult();
    }
}
```

**실행 결과**
```
House{foundation=자갈 기초, structure=목재 프레임, roof=기와 지붕}
House{foundation=콘크리트 기초, structure=석재 벽, roof=슬레이트 지붕}
```

---

## 3. Factory Method (팩토리 메서드)

**시험 정의**
객체를 생성하기 위한 인터페이스를 정의하되, **어떤 클래스의 인스턴스를 생성할지는 서브클래스가 결정**하도록 하는 패턴. 클래스의 인스턴스 생성을 서브클래스에게 위임한다. **가상 생성자(Virtual Constructor) 패턴**이라고도 한다.

**언제 사용하나**
- 생성해야 할 객체의 클래스를 **미리 예측할 수 없을 때**
- 객체 생성 책임을 서브클래스에 맡겨 **확장에 열려 있게** 하고 싶을 때

**추가 설명**
- 역할: `Product`, `ConcreteProduct`, `Creator`(팩토리 메서드를 선언 + 그걸 사용하는 로직 보유), `ConcreteCreator`(팩토리 메서드 구현).
- 부모(Creator)가 **골격(Template Method)** 을 가지고, 자식이 **생성 부분만** 결정하는 구조라서 Template Method와 같이 쓰이는 경우가 많다.

**시험 포인트**: "서브클래스가 결정", "Virtual Constructor", 객체 생성 시 **상속**을 이용.

```java
// FactoryMethodDemo.java
public class FactoryMethodDemo {
    public static void main(String[] args) {
        Logistics road = new RoadLogistics();
        Logistics sea = new SeaLogistics();

        road.planDelivery();
        sea.planDelivery();
    }
}

// Product
interface Transport { void deliver(); }

// ConcreteProduct
class Truck implements Transport {
    public void deliver() { System.out.println("트럭으로 육로 배송"); }
}
class Ship implements Transport {
    public void deliver() { System.out.println("선박으로 해상 배송"); }
}

// Creator
abstract class Logistics {
    // 팩토리 메서드: 무엇을 만들지는 서브클래스가 결정
    abstract Transport createTransport();

    void planDelivery() {
        System.out.println("배송 계획 수립");
        Transport transport = createTransport();
        transport.deliver();
    }
}

// ConcreteCreator
class RoadLogistics extends Logistics {
    Transport createTransport() { return new Truck(); }
}
class SeaLogistics extends Logistics {
    Transport createTransport() { return new Ship(); }
}
```

**실행 결과**
```
배송 계획 수립
트럭으로 육로 배송
배송 계획 수립
선박으로 해상 배송
```

---

## 4. Prototype (프로토타입)

**시험 정의**
원형(Prototype)이 되는 인스턴스를 사용하여 생성할 객체의 종류를 명시하고, **이렇게 만든 견본을 복사(clone)해서 새로운 객체를 생성**하는 패턴.

**언제 사용하나**
- 객체 생성 비용이 크거나(DB 조회, 복잡한 초기화) 생성 과정이 복잡해서 **복제가 더 싸고 편할 때**
- 클래스가 런타임에 결정되는 경우
- 상태가 약간씩만 다른 객체가 많이 필요할 때

**추가 설명**
- 자바에서는 `Cloneable` + `clone()` 으로 구현한다.
- **얕은 복사(shallow copy)** 는 참조 필드를 공유하므로, `List` 같은 가변 필드가 있으면 **깊은 복사(deep copy)** 를 직접 해줘야 한다. (아래 예제에서 `pages`를 새로 복사)
- 복사 생성자나 직렬화로 복제하는 방법도 있다.

**시험 포인트**: "복제", "원형", `clone()`, 생성 비용 절감.

```java
// PrototypeDemo.java
import java.util.ArrayList;
import java.util.List;

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

    Document(String title) { this.title = title; }

    void setTitle(String title) { this.title = title; }
    void addPage(String page) { pages.add(page); }

    @Override
    public Document clone() {
        try {
            Document copy = (Document) super.clone();   // 얕은 복사
            copy.pages = new ArrayList<>(this.pages);   // 가변 필드는 깊은 복사
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public String toString() {
        return "Document{title=" + title + ", pages=" + pages + "}";
    }
}
```

**실행 결과**
```
Document{title=보고서 템플릿, pages=[1페이지]}
Document{title=2024 보고서, pages=[1페이지, 2페이지]}
```

---

## 5. Singleton (싱글턴)

**시험 정의**
클래스의 인스턴스가 **오직 하나만 생성**되도록 보장하고, 어디서든 그 인스턴스에 접근할 수 있는 **전역 접근점**을 제공하는 패턴.

**언제 사용하나**
- 설정 정보, 로거, 커넥션 풀, 스레드 풀, 캐시처럼 **프로그램 전체에서 하나만** 있어야 하는 객체
- 인스턴스가 여러 개 생기면 자원 낭비나 상태 불일치가 생기는 경우

**추가 설명**
- 생성자를 `private`으로 막고, `static` 메서드로 유일한 인스턴스를 반환한다.
- **멀티스레드 안전**이 중요하다. 방법: ① Holder(Initialization-on-demand) ② Double-Checked Locking(+`volatile`) ③ `enum` 싱글턴(직렬화·리플렉션에도 안전).
- Spring의 Bean 기본 스코프(singleton)는 "컨테이너당 하나"이므로 GoF 싱글턴과는 구현이 다르다.
- 단점: 전역 상태 → 테스트 어려움, 결합도 증가.

**시험 포인트**: "인스턴스 하나", "전역 접근점", `private` 생성자 + `getInstance()`.

```java
// SingletonDemo.java
public class SingletonDemo {
    public static void main(String[] args) {
        Singleton a = Singleton.getInstance();
        Singleton b = Singleton.getInstance();
        a.increase();
        b.increase();

        System.out.println("같은 인스턴스? " + (a == b));
        System.out.println("공유 카운터: " + a.getCount());

        EnumSingleton.INSTANCE.hello();
    }
}

// 방법 1: Holder 방식 (지연 초기화 + 스레드 안전, 가장 권장되는 클래식 방식)
class Singleton {
    private int count;

    private Singleton() { }   // 외부에서 new 금지

    private static class Holder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return Holder.INSTANCE;   // 이 시점에 클래스 로딩 → JVM이 스레드 안전 보장
    }

    public synchronized void increase() { count++; }
    public int getCount() { return count; }
}

// 방법 2: enum 싱글턴 (가장 간결하고 안전)
enum EnumSingleton {
    INSTANCE;
    public void hello() { System.out.println("enum 싱글턴 동작"); }
}
```

참고: Double-Checked Locking 방식

```java
class DclSingleton {
    private static volatile DclSingleton instance;   // volatile 필수
    private DclSingleton() { }

    public static DclSingleton getInstance() {
        if (instance == null) {                      // 1차 체크 (락 없이)
            synchronized (DclSingleton.class) {
                if (instance == null) {              // 2차 체크 (락 안에서)
                    instance = new DclSingleton();
                }
            }
        }
        return instance;
    }
}
```

**실행 결과**
```
같은 인스턴스? true
공유 카운터: 2
enum 싱글턴 동작
```

---

# Part 2. 구조(Structural) 패턴

---

## 6. Adapter (어댑터)

**시험 정의**
기존에 생성된 클래스를 재사용할 수 있도록 **중간에서 인터페이스를 변환**해 주는 패턴. 호환성이 없는 인터페이스 때문에 함께 동작할 수 없는 클래스들을 연결해 준다. **Wrapper 패턴**이라고도 한다.

**언제 사용하나**
- 기존(레거시, 외부 라이브러리) 클래스를 **수정하지 않고** 새 시스템의 인터페이스에 맞춰 쓰고 싶을 때
- 인터페이스가 달라서 직접 연결이 안 되는 클래스들을 같이 쓰고 싶을 때

**추가 설명**
- 두 가지 방식: **객체 어댑터**(합성/위임, 아래 예제, 권장) vs **클래스 어댑터**(상속, 자바는 다중 상속이 안 돼서 제약이 큼).
- 역할: `Target`(클라이언트가 원하는 인터페이스), `Adaptee`(기존 클래스), `Adapter`(변환기).
- 해외 직구 가전을 220V 콘센트에 꽂기 위한 "돼지코(변환 어댑터)"가 대표 비유.

**시험 포인트**: "Wrapper", "인터페이스 변환", "기존 코드 수정 없이 재사용".

```java
// AdapterDemo.java
public class AdapterDemo {
    public static void main(String[] args) {
        // 클라이언트는 KoreanPlug 인터페이스만 안다
        KoreanPlug plug = new PowerAdapter(new USDevice());
        plug.plug220V();
    }
}

// Target: 클라이언트가 기대하는 인터페이스
interface KoreanPlug {
    void plug220V();
}

// Adaptee: 이미 존재하는, 수정하고 싶지 않은 클래스
class USDevice {
    void plug110V() { System.out.println("미국산 기기: 110V 전원으로 동작"); }
}

// Adapter: Target을 구현하면서 Adaptee를 감싼다
class PowerAdapter implements KoreanPlug {
    private final USDevice device;

    PowerAdapter(USDevice device) { this.device = device; }

    @Override
    public void plug220V() {
        System.out.println("어댑터: 220V -> 110V 변환");
        device.plug110V();
    }
}
```

**실행 결과**
```
어댑터: 220V -> 110V 변환
미국산 기기: 110V 전원으로 동작
```

---

## 7. Bridge (브리지)

**시험 정의**
**구현부(Implementation)에서 추상층(Abstraction)을 분리**하여 각자 독립적으로 확장(변형)할 수 있도록 하는 패턴. 기능 클래스 계층과 구현 클래스 계층을 **다리(Bridge)** 로 연결한다.

**언제 사용하나**
- 추상화와 구현이 모두 **독립적으로 확장**되어야 할 때 (클래스 폭발 방지)
- 예: `리모컨 종류(2) × 기기 종류(3)` 조합을 `2×3=6`개 클래스로 만들지 않고 `2+3=5`개로 해결
- 구현을 **런타임에 교체**하고 싶을 때

**추가 설명**
- 역할: `Abstraction`(기능 계층 최상위, `Implementor`를 **포함**), `RefinedAbstraction`(기능 추가), `Implementor`(구현 인터페이스), `ConcreteImplementor`(실제 구현).
- 상속(is-a) 대신 **위임/합성(has-a)** 으로 두 계층을 잇는 것이 핵심.
- Adapter는 **이미 만들어진** 것들을 맞추는 사후 처방, Bridge는 **처음부터** 분리해서 설계하는 사전 설계라는 점이 다르다.

**시험 포인트**: "구현부에서 추상층을 분리", "독립적 확장", "기능 클래스 계층과 구현 클래스 계층".

```java
// BridgeDemo.java
public class BridgeDemo {
    public static void main(String[] args) {
        RemoteControl tvRemote = new RemoteControl(new Tv());
        tvRemote.togglePower();
        tvRemote.volumeUp();

        AdvancedRemote radioRemote = new AdvancedRemote(new Radio());
        radioRemote.togglePower();
        radioRemote.mute();
    }
}

// Implementor
interface Device {
    boolean isEnabled();
    void enable();
    void disable();
    int getVolume();
    void setVolume(int volume);
}

// ConcreteImplementor들
class BaseDevice implements Device {
    private final String name;
    private boolean on = false;
    private int volume = 30;

    BaseDevice(String name) { this.name = name; }

    public boolean isEnabled() { return on; }
    public void enable()  { on = true;  System.out.println(name + " 전원 ON"); }
    public void disable() { on = false; System.out.println(name + " 전원 OFF"); }
    public int getVolume() { return volume; }
    public void setVolume(int v) {
        volume = Math.max(0, Math.min(100, v));
        System.out.println(name + " 볼륨: " + volume);
    }
}
class Tv extends BaseDevice    { Tv()    { super("TV"); } }
class Radio extends BaseDevice { Radio() { super("라디오"); } }

// Abstraction : 구현(Device)을 "포함"해서 위임한다 -> 이것이 Bridge
class RemoteControl {
    protected final Device device;

    RemoteControl(Device device) { this.device = device; }

    void togglePower() {
        if (device.isEnabled()) device.disable(); else device.enable();
    }
    void volumeUp() { device.setVolume(device.getVolume() + 10); }
}

// RefinedAbstraction : 기능 계층만 독립적으로 확장
class AdvancedRemote extends RemoteControl {
    AdvancedRemote(Device device) { super(device); }

    void mute() { device.setVolume(0); }
}
```

**실행 결과**
```
TV 전원 ON
TV 볼륨: 40
라디오 전원 ON
라디오 볼륨: 0
```

---

## 8. Composite (컴포지트)

**시험 정의**
여러 개의 객체들로 구성된 **복합 객체와 단일 객체를 클라이언트에서 구별 없이 다루기 위한** 패턴. 객체들을 **트리 구조(부분-전체 계층)** 로 구성한다.

**언제 사용하나**
- 파일/폴더, 메뉴/하위 메뉴, 조직도, GUI 컴포넌트처럼 **부분-전체 계층 구조**를 표현할 때
- 개별 객체와 그룹 객체를 **동일하게(같은 인터페이스로)** 다루고 싶을 때

**추가 설명**
- 역할: `Component`(공통 인터페이스), `Leaf`(단일 객체), `Composite`(자식 `Component`들을 보유하는 복합 객체).
- `Composite`가 `Component` 리스트를 가지므로 **재귀적** 구조가 된다.

**시험 포인트**: "복합 객체와 단일 객체를 동일하게", "트리 구조", "재귀적 합성". 폴더-파일이 단골 예시.

```java
// CompositeDemo.java
import java.util.ArrayList;
import java.util.List;

public class CompositeDemo {
    public static void main(String[] args) {
        Directory root = new Directory("root");
        root.add(new File("a.txt", 100));

        Directory src = new Directory("src");
        src.add(new File("Main.java", 300));
        src.add(new File("Util.java", 200));
        root.add(src);

        root.print("");   // 폴더든 파일이든 똑같이 print() 호출
    }
}

// Component
interface FileSystemItem {
    int getSize();
    void print(String indent);
}

// Leaf
class File implements FileSystemItem {
    private final String name;
    private final int size;

    File(String name, int size) { this.name = name; this.size = size; }

    public int getSize() { return size; }
    public void print(String indent) {
        System.out.println(indent + "[F] " + name + " (" + size + ")");
    }
}

// Composite
class Directory implements FileSystemItem {
    private final String name;
    private final List<FileSystemItem> children = new ArrayList<>();

    Directory(String name) { this.name = name; }

    void add(FileSystemItem item) { children.add(item); }

    public int getSize() {
        int total = 0;
        for (FileSystemItem child : children) total += child.getSize();   // 재귀
        return total;
    }

    public void print(String indent) {
        System.out.println(indent + "[D] " + name + " (" + getSize() + ")");
        for (FileSystemItem child : children) child.print(indent + "  ");
    }
}
```

**실행 결과**
```
[D] root (600)
  [F] a.txt (100)
  [D] src (500)
    [F] Main.java (300)
    [F] Util.java (200)
```

---

## 9. Decorator (데코레이터)

**시험 정의**
객체에 **동적으로 새로운 서비스(기능/책임)를 추가**할 수 있게 하며, 기능 확장이 필요할 때 **서브클래싱(상속) 대신 사용할 수 있는 유연한 대안**을 제공하는 패턴.

**언제 사용하나**
- 기능 조합이 많아서 상속으로 처리하면 **클래스가 폭발**할 때 (예: 커피 + 우유 + 샷 + 시럽...)
- **런타임에** 객체에 기능을 붙였다 뗐다 하고 싶을 때
- 기존 클래스를 수정하지 않고 기능을 덧붙이고 싶을 때

**추가 설명**
- 역할: `Component`(공통 인터페이스), `ConcreteComponent`(원본), `Decorator`(Component를 **포함**하며 같은 인터페이스 구현), `ConcreteDecorator`(기능 추가).
- 자바 `BufferedReader(new InputStreamReader(System.in))` 같은 **I/O 스트림**이 실제 사례.
- Proxy와 구조가 비슷하지만 목적이 다르다 → Decorator는 **기능 추가**, Proxy는 **접근 제어**.

**시험 포인트**: "동적으로 기능 추가", "상속의 대안", "겹겹이 감싸기".

```java
// DecoratorDemo.java
public class DecoratorDemo {
    public static void main(String[] args) {
        Coffee coffee = new Americano();
        System.out.println(coffee.description() + " = " + coffee.cost() + "원");

        coffee = new MilkDecorator(coffee);   // 우유 추가
        coffee = new ShotDecorator(coffee);   // 샷 추가
        coffee = new ShotDecorator(coffee);   // 샷 한 번 더
        System.out.println(coffee.description() + " = " + coffee.cost() + "원");
    }
}

// Component
interface Coffee {
    String description();
    int cost();
}

// ConcreteComponent
class Americano implements Coffee {
    public String description() { return "아메리카노"; }
    public int cost() { return 2500; }
}

// Decorator : Coffee를 구현하면서 Coffee를 포함
abstract class CoffeeDecorator implements Coffee {
    protected final Coffee coffee;
    CoffeeDecorator(Coffee coffee) { this.coffee = coffee; }
}

// ConcreteDecorator
class MilkDecorator extends CoffeeDecorator {
    MilkDecorator(Coffee coffee) { super(coffee); }
    public String description() { return coffee.description() + " + 우유"; }
    public int cost() { return coffee.cost() + 500; }
}

class ShotDecorator extends CoffeeDecorator {
    ShotDecorator(Coffee coffee) { super(coffee); }
    public String description() { return coffee.description() + " + 샷 추가"; }
    public int cost() { return coffee.cost() + 300; }
}
```

**실행 결과**
```
아메리카노 = 2500원
아메리카노 + 우유 + 샷 추가 + 샷 추가 = 3600원
```

---

## 10. Facade (퍼사드)

**시험 정의**
서브시스템에 있는 **인터페이스 집합에 대해 하나의 통합된 인터페이스(고수준 인터페이스)** 를 제공하여, 서브시스템을 더 쉽게 사용할 수 있도록 하는 패턴.

**언제 사용하나**
- 복잡한 서브시스템을 **간단한 창구 하나**로 감싸서 사용 편의성을 높이고 싶을 때
- 클라이언트와 서브시스템 사이의 **결합도를 낮추고** 싶을 때
- 레이어(계층) 구조에서 각 계층의 진입점을 만들 때

**추가 설명**
- 서브시스템 클래스들은 Facade의 존재를 모른다. Facade가 내부 객체들을 **순서대로 조합**해서 호출해 준다.
- Facade가 서브시스템 접근을 **막는 것은 아니다**(필요하면 직접 써도 됨).
- Spring의 `Service` 계층이 여러 Repository/외부 API를 묶어 하나의 비즈니스 기능으로 제공하는 것도 Facade와 유사하다.

**시험 포인트**: "통합된 인터페이스", "서브시스템", "사용 편의성/단순화".

```java
// FacadeDemo.java
public class FacadeDemo {
    public static void main(String[] args) {
        HomeTheaterFacade theater = new HomeTheaterFacade(
                new Projector(), new Amplifier(), new Player());

        theater.watchMovie("인셉션");   // 클라이언트는 이 한 줄만 알면 된다
        theater.endMovie();
    }
}

// 서브시스템들
class Projector {
    void on()  { System.out.println("프로젝터 ON"); }
    void off() { System.out.println("프로젝터 OFF"); }
}
class Amplifier {
    void on()  { System.out.println("앰프 ON"); }
    void off() { System.out.println("앰프 OFF"); }
}
class Player {
    void play(String title) { System.out.println("플레이어: '" + title + "' 재생"); }
    void stop()             { System.out.println("플레이어 정지"); }
}

// Facade
class HomeTheaterFacade {
    private final Projector projector;
    private final Amplifier amplifier;
    private final Player player;

    HomeTheaterFacade(Projector projector, Amplifier amplifier, Player player) {
        this.projector = projector;
        this.amplifier = amplifier;
        this.player = player;
    }

    void watchMovie(String title) {
        System.out.println("=== 영화 시작 ===");
        projector.on();
        amplifier.on();
        player.play(title);
    }

    void endMovie() {
        System.out.println("=== 영화 종료 ===");
        player.stop();
        amplifier.off();
        projector.off();
    }
}
```

**실행 결과**
```
=== 영화 시작 ===
프로젝터 ON
앰프 ON
플레이어: '인셉션' 재생
=== 영화 종료 ===
플레이어 정지
앰프 OFF
프로젝터 OFF
```

---

## 11. Flyweight (플라이웨이트)

**시험 정의**
**크기가 작은 객체들이 여러 개 있을 때**, 이들을 **공유**하여 사용함으로써 메모리 사용량을 줄이고 비용 효율을 높이는 패턴.

**언제 사용하나**
- 똑같거나 비슷한 객체가 **대량으로** 필요해서 메모리가 부담될 때 (문서의 글자 하나하나, 게임의 나무/총알, 지도의 마커)
- 객체의 상태를 **공유 가능한 부분**과 **공유 불가능한 부분**으로 나눌 수 있을 때

**추가 설명**
- 핵심 개념:
  - **Intrinsic State(내재 상태)**: 공유되는 불변 값 (나무 종류, 색상, 텍스처) → Flyweight 객체 안에 저장
  - **Extrinsic State(외재 상태)**: 객체마다 다른 값 (좌표) → 클라이언트가 전달
- `FlyweightFactory`가 풀(pool/cache)을 관리하며 이미 있으면 재사용한다.
- 자바의 `String` 상수 풀, `Integer.valueOf()`(-128~127 캐시)가 유사한 사례.

**시험 포인트**: "공유", "메모리 절약", "작은 객체 다수", 내재/외재 상태.

```java
// FlyweightDemo.java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlyweightDemo {
    public static void main(String[] args) {
        List<Tree> forest = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            boolean even = (i % 2 == 0);
            TreeType type = TreeFactory.getTreeType(even ? "소나무" : "참나무",
                                                    even ? "초록" : "갈색");
            forest.add(new Tree(i, i * 2, type));   // 좌표만 다르고 type은 공유
        }

        forest.get(0).draw();
        forest.get(1).draw();
        System.out.println("생성된 나무 수: " + forest.size());
        System.out.println("공유되는 TreeType 수: " + TreeFactory.getCount());
    }
}

// Flyweight : 공유되는 내재 상태(이름, 색상)
class TreeType {
    private final String name;
    private final String color;

    TreeType(String name, String color) { this.name = name; this.color = color; }

    void draw(int x, int y) {   // x, y는 외부에서 받는 외재 상태
        System.out.println(name + "(" + color + ")를 (" + x + ", " + y + ")에 그림");
    }
}

// FlyweightFactory : 풀 관리
class TreeFactory {
    private static final Map<String, TreeType> pool = new HashMap<>();

    static TreeType getTreeType(String name, String color) {
        return pool.computeIfAbsent(name + "/" + color, k -> new TreeType(name, color));
    }
    static int getCount() { return pool.size(); }
}

// Context : 외재 상태(좌표)를 가진 객체
class Tree {
    private final int x, y;
    private final TreeType type;

    Tree(int x, int y, TreeType type) { this.x = x; this.y = y; this.type = type; }

    void draw() { type.draw(x, y); }
}
```

**실행 결과**
```
소나무(초록)를 (0, 0)에 그림
참나무(갈색)를 (1, 2)에 그림
생성된 나무 수: 1000
공유되는 TreeType 수: 2
```

---

## 12. Proxy (프록시)

**시험 정의**
**접근이 어렵거나 비용이 큰 객체를 대신하는 대리자(Surrogate/Placeholder)** 를 제공하여, 실제 객체에 대한 접근을 **제어**하는 패턴.

**언제 사용하나**
- **가상 프록시**: 생성 비용이 큰 객체를 실제로 필요할 때까지 지연 생성(Lazy Loading)
- **보호 프록시**: 접근 권한을 검사
- **원격 프록시**: 원격지(다른 서버) 객체를 로컬 객체처럼 사용 (RMI, gRPC 스텁)
- **캐싱 프록시 / 로깅 프록시** 등

**추가 설명**
- 프록시와 실제 객체는 **같은 인터페이스**를 구현해서 클라이언트는 차이를 모른다.
- Spring AOP(`@Transactional`, `@Async`), JPA의 지연 로딩(Hibernate 프록시)이 대표적인 프록시 사례.
- Decorator와 구조는 같지만 **목적**이 다르다: Decorator = 기능 **추가**, Proxy = 접근 **제어**.

**시험 포인트**: "대리자", "접근 제어", "지연 생성(Lazy)".

```java
// ProxyDemo.java
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

// Subject
interface Image { void display(); }

// RealSubject : 생성 비용이 큰 객체
class RealImage implements Image {
    private final String fileName;

    RealImage(String fileName) {
        this.fileName = fileName;
        System.out.println("[RealImage] " + fileName + " 디스크에서 로딩...");   // 무거운 작업
    }

    public void display() { System.out.println(fileName + " 화면에 표시"); }
}

// Proxy : 실제 객체 생성/접근을 통제
class ProxyImage implements Image {
    private final String fileName;
    private RealImage realImage;   // 처음엔 null

    ProxyImage(String fileName) { this.fileName = fileName; }

    public void display() {
        if (realImage == null) {                 // 필요한 순간에만 생성 (Lazy)
            realImage = new RealImage(fileName);
        }
        realImage.display();
    }
}
```

**실행 결과**
```
이미지 객체 생성 완료 (아직 로딩 안 함)
-- 첫 번째 display --
[RealImage] photo.jpg 디스크에서 로딩...
photo.jpg 화면에 표시
-- 두 번째 display --
photo.jpg 화면에 표시
```

---

# Part 3. 행위(Behavioral) 패턴

---

## 13. Chain of Responsibility (책임 연쇄)

**시험 정의**
요청을 처리할 수 있는 **기회를 둘 이상의 객체에게 부여**함으로써, 요청을 보내는 객체와 받아 처리하는 객체 간의 **결합도를 없애는** 패턴. 처리 객체들을 **체인(사슬)** 으로 연결하고, 처리될 때까지 체인을 따라 요청을 전달한다.

**언제 사용하나**
- 하나의 요청을 **여러 객체 중 하나**(또는 여럿)가 처리할 수 있고, 누가 처리할지 **미리 알 수 없을 때**
- 결재 라인(팀장 → 부장 → 대표), 로그 레벨 처리, 서블릿 필터/Spring Security 필터 체인, 예외 처리 핸들러

**추가 설명**
- 역할: `Handler`(요청 처리 인터페이스 + 다음 핸들러 참조), `ConcreteHandler`(처리 가능하면 처리, 아니면 `next`에 전달), `Client`.
- 체인 구성은 런타임에 바꿀 수 있다. 단, 끝까지 아무도 처리하지 못하는 경우를 대비해야 한다.

**시험 포인트**: "요청을 처리할 기회를 둘 이상의 객체에 부여", "송신자-수신자 결합도 제거", "체인".

```java
// ChainOfResponsibilityDemo.java
public class ChainOfResponsibilityDemo {
    public static void main(String[] args) {
        Approver teamLead = new Approver("팀장", 1_000_000);
        Approver manager  = new Approver("부장", 5_000_000);
        Approver ceo      = new Approver("대표", 50_000_000);

        teamLead.setNext(manager).setNext(ceo);   // 체인 구성

        teamLead.handle(800_000);
        teamLead.handle(3_000_000);
        teamLead.handle(20_000_000);
        teamLead.handle(100_000_000);
    }
}

// Handler + ConcreteHandler (필드로 권한만 다르게)
class Approver {
    private final String name;
    private final int limit;
    private Approver next;

    Approver(String name, int limit) { this.name = name; this.limit = limit; }

    Approver setNext(Approver next) {
        this.next = next;
        return next;   // 체이닝용
    }

    void handle(int amount) {
        if (amount <= limit) {
            System.out.println(name + " 승인: " + amount + "원");
        } else if (next != null) {
            next.handle(amount);      // 내가 못 하면 다음 사람에게
        } else {
            System.out.println("승인 불가: " + amount + "원 (결재 라인 초과)");
        }
    }
}
```

**실행 결과**
```
팀장 승인: 800000원
부장 승인: 3000000원
대표 승인: 20000000원
승인 불가: 100000000원 (결재 라인 초과)
```

---

## 14. Command (커맨드)

**시험 정의**
**요청 자체를 객체로 캡슐화**하여, 서로 다른 요청을 가진 클라이언트를 매개변수화하고, 요청을 **큐에 저장하거나 로깅**하며, **실행 취소(undo)** 를 지원할 수 있게 하는 패턴.

**언제 사용하나**
- 요청을 **저장/전달/지연 실행**해야 할 때 (작업 큐, 스케줄러, 매크로)
- **Undo/Redo** 기능이 필요할 때
- 요청을 보내는 쪽(Invoker)과 수행하는 쪽(Receiver)을 분리하고 싶을 때

**추가 설명**
- 역할: `Command`(execute/undo 인터페이스), `ConcreteCommand`(Receiver와 동작을 묶음), `Receiver`(실제 일을 하는 객체), `Invoker`(커맨드를 실행), `Client`.
- 리모컨 버튼(Invoker) ↔ 전등(Receiver) 사이에 "ON 명령" 객체가 끼어 있는 구조.

**시험 포인트**: "요청을 객체로 캡슐화", "Undo", "큐/로그".

```java
// CommandDemo.java
import java.util.ArrayDeque;
import java.util.Deque;

public class CommandDemo {
    public static void main(String[] args) {
        Light light = new Light();                  // Receiver
        RemoteInvoker remote = new RemoteInvoker(); // Invoker

        remote.press(new LightOnCommand(light));
        remote.press(new LightOffCommand(light));

        remote.undo();
        remote.undo();
        remote.undo();
    }
}

// Receiver
class Light {
    void on()  { System.out.println("조명 ON"); }
    void off() { System.out.println("조명 OFF"); }
}

// Command
interface Command {
    void execute();
    void undo();
}

// ConcreteCommand
class LightOnCommand implements Command {
    private final Light light;
    LightOnCommand(Light light) { this.light = light; }
    public void execute() { light.on(); }
    public void undo()    { light.off(); }
}
class LightOffCommand implements Command {
    private final Light light;
    LightOffCommand(Light light) { this.light = light; }
    public void execute() { light.off(); }
    public void undo()    { light.on(); }
}

// Invoker : 실행한 명령을 기록해서 undo 지원
class RemoteInvoker {
    private final Deque<Command> history = new ArrayDeque<>();

    void press(Command command) {
        command.execute();
        history.push(command);
    }

    void undo() {
        System.out.println("-- undo --");
        if (history.isEmpty()) {
            System.out.println("되돌릴 명령이 없습니다");
            return;
        }
        history.pop().undo();
    }
}
```

**실행 결과**
```
조명 ON
조명 OFF
-- undo --
조명 ON
-- undo --
조명 OFF
-- undo --
되돌릴 명령이 없습니다
```

---

## 15. Interpreter (인터프리터)

**시험 정의**
언어에 대한 **문법을 정의**하고, 그 문법을 사용하여 **문장(표현식)을 해석하는 해석기**를 만드는 패턴.

**언제 사용하나**
- 간단한 **언어/문법/규칙**을 해석해야 할 때 (수식 계산, 정규식, SQL 같은 질의, 검색 조건식, 규칙 엔진)
- 문법이 단순하고 자주 바뀔 때

**추가 설명**
- 역할: `AbstractExpression`(interpret 선언), `TerminalExpression`(숫자, 변수 같은 **종단 기호**), `NonterminalExpression`(+, − 같은 **비종단 기호**, 하위 표현식을 조합), `Context`(변수 값 등 해석에 필요한 정보).
- 문법 규칙 하나 = 클래스 하나. 문법이 복잡해지면 클래스가 폭증하므로 **복잡한 문법에는 부적합**하다(파서 생성기를 쓰는 편이 낫다).
- 표현식이 **Composite 패턴**의 트리 구조와 같은 형태를 가진다.

**시험 포인트**: "문법", "문장 해석", "종단/비종단 표현".

```java
// InterpreterDemo.java
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class InterpreterDemo {
    public static void main(String[] args) {
        Map<String, Integer> context = Map.of("x", 10, "y", 5);   // Context

        String[] sentences = { "x y + 3 -", "x 2 -" };            // 후위 표기식
        for (String s : sentences) {
            Expression expr = Parser.parse(s);                     // 문장 -> 표현식 트리
            System.out.println(s + " = " + expr.interpret(context));
        }
    }
}

// AbstractExpression
interface Expression {
    int interpret(Map<String, Integer> context);
}

// TerminalExpression : 숫자
class NumberExpr implements Expression {
    private final int value;
    NumberExpr(int value) { this.value = value; }
    public int interpret(Map<String, Integer> context) { return value; }
}

// TerminalExpression : 변수
class VariableExpr implements Expression {
    private final String name;
    VariableExpr(String name) { this.name = name; }
    public int interpret(Map<String, Integer> context) { return context.get(name); }
}

// NonterminalExpression : 덧셈
class AddExpr implements Expression {
    private final Expression left, right;
    AddExpr(Expression left, Expression right) { this.left = left; this.right = right; }
    public int interpret(Map<String, Integer> context) {
        return left.interpret(context) + right.interpret(context);
    }
}

// NonterminalExpression : 뺄셈
class SubExpr implements Expression {
    private final Expression left, right;
    SubExpr(Expression left, Expression right) { this.left = left; this.right = right; }
    public int interpret(Map<String, Integer> context) {
        return left.interpret(context) - right.interpret(context);
    }
}

// 문장을 표현식 트리로 만들어 주는 파서 (후위 표기법)
class Parser {
    static Expression parse(String postfix) {
        Deque<Expression> stack = new ArrayDeque<>();
        for (String token : postfix.trim().split("\\s+")) {
            if (token.equals("+")) {
                Expression right = stack.pop();
                Expression left = stack.pop();
                stack.push(new AddExpr(left, right));
            } else if (token.equals("-")) {
                Expression right = stack.pop();
                Expression left = stack.pop();
                stack.push(new SubExpr(left, right));
            } else if (token.matches("\\d+")) {
                stack.push(new NumberExpr(Integer.parseInt(token)));
            } else {
                stack.push(new VariableExpr(token));
            }
        }
        return stack.pop();
    }
}
```

**실행 결과**
```
x y + 3 - = 12
x 2 - = 8
```

---

## 16. Iterator (반복자)

**시험 정의**
**내부 표현 방법을 노출하지 않고** 복합 객체(집합 객체)의 원소들을 **순차적으로 접근**할 수 있는 방법을 제공하는 패턴.

**언제 사용하나**
- 컬렉션의 내부 구조(배열, 리스트, 트리…)와 상관없이 **같은 방식으로 순회**하고 싶을 때
- 하나의 컬렉션에 대해 **여러 가지 순회 방식**(정순, 역순, 필터링)을 제공하고 싶을 때

**추가 설명**
- 역할: `Iterator`(hasNext/next), `ConcreteIterator`(순회 위치 관리), `Aggregate`(Iterator 생성 메서드), `ConcreteAggregate`.
- 자바의 `java.util.Iterator` / `Iterable` 이 바로 이 패턴이며, `for-each` 문이 이를 활용한다. 아래 예제는 패턴 구조가 드러나도록 **배열 기반 컬렉션**을 직접 구현한다.

**시험 포인트**: "내부 표현을 노출하지 않고", "순차적 접근", `hasNext()`/`next()`.

```java
// IteratorDemo.java
import java.util.Iterator;

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
```

**실행 결과**
```
Java의 정석
클린 코드
이펙티브 자바
```

---

## 17. Mediator (중재자)

**시험 정의**
한 집합에 속해 있는 객체들의 **상호작용을 캡슐화하는 객체(중재자)를 정의**하여, 객체들이 **서로를 직접 참조하지 않게** 함으로써 결합도를 낮추는 패턴.

**언제 사용하나**
- 객체들이 서로 복잡하게 얽혀(N:N) 있어 **의존 관계가 스파게티**가 될 때
- 채팅방, 항공 관제탑, GUI 다이얼로그(버튼·입력창·체크박스가 서로 영향) 같이 **상호작용 로직을 한 곳에 모으고** 싶을 때

**추가 설명**
- 역할: `Mediator`(통신 인터페이스), `ConcreteMediator`(동료 객체 관리 + 조정 로직), `Colleague`(동료, 중재자만 알고 있음).
- N:N 관계를 **1:N** 으로 단순화한다. 단점: 중재자가 비대해져 **God Object**가 될 수 있다.
- Observer와 비교: Observer는 **일방향 통지**(Subject → Observer), Mediator는 **양방향 조정**을 중재자가 책임진다.

**시험 포인트**: "상호작용 캡슐화", "직접 참조 금지", "결합도 감소", "관제탑".

```java
// MediatorDemo.java
import java.util.ArrayList;
import java.util.List;

public class MediatorDemo {
    public static void main(String[] args) {
        ChatMediator room = new ChatRoom();

        User hong = new User("홍길동", room);
        User kim  = new User("김철수", room);
        User lee  = new User("이영희", room);
        room.addUser(hong);
        room.addUser(kim);
        room.addUser(lee);

        hong.send("안녕하세요");     // 서로를 직접 모르고 중재자(ChatRoom)만 안다
        kim.send("반갑습니다");
    }
}

// Mediator
interface ChatMediator {
    void addUser(User user);
    void send(String message, User from);
}

// ConcreteMediator
class ChatRoom implements ChatMediator {
    private final List<User> users = new ArrayList<>();

    public void addUser(User user) { users.add(user); }

    public void send(String message, User from) {
        for (User user : users) {
            if (user != from) {                       // 보낸 사람 제외 전달
                user.receive(from.getName() + ": " + message);
            }
        }
    }
}

// Colleague
class User {
    private final String name;
    private final ChatMediator mediator;

    User(String name, ChatMediator mediator) { this.name = name; this.mediator = mediator; }

    String getName() { return name; }
    void send(String message) { mediator.send(message, this); }
    void receive(String message) { System.out.println("[" + name + " 수신] " + message); }
}
```

**실행 결과**
```
[김철수 수신] 홍길동: 안녕하세요
[이영희 수신] 홍길동: 안녕하세요
[홍길동 수신] 김철수: 반갑습니다
[이영희 수신] 김철수: 반갑습니다
```

---

## 18. Memento (메멘토)

**시험 정의**
**캡슐화를 위배하지 않으면서** 객체의 내부 상태를 외부에 저장하고, 필요할 때 **그 상태로 복구**할 수 있게 하는 패턴.

**언제 사용하나**
- **Undo(실행 취소)**, 체크포인트, 세이브/로드, 트랜잭션 롤백이 필요할 때
- 객체의 내부 상태를 외부에 노출하지 않고 스냅샷을 저장해야 할 때

**추가 설명**
- 역할: `Originator`(상태를 가진 원본, Memento 생성/복원), `Memento`(상태 스냅샷, **Originator만** 내용을 볼 수 있음), `Caretaker`(Memento를 보관만 하고 내용은 모름).
- 아래 예제처럼 `Memento`를 Originator의 **중첩 클래스**로 만들고 필드를 `private`으로 두면 Caretaker가 내용을 건드릴 수 없다(캡슐화 유지).
- Command 패턴의 undo와 함께 자주 쓰인다(Command는 "동작"을 되돌리고, Memento는 "상태"를 복원).

**시험 포인트**: "캡슐화 위배 없이 상태 저장", "복원", Undo.

```java
// MementoDemo.java
import java.util.ArrayDeque;
import java.util.Deque;

public class MementoDemo {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();      // Originator
        History history = new History();           // Caretaker

        editor.type("Hello");
        history.push(editor.save());

        editor.type(", World");
        history.push(editor.save());

        editor.type("!!!");
        System.out.println("현재: " + editor.getContent());

        editor.restore(history.pop());
        System.out.println("undo 1: " + editor.getContent());

        editor.restore(history.pop());
        System.out.println("undo 2: " + editor.getContent());
    }
}

// Originator
class TextEditor {
    private String content = "";

    void type(String text) { content += text; }
    String getContent() { return content; }

    Memento save() { return new Memento(content); }
    void restore(Memento memento) { this.content = memento.content; }

    // Memento : 외부에서는 내용을 볼 수 없다 (private 필드)
    static class Memento {
        private final String content;
        private Memento(String content) { this.content = content; }
    }
}

// Caretaker : Memento를 보관만 한다
class History {
    private final Deque<TextEditor.Memento> stack = new ArrayDeque<>();

    void push(TextEditor.Memento memento) { stack.push(memento); }
    TextEditor.Memento pop() { return stack.pop(); }
}
```

**실행 결과**
```
현재: Hello, World!!!
undo 1: Hello, World
undo 2: Hello
```

---

## 19. Observer (옵서버)

**시험 정의**
객체 사이에 **일대다(1:N) 의존 관계**를 정의하여, 한 객체의 **상태가 변하면 그 객체에 의존하는 다른 객체들에게 자동으로 통지**하고 갱신되도록 하는 패턴. **Publish-Subscribe 패턴**이라고도 한다.

**언제 사용하나**
- 한 객체의 변경이 **다른 여러 객체**의 변경을 필요로 하는데, 몇 개가 영향받는지 **미리 알 수 없을 때**
- 이벤트 처리, 뉴스 구독, 주가 알림, MVC의 Model → View 갱신

**추가 설명**
- 역할: `Subject`(관찰 대상, 옵저버 등록/해제/통지), `Observer`(갱신 인터페이스 `update`), `ConcreteSubject`, `ConcreteObserver`.
- Subject는 Observer의 **구체 클래스를 모르고** 인터페이스만 안다 → 느슨한 결합.
- 자바 표준의 `java.util.Observable`은 **Java 9부터 deprecated** 이므로 직접 구현하거나 `PropertyChangeListener`, 리액티브 라이브러리를 쓴다.

**시험 포인트**: "일대다 의존", "상태 변화 시 자동 통지", "Publish-Subscribe", MVC.

```java
// ObserverDemo.java
import java.util.ArrayList;
import java.util.List;

public class ObserverDemo {
    public static void main(String[] args) {
        NewsAgency agency = new NewsAgency();
        Subscriber cheolsu = new Subscriber("철수");
        Subscriber younghee = new Subscriber("영희");

        agency.attach(cheolsu);
        agency.attach(younghee);
        agency.publish("속보: 정보처리기사 합격 발표");

        System.out.println("-- 영희 구독 취소 --");
        agency.detach(younghee);
        agency.publish("속보: 다음 회차 접수 시작");
    }
}

// Observer
interface Observer {
    void update(String news);
}

// Subject
interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers();
}

// ConcreteSubject
class NewsAgency implements Subject {
    private final List<Observer> observers = new ArrayList<>();
    private String latestNews;

    public void attach(Observer o) { observers.add(o); }
    public void detach(Observer o) { observers.remove(o); }

    public void notifyObservers() {
        for (Observer o : observers) o.update(latestNews);
    }

    void publish(String news) {
        this.latestNews = news;   // 상태 변경
        notifyObservers();        // 자동 통지
    }
}

// ConcreteObserver
class Subscriber implements Observer {
    private final String name;
    Subscriber(String name) { this.name = name; }

    public void update(String news) {
        System.out.println("[" + name + "] 새 소식 수신: " + news);
    }
}
```

**실행 결과**
```
[철수] 새 소식 수신: 속보: 정보처리기사 합격 발표
[영희] 새 소식 수신: 속보: 정보처리기사 합격 발표
-- 영희 구독 취소 --
[철수] 새 소식 수신: 속보: 다음 회차 접수 시작
```

---

## 20. State (상태)

**시험 정의**
객체의 **내부 상태가 변경될 때 행위(동작)도 달라지도록** 하는 패턴. 객체는 마치 **자신의 클래스가 바뀐 것처럼** 보인다. 상태를 **클래스로 캡슐화**하여 상태별 동작을 분리한다.

**언제 사용하나**
- 객체의 행동이 **상태에 따라 달라지고**, 상태 전이가 복잡해서 `if / switch` 분기문이 여기저기 늘어날 때
- 주문(접수→결제→배송→완료), 자판기, 미디어 플레이어, TCP 연결 상태 등

**추가 설명**
- 역할: `Context`(현재 State 보유, 요청을 State에 위임), `State`(상태별 동작 인터페이스), `ConcreteState`(상태별 동작 구현 + **다음 상태로 전이**).
- Strategy와 구조(Context + 인터페이스 + 구현체들)가 똑같지만 의도가 다르다 → State는 **상태가 스스로 전이**되며 Context의 동작이 자동으로 바뀌고, Strategy는 **클라이언트가 알고리즘을 선택**한다.

**시험 포인트**: "상태에 따라 행위 변경", "상태를 클래스로 캡슐화", 조건문 제거.

```java
// StateDemo.java
public class StateDemo {
    public static void main(String[] args) {
        Player player = new Player();

        player.play();    // 정지 -> 재생
        player.pause();   // 재생 -> 일시정지
        player.pause();   // 이미 일시정지
        player.play();    // 일시정지 -> 재생
        player.stop();    // 재생 -> 정지
        player.pause();   // 정지 상태에서는 불가
    }
}

// State
interface PlayerState {
    void play(Player player);
    void pause(Player player);
    void stop(Player player);
}

// Context
class Player {
    private PlayerState state = new StoppedState();   // 초기 상태

    void setState(PlayerState state) { this.state = state; }

    void play()  { state.play(this); }     // 동작은 현재 상태에 위임
    void pause() { state.pause(this); }
    void stop()  { state.stop(this); }
}

// ConcreteState 1
class StoppedState implements PlayerState {
    public void play(Player p)  { System.out.println("재생 시작");  p.setState(new PlayingState()); }
    public void pause(Player p) { System.out.println("정지 상태에서는 일시정지할 수 없음"); }
    public void stop(Player p)  { System.out.println("이미 정지 상태"); }
}

// ConcreteState 2
class PlayingState implements PlayerState {
    public void play(Player p)  { System.out.println("이미 재생 중"); }
    public void pause(Player p) { System.out.println("일시정지");   p.setState(new PausedState()); }
    public void stop(Player p)  { System.out.println("정지");       p.setState(new StoppedState()); }
}

// ConcreteState 3
class PausedState implements PlayerState {
    public void play(Player p)  { System.out.println("재생 재개");  p.setState(new PlayingState()); }
    public void pause(Player p) { System.out.println("이미 일시정지 상태"); }
    public void stop(Player p)  { System.out.println("정지");       p.setState(new StoppedState()); }
}
```

**실행 결과**
```
재생 시작
일시정지
이미 일시정지 상태
재생 재개
정지
정지 상태에서는 일시정지할 수 없음
```

---

## 21. Strategy (전략)

**시험 정의**
**동일 계열의 알고리즘들을 정의하고, 각각을 캡슐화**하여 **상호 교환이 가능**하도록 만드는 패턴. 알고리즘을 사용하는 클라이언트와 상관없이 **독립적으로 알고리즘을 변경**할 수 있다.

**언제 사용하나**
- 목적은 같은데 **방법(알고리즘)만 다른** 여러 클래스가 있을 때 (결제 수단, 정렬 방식, 할인 정책, 경로 탐색, 압축 방식)
- `if-else`로 알고리즘을 분기하는 코드를 **런타임에 교체 가능한 객체**로 바꾸고 싶을 때

**추가 설명**
- 역할: `Strategy`(알고리즘 인터페이스), `ConcreteStrategy`(알고리즘 구현), `Context`(Strategy를 **포함**하고 사용).
- 전략이 메서드 하나뿐이면 자바 **람다식**으로 바로 전달할 수 있다(아래 예제의 포인트 결제). `Comparator`가 대표적인 Strategy 인터페이스.
- Template Method는 **상속**으로 알고리즘 일부를 바꾸고, Strategy는 **합성**으로 알고리즘 전체를 갈아 끼운다.

**시험 포인트**: "알고리즘군을 캡슐화", "교환 가능", "런타임에 알고리즘 변경".

```java
// StrategyDemo.java
public class StrategyDemo {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart(15000);

        cart.setStrategy(new CardPayment("1234-****"));
        cart.checkout();

        cart.setStrategy(new KakaoPayPayment());
        cart.checkout();

        // Strategy가 메서드 하나뿐이면 람다로도 가능
        cart.setStrategy(amount -> System.out.println("포인트 결제: " + amount + "원"));
        cart.checkout();
    }
}

// Strategy
@FunctionalInterface
interface PaymentStrategy {
    void pay(int amount);
}

// ConcreteStrategy
class CardPayment implements PaymentStrategy {
    private final String maskedCardNumber;
    CardPayment(String maskedCardNumber) { this.maskedCardNumber = maskedCardNumber; }

    public void pay(int amount) {
        System.out.println("카드 결제: " + amount + "원 (카드 " + maskedCardNumber + ")");
    }
}
class KakaoPayPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("카카오페이 결제: " + amount + "원");
    }
}

// Context
class ShoppingCart {
    private final int total;
    private PaymentStrategy strategy;

    ShoppingCart(int total) { this.total = total; }

    void setStrategy(PaymentStrategy strategy) { this.strategy = strategy; }
    void checkout() { strategy.pay(total); }
}
```

**실행 결과**
```
카드 결제: 15000원 (카드 1234-****)
카카오페이 결제: 15000원
포인트 결제: 15000원
```

---

## 22. Template Method (템플릿 메서드)

**시험 정의**
**상위 클래스에서 알고리즘의 처리 절차(골격)를 정의**하고, **하위 클래스에서 구체적인 처리 내용을 구현**하는 패턴. 알고리즘의 **구조는 변경하지 않고** 특정 단계만 재정의할 수 있다.

**언제 사용하나**
- 여러 클래스가 **처리 순서(흐름)는 같고 세부 단계만 다를 때** (중복 코드 제거)
- 알고리즘의 **불변 부분은 한 곳에 모으고** 가변 부분만 서브클래스가 확장하게 하고 싶을 때
- 프레임워크의 확장 지점(Hook) 제공 (Spring의 `JdbcTemplate`, 서블릿 `HttpServlet.service() → doGet/doPost`)

**추가 설명**
- 역할: `AbstractClass`(템플릿 메서드 `final` + 추상 단계 + 선택적 **Hook** 메서드), `ConcreteClass`(단계 구현).
- 제어 흐름이 "부모가 자식을 호출"하는 **할리우드 원칙**("Don't call us, we'll call you")의 대표 사례.
- 템플릿 메서드는 오버라이드되지 않도록 `final`로 선언하는 것이 안전하다.

**시험 포인트**: "상위 클래스에서 골격, 하위 클래스에서 구체화", "알고리즘 구조 유지", 할리우드 원칙.

```java
// TemplateMethodDemo.java
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
```

**실행 결과**
```
[CSV] 파일 읽기
[CSV] 쉼표로 분리하여 파싱
[공통] 데이터 검증
[CSV] DB 저장
--
[JSON] API 응답 읽기
[JSON] 객체로 역직렬화
[JSON] DB 저장
```

---

## 23. Visitor (방문자)

**시험 정의**
각 클래스(요소)의 **데이터 구조로부터 처리(연산) 기능을 분리**하여 별도의 클래스(방문자)로 구성함으로써, **요소 클래스를 수정하지 않고도 새로운 연산을 추가**할 수 있게 하는 패턴.

**언제 사용하나**
- 객체 구조는 **거의 안 바뀌는데**, 그 위에서 수행할 **연산은 자주 추가**될 때
- 서로 다른 타입의 요소들에 대해 **타입별로 다른 처리**를 하고 싶을 때 (세금 계산, 할인 계산, XML/JSON 내보내기, 컴파일러의 AST 처리)

**추가 설명**
- 역할: `Visitor`(요소 타입별 `visit` 메서드 선언), `ConcreteVisitor`(연산 구현), `Element`(`accept(Visitor)` 선언), `ConcreteElement`(`accept`에서 `visitor.visit(this)` 호출).
- 핵심은 **더블 디스패치(Double Dispatch)**: `item.accept(visitor)` → `visitor.visit(this)` 두 번의 호출로 "요소 타입 + 방문자 타입"에 맞는 코드가 실행된다.
- 단점: **새로운 요소 타입 추가가 어렵다**(모든 Visitor에 `visit` 메서드를 추가해야 함). 반대로 새 연산 추가는 쉽다.

**시험 포인트**: "처리 기능을 별도 클래스로 분리", "구조 수정 없이 새 연산 추가", `accept()`/`visit()`.

```java
// VisitorDemo.java
import java.util.List;

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
```

**실행 결과**
```
총 세금: 15000원
총 할인: 9500원
```

---

# 부록. 시험 대비 총정리

## A. 한 줄 요약 + 키워드 암기표

| # | 분류 | 패턴 | 한 줄 핵심 | 시험 키워드 |
|---|---|---|---|---|
| 1 | 생성 | Abstract Factory | 관련 객체들의 **군(Family)** 을 생성 | 제품군, Kit |
| 2 | 생성 | Builder | **생성 과정과 표현을 분리** | 복합 객체, Director |
| 3 | 생성 | Factory Method | 생성할 클래스를 **서브클래스가 결정** | Virtual Constructor |
| 4 | 생성 | Prototype | 원형을 **복제**해서 생성 | clone, 원형 |
| 5 | 생성 | Singleton | 인스턴스 **하나만**, 전역 접근 | getInstance, private 생성자 |
| 6 | 구조 | Adapter | **인터페이스 변환**으로 호환 | Wrapper |
| 7 | 구조 | Bridge | **추상층과 구현층 분리** | 독립적 확장 |
| 8 | 구조 | Composite | 단일/복합 객체를 **동일하게** | 트리, 부분-전체 |
| 9 | 구조 | Decorator | **동적으로 기능 추가** | 상속의 대안 |
| 10 | 구조 | Facade | **통합 인터페이스**로 단순화 | 서브시스템, 창구 |
| 11 | 구조 | Flyweight | 객체 **공유**로 메모리 절약 | 내재/외재 상태 |
| 12 | 구조 | Proxy | **대리자**로 접근 제어 | Surrogate, Lazy |
| 13 | 행위 | Chain of Responsibility | 요청을 **체인으로 전달** | 결재 라인, 필터 |
| 14 | 행위 | Command | 요청을 **객체로 캡슐화** | Undo, 큐, 로그 |
| 15 | 행위 | Interpreter | **문법**을 정의해 문장 해석 | 종단/비종단 표현 |
| 16 | 행위 | Iterator | 내부 노출 없이 **순차 접근** | hasNext/next |
| 17 | 행위 | Mediator | **중재자**가 상호작용 캡슐화 | 관제탑, 결합도 감소 |
| 18 | 행위 | Memento | **상태 저장·복원** (캡슐화 유지) | Undo, 스냅샷 |
| 19 | 행위 | Observer | 상태 변화를 **자동 통지** (1:N) | Publish-Subscribe |
| 20 | 행위 | State | 상태에 따라 **행위 변경** | 상태를 클래스로 |
| 21 | 행위 | Strategy | **알고리즘을 교체** 가능하게 | 알고리즘군 캡슐화 |
| 22 | 행위 | Template Method | **골격은 상위, 세부는 하위** | 할리우드 원칙 |
| 23 | 행위 | Visitor | **연산을 분리**, 구조 수정 없이 추가 | accept/visit, 더블 디스패치 |

## B. 헷갈리는 패턴 비교

| 비교 | 차이점 |
|---|---|
| **Abstract Factory vs Factory Method** | Abstract Factory = 관련된 **여러 제품군**을 생성(합성/객체 중심). Factory Method = **제품 하나**의 생성을 서브클래스에 위임(상속 중심). |
| **Adapter vs Bridge** | Adapter = **이미 만들어진** 클래스들을 사후에 맞춤. Bridge = **처음부터** 추상/구현을 분리하여 설계. |
| **Adapter vs Facade** | Adapter = 인터페이스를 **변환**(1:1, 기존 인터페이스에 맞춤). Facade = 복잡한 서브시스템에 **새로운 단순 인터페이스** 제공. |
| **Decorator vs Proxy** | 구조는 유사. Decorator = **기능 추가**가 목적. Proxy = **접근 제어**가 목적. |
| **Composite vs Decorator** | Composite = **여러 자식**을 가진 트리 구조(부분-전체). Decorator = **하나의 대상**을 감싸 기능을 덧붙임. |
| **State vs Strategy** | 구조는 유사. State = **상태 전이에 따라 스스로** 동작이 바뀜. Strategy = **클라이언트가 알고리즘을 선택**해서 주입. |
| **Strategy vs Template Method** | Strategy = **합성**으로 알고리즘 **전체**를 교체. Template Method = **상속**으로 알고리즘의 **일부 단계**만 재정의. |
| **Observer vs Mediator** | Observer = Subject → Observer **단방향 통지**(1:N). Mediator = 중재자가 객체 간 **양방향 상호작용**을 조율. |
| **Command vs Memento** | 둘 다 Undo에 쓰임. Command = **동작**을 객체화해 역동작 실행. Memento = **상태**를 저장해 복원. |
| **Prototype vs Singleton** | Prototype = 복제로 **여러 개** 만듦. Singleton = **하나만** 존재 보장. |

## C. 한 줄로 분류하는 요령

- **"~를 생성"** 이 보이면 → 생성 패턴 (Factory / Builder / Prototype / Singleton)
- **"~를 감싸서 / 연결 / 구성"** 이 보이면 → 구조 패턴 (Adapter / Bridge / Composite / Decorator / Facade / Flyweight / Proxy)
- **"~의 책임 분담 / 알고리즘 / 상호작용 / 통지"** 가 보이면 → 행위 패턴 (나머지 11개)
- 실기 시험(정보처리기사 실기)에서는 **코드를 보고 패턴 이름을 쓰는 문제**가 자주 나오므로, 위 예제 코드의 **클래스 구조(누가 누구를 포함/상속하는지)** 를 눈에 익혀 두는 것이 좋습니다.

---

> **참고**: 시험 정의 문구는 수험서·기출에서 널리 쓰이는 표현을 기준으로 정리했습니다. 교재마다 단어 선택이 조금씩 다를 수 있으니, 사용하시는 교재의 표현과 대조하며 보시면 좋습니다. 핵심 의미(키워드)는 동일합니다.
