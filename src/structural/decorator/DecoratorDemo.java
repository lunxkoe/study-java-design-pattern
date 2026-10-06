package structural.decorator;

/*
> 데코레이터 (객체 간의 결합, 다른 객체들은 덧붙이는 방식, 부가적인 기능 추가)
- 객체 간의 결합을 통해 능동적으로 기능들을 확장할 수 있는 패턴
- 임의의 객체에 부가적인 기능을 추가하기 위해 다른 객체들을 덧붙이는 방식으로 구현
*/
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
