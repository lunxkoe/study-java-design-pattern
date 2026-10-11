package behavioral.strategy;

/*
> 전략
- 동일한 계열의 알고리즘들을 개별적으로 캡슐화하여 상호 교환할 수 있게 정의
- 클라이언트는 독립적으로 원하는 알고리즘을 선택하여 사용할 수 있으며,
- 클라이언트에 영향 없이 알고리즘의 변경이 가능
*/
public class StrategyDemo {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart(15000);

        cart.setStrategy(new CardPayment("1234-****"));
        cart.checkout();

        cart.setStrategy(new KakaoPayPayment());
        cart.checkout();

        // Strategy가 메서드 하나뿐이면 람다로도 가능
        cart.setStrategy(amount -> System.out.println("포인트 결제: " + amount + "원"));
        cart.checkout();
    }
}

// Strategy
@FunctionalInterface
interface PaymentStrategy {
    void pay(int amount);
}

// ConcreteStrategy
class CardPayment implements PaymentStrategy {
    private final String maskedCardNumber;
    CardPayment(String maskedCardNumber) { this.maskedCardNumber = maskedCardNumber; }

    public void pay(int amount) {
        System.out.println("카드 결제: " + amount + "원 (카드 " + maskedCardNumber + ")");
    }
}
class KakaoPayPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("카카오페이 결제: " + amount + "원");
    }
}

// Context
class ShoppingCart {
    private final int total;
    private PaymentStrategy strategy;

    ShoppingCart(int total) { this.total = total; }

    void setStrategy(PaymentStrategy strategy) { this.strategy = strategy; }
    void checkout() { strategy.pay(total); }
}
