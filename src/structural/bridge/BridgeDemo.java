package structural.bridge;

/*
> 브릿지 (기능과 구현부를 분리)
- 구현부에서 추상층을 분리하여 서로가 독립적으로 확장할 수 있도록 구성한 패턴
- 기능과 구현을 두 개의 별도의 클래스로 분리
*/
public class BridgeDemo {

    public static void main(String[] args) {
        RemoteControl tvRemote = new RemoteControl(new TV());
        tvRemote.togglePower();
        tvRemote.volumeUp();

        AdvancedRemote radioRemote = new AdvancedRemote(new Radio());
        radioRemote.togglePower();
        radioRemote.mute();
    }
}

interface Device {
    boolean isEnabled();
    void enable();
    void disable();
    int getVolume();
    void setVolume(int volume);
}

class BaseDevice implements Device {

    private final String name;
    private boolean on = false;
    private int volumn = 30;

    BaseDevice(String name) {
        this.name = name;
    }

    @Override
    public boolean isEnabled() {
        return on;
    }

    @Override
    public void enable() {
        on = true;
        System.out.println(name + " 전원 ON");
    }

    @Override
    public void disable() {
        on = false;
        System.out.println(name + " 전원 OFF");
    }

    @Override
    public int getVolume() {
        return volumn;
    }

    @Override
    public void setVolume(int volume) {
        volume = Math.max(0, Math.min(100, volume));
        System.out.println(name + " 볼륨: " + volume);
    }
}

class TV extends BaseDevice {

    TV() {
        super("TV");
    }
}

class Radio extends BaseDevice {

    Radio() {
        super("라디오");
    }
}

class RemoteControl {

    protected final Device device;

    RemoteControl(Device device) {
        this.device = device;
    }

    void togglePower() {
        if (device.isEnabled()) {
            device.disable();
        } else {
            device.enable();
        }
    }

    void volumeUp() {
        device.setVolume(device.getVolume() + 10);
    }
}

// RefinedAbstraction : 기능 계층만 독립적으로 확장
class AdvancedRemote extends RemoteControl {
    AdvancedRemote(Device device) { super(device); }

    void mute() { device.setVolume(0); }
}
