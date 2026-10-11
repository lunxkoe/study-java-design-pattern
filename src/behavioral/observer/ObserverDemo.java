package behavioral.observer;

import java.util.ArrayList;
import java.util.List;

/*
> 옵서버
- 한 객체의 상태가 변화하면 객체에 상속되어있는 다른 객체들에게 변화된 상태를 전달하는 패턴
- 일대다의 의존성을 정의함
- 주로 분산된 시스템 간에 이벤트를 생성, 발행하고 이를 수신해야할 때 이용
*/
public class ObserverDemo {
    public static void main(String[] args) {
        NewsAgency agency = new NewsAgency();
        Subscriber cheolsu = new Subscriber("철수");
        Subscriber younghee = new Subscriber("영희");

        agency.attach(cheolsu);
        agency.attach(younghee);
        agency.publish("속보: 정보처리기사 합격 발표");

        System.out.println("-- 영희 구독 취소 --");
        agency.detach(younghee);
        agency.publish("속보: 다음 회차 접수 시작");
    }
}

// Observer
interface Observer {
    void update(String news);
}

// Subject
interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers();
}

// ConcreteSubject
class NewsAgency implements Subject {
    private final List<Observer> observers = new ArrayList<>();
    private String latestNews;

    public void attach(Observer o) { observers.add(o); }
    public void detach(Observer o) { observers.remove(o); }

    public void notifyObservers() {
        for (Observer o : observers) o.update(latestNews);
    }

    void publish(String news) {
        this.latestNews = news;   // 상태 변경
        notifyObservers();        // 자동 통지
    }
}

// ConcreteObserver
class Subscriber implements Observer {
    private final String name;
    Subscriber(String name) { this.name = name; }

    public void update(String news) {
        System.out.println("[" + name + "] 새 소식 수신: " + news);
    }
}
