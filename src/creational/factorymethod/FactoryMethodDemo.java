package creational.factorymethod;

/*
> 팩토리 메서드 - 상위 클래스에서 메서드로 처리(구현 X) - 하위 클래스에서 처리하도록
- 객체 생성을 서브 클래스에서 처리하도록 분리하여 캡슐화한 패턴
- 상위 클래스에서 인터페이스만 정의하고 실제 생성은 서브 클래스가 담당함
- 가상 생성자(Virtual Constructor) 패턴이라고도 함
*/
public class FactoryMethodDemo {

    public static void main(String[] args) {
        Logistics road = new RoadLogistics();
        Logistics sea = new SeaLogistics();

        road.planDelivery();
        sea.planDelivery();
    }
}

// Product
interface Transport {
    void deliver();
}

// ConcreteProduct
class Truck implements Transport {

    @Override
    public void deliver() {
        System.out.println("트럭으로 육로 배송");
    }
}

class Ship implements Transport {

    @Override
    public void deliver() {
        System.out.println("선박으로 해상 배송");
    }
}

// Creator
abstract class Logistics {
    // 팩토리 메서드: 무엇을 만들지는 서브 클래스가 결정
    abstract Transport createTransport();

    void planDelivery() { // 템플릿 메서드 (행위 패턴)
        System.out.println("배송 계획 수립");
        Transport transport = createTransport();
        transport.deliver();
    }
}

// ConcreteCreator
class RoadLogistics extends Logistics {

    @Override
    Transport createTransport() {
        return new Truck();
    }
}

class SeaLogistics extends Logistics {

    @Override
    Transport createTransport() {
        return new Ship();
    }
}
