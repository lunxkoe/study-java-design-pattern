package creational.singleton;

/*
> 싱글톤 - 단 하나의 객체를 생성
- 하나의 객체를 생성하면 생성된 객체를 어디서든 참조할 수 있지만
- 여러 프로세스가 동시에 참조할 수 없는 패턴
- 클래스 내에서 인스턴스가 하나뿐임을 보장하며, 불필요한 메모리 낭비를 최소화할 수 있음
*/
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

// 방법 1: Holder 방식 (지연 초기화 + 스레드 안전, 가장 권장되는 방식)
class Singleton {

    private int count;

    private Singleton() {} // 외부에서 new 금지

    private static class Holder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return Holder.INSTANCE; // 이 시점에 클래스 로딩 -> JVM이 스레드 안전 보장
    }

    public synchronized void increase() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

// 방법 2: enum 싱글턴 (가장 간결하고 안전) - 해당 클래스가 로딩되는 순간 싱글톤 객체가 자동으로 생성됨
enum EnumSingleton {
    INSTANCE;

    public void hello() {
        System.out.println("enum 싱글턴 동작");
    }
}

// 방법 3: Double-Checked Locking 방식
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
