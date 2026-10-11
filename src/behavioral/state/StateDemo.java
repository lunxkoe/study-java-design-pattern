package behavioral.state;

/*
> 상태
- 객체의 상태에 따라 동일한 동작을 다르게 처리해야할 때 사용하는 패턴
- 객체 상태를 캡슐화하고 이를 참조하는 방식으로 처리함
*/
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
