package structural.flyweight;

/*
> 플라이웨이트 ("공유", "메모리 절약", "작은 객체 다수", 내재/외재 상태) - 자바 상수풀 생각
- 인스턴스가 필요할 때마다 매번 생성하는 것이 아닌
- 가능한 공유해서 사용함으로써 메모리를 절약하는 패턴
- 다수의 유사 객체를 생성하거나 조작할 때 유용하게 사용할 수 있음
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlyWeightDemo {
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
