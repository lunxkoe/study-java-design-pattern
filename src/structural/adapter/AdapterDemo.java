package structural.adapter;

/*
> 어댑터 (일본 / 한국 충전기 어댑터)
- 호환성이 없는 클래스들의 인터페이스를 다른 클래스가 이용할 수 있도록 변환해주는 패턴
- 기존 클래스를 이용하고 싶지만 인터페이스가 일치하지 않을 때 사용함
*/
public class AdapterDemo {

    public static void main(String[] args) {
        KoreanPlug plug = new PowerAdapter(new USDevice());
        plug.plug220V();
    }
}

interface KoreanPlug {
    void plug220V();
}

class USDevice {

    void plug110V() {
        System.out.println("미국산 기기: 110V 전원으로 동작");
    }
}

class PowerAdapter implements KoreanPlug {

    private final USDevice device;

    PowerAdapter(USDevice device) {
        this.device = device;
    }

    @Override
    public void plug220V() {
        System.out.println("어댑터: 220V -> 110V 변환");
        device.plug110V();
    }
}
