package structural.composite;

import java.util.ArrayList;
import java.util.List;

/*
> 컴포지트 (복합 객체, 단일 객체, 트리 구조 - **합성** - 파일과 폴더는 같은 동작으로 다룸 - 약간 리눅스 느낌)
- 여러 객체를 가진 복합 객체와 단일 객체를 구분 없이 다루고자 할 때 사용하는 패턴
- 객체들을 트리 구조로 구성하여 디렉터리 안에 디렉터리가 있듯이 복합 객체 안에 복합 객체가 포함되는 구조를 구현할 수 있음
*/
public class CompositeDemo {
    public static void main(String[] args) {
        Directory root = new Directory("root");
        root.add(new File("a.txt", 100));

        Directory src = new Directory("src");
        src.add(new File("Main.java", 300));
        src.add(new File("Util.java", 200));
        root.add(src);

        root.print("");   // 폴더든 파일이든 똑같이 print() 호출
    }
}

// Component
interface FileSystemItem {
    int getSize();
    void print(String indent);
}

// Leaf
class File implements FileSystemItem {
    private final String name;
    private final int size;

    File(String name, int size) { this.name = name; this.size = size; }

    public int getSize() { return size; }
    public void print(String indent) {
        System.out.println(indent + "[F] " + name + " (" + size + ")");
    }
}

// Composite
class Directory implements FileSystemItem {
    private final String name;
    private final List<FileSystemItem> children = new ArrayList<>();

    Directory(String name) { this.name = name; }

    // 폴더에만 추가되는 기능
    void add(FileSystemItem item) { children.add(item); }

    // 아래 getSize() / print()는 파일 객체도 동일하게 사용할 수 있음 (현재 폴더가 - 파일 객체를 가지고 있을 뿐 / 폴더를 가질 수도 있음 (같은 인터페이스를 구현 중)
    public int getSize() {
        int total = 0;
        for (FileSystemItem child : children) total += child.getSize();   // 재귀
        return total;
    }

    public void print(String indent) {
        System.out.println(indent + "[D] " + name + " (" + getSize() + ")");
        for (FileSystemItem child : children) child.print(indent + "  ");
    }
}
