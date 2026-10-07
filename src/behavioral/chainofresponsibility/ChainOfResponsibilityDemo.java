package behavioral.chainofresponsibility;

/*
> 책임 연쇄
- 요청을 처리할 수 있는 객체가 둘 이상 존재
- 한 객체가 처리하지 못하면 다음 객체로 넘어가는 형태
- 요청을 처리할 수 있는 각 객체들이 고리로 묶여 있어 요처이 해결될 때까지 고리를 따라 책임 연쇄
*/
public class ChainOfResponsibilityDemo {

    public static void main(String[] args) {
        Approver teamLead = new Approver("팀장", 1_000_000);
        Approver manager  = new Approver("부장", 5_000_000);
        Approver ceo      = new Approver("대표", 50_000_000);

        teamLead.setNext(manager).setNext(ceo);   // 체인 구성

        teamLead.handle(800_000);
        teamLead.handle(3_000_000);
        teamLead.handle(20_000_000);
        teamLead.handle(100_000_000);
    }
}

class Approver {
    private final String name;
    private final int limit;
    private Approver next;

    public Approver(String name, int limit) {
        this.name = name;
        this.limit = limit;
    }

    Approver setNext(Approver next) {
        this.next = next;
        return next;
    }

    void handle(int amount) {
        if (amount <= limit) {
            System.out.println(name + " 승인: " + amount + "원");
        } else if (next != null) {
            next.handle(amount);
        } else {
            System.out.println("승인 불가: " + amount + "원 (결재 라인 초과)");
        }
    }
}