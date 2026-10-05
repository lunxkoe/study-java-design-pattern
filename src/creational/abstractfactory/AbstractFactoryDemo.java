package creational.abstractfactory;

/*
> 추상 팩토리 - 의존 객체들의 그룹
- 구체적인 클래스에 의존하지 않고
- 인터페이스를 통해 서로 연관, 의존하는 객체들의 **그룹**으로 생성
- **연관된 서브 클래스를 한 번에 교체하는 것이 가능**
*/
public class AbstractFactoryDemo {

    public static void main(String[] args) {
        runApp(new WindowsFactory());
        runApp(new MacFactory());
    }

    static void runApp(GUIFactory guiFactory) {
        Button button = guiFactory.createButton();
        Checkbox checkbox = guiFactory.createCheckbox();
        button.paint();
        checkbox.paint();
    }
}

interface Button {
    void paint();
}

interface Checkbox {
    void paint();
}

interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

class WindowsButton implements Button {

    @Override
    public void paint() {
        System.out.println("[Windows] 버튼 그리기");
    }
}

class WindowsCheckbox implements Checkbox {

    @Override
    public void paint() {
        System.out.println("[Windows] 체크박스 그리기");
    }
}

class MacButton implements Button {

    @Override
    public void paint() {
        System.out.println("[Mac] 버튼 그리기");
    }
}

class MacCheckbox implements Checkbox {

    @Override
    public void paint() {
        System.out.println("[Mac] 체크박스 그리기");
    }
}

class WindowsFactory implements GUIFactory {

    @Override
    public Button createButton() {
        return new WindowsButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }
}

class MacFactory implements GUIFactory {

    @Override
    public Button createButton() {
        return new MacButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new MacCheckbox();
    }
}
