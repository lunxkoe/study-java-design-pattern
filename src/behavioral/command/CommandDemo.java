package behavioral.command;

import java.util.ArrayDeque;
import java.util.Deque;

/*
> 커맨드 (요청 자체를 캡슐화)
- 요청을 객체의 형태로 탭슐화하여 재이용하거나 취소할 수 있도록 요청에 필요한 정보를 저장하거나 로그에 남기는 패턴
- 요청에 사용되는 각종 명령덜을 추상 클래스와 구체 클래스로 분리하여 단순화
*/
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
