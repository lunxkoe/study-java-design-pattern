package creational.abstractfactory;

/** 추상 팩토리 패턴 */
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
