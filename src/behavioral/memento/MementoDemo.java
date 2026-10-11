package behavioral.memento;

import java.util.ArrayDeque;
import java.util.Deque;

/*
> 메멘토
- 특정 시점의 객체 내부 상태를 객체화함으로써 이후 요청에 따라 객체를 해당 시점의 상태로 돌릴 수 있는 기능
- Ctrl+Z 같은 되돌리기 기능을 개발할 때 주로 이용함
*/
public class MementoDemo {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();      // Originator
        History history = new History();           // Caretaker

        editor.type("Hello");
        history.push(editor.save());

        editor.type(", World");
        history.push(editor.save());

        editor.type("!!!");
        System.out.println("현재: " + editor.getContent());

        editor.restore(history.pop());
        System.out.println("undo 1: " + editor.getContent());

        editor.restore(history.pop());
        System.out.println("undo 2: " + editor.getContent());
    }
}

// Originator
class TextEditor {
    private String content = "";

    void type(String text) { content += text; }
    String getContent() { return content; }

    Memento save() { return new Memento(content); }
    void restore(Memento memento) { this.content = memento.content; }

    // Memento : 외부에서는 내용을 볼 수 없다 (private 필드)
    static class Memento {
        private final String content;
        private Memento(String content) { this.content = content; }
    }
}

// Caretaker : Memento를 보관만 한다
class History {
    private final Deque<TextEditor.Memento> stack = new ArrayDeque<>();

    void push(TextEditor.Memento memento) { stack.push(memento); }
    TextEditor.Memento pop() { return stack.pop(); }
}
