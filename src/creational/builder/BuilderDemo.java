package creational.builder;

/*
> 빌더 - 생성과 표현을 분리
- 작게 분리된 인스턴스를 건축 하듯이 조합하여 객체를 생성하는 패턴
- 객체의 생성 과정과 표현 방법을 분리하고 있어
- 동일한 객체 생성에도 서로 다른 결과를 만들어 낼 수 있음
*/
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
        return "House{" +
                "foundation='" + foundation + '\'' +
                ", structure='" + structure + '\'' +
                ", roof='" + roof + '\'' +
                '}';
    }
}

// Builder
interface HouseBuilder {
    void buildFoundation();
    void buildStructure();
    void buildRoof();
    House getResult();
}

// ConcreteBuilder - 1 - 표현 과정
class WoodHouseBuilder implements HouseBuilder {

    private final House house = new House();

    @Override
    public void buildFoundation() {
        house.foundation = "자갈 기초";
    }

    @Override
    public void buildStructure() {
        house.structure = "목제 프레임";
    }

    @Override
    public void buildRoof() {
        house.roof = "기와 지붕";
    }

    @Override
    public House getResult() {
        return house;
    }
}

// ConcreteBuilder - 2 - 표현 과정
class StoneHouseBuilder implements HouseBuilder {

    private final House house = new House();

    @Override
    public void buildFoundation() {
        house.foundation = "콘트리트 기초";
    }

    @Override
    public void buildStructure() {
        house.structure = "석재 벽";
    }

    @Override
    public void buildRoof() {
        house.roof = "슬레이트 지붕";
    }

    @Override
    public House getResult() {
        return house;
    }
}

// Director: 생성 "순서"만 책임 - 생성 과정
class Director {
    House construct(HouseBuilder builder) {
        builder.buildFoundation();
        builder.buildStructure();
        builder.buildRoof();
        return builder.getResult();
    }
}
