package behavioral;

import java.util.ArrayList;
import java.util.List;

/*
> 중재자
- 수많은 객체들 간의 복잡한 상호 작용을 캡슐화하여 객체로 정의하는 패턴
- 객체 사이의 의존성을 줄여 결합도를 감소시킬 수 있음
*/
public class MediatorDemo {
    public static void main(String[] args) {
        ChatMediator room = new ChatRoom();

        User hong = new User("홍길동", room);
        User kim  = new User("김철수", room);
        User lee  = new User("이영희", room);
        room.addUser(hong);
        room.addUser(kim);
        room.addUser(lee);

        hong.send("안녕하세요");     // 서로를 직접 모르고 중재자(ChatRoom)만 안다
        kim.send("반갑습니다");
    }
}

// Mediator
interface ChatMediator {
    void addUser(User user);
    void send(String message, User from);
}

// ConcreteMediator
class ChatRoom implements ChatMediator {
    private final List<User> users = new ArrayList<>();

    public void addUser(User user) { users.add(user); }

    public void send(String message, User from) {
        for (User user : users) {
            if (user != from) {                       // 보낸 사람 제외 전달
                user.receive(from.getName() + ": " + message);
            }
        }
    }
}

// Colleague
class User {
    private final String name;
    private final ChatMediator mediator;

    User(String name, ChatMediator mediator) { this.name = name; this.mediator = mediator; }

    String getName() { return name; }
    void send(String message) { mediator.send(message, this); }
    void receive(String message) { System.out.println("[" + name + " 수신] " + message); }
}


