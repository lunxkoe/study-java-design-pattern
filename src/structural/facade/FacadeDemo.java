package structural.facade;

/*
> 퍼사드 ("통합된 인터페이스", "서브시스템", "사용 편의성/단순화")
- 복잡한 서브 크래스들을 피해 더 상위에 인터페이스를 구성함으로써 서브 클래스들의 기능을 간편하게 사용
- 서블 클래스들 사이의 통합 인터페이스를 제공하는 Wrapper 객체가 필요함
*/
public class FacadeDemo {

    public static void main(String[] args) {
        HomeTheaterFacade theater = new HomeTheaterFacade(
                new Projector(), new Amplifier(), new Player());

        theater.watchMovie("인셉션");   // 클라이언트는 이 한 줄만 알면 된다
        theater.endMovie();
    }
}

// 서브 시스템들
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
