package behavioral.interpreter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/*
> 인터프리터
- 언어에 문법 표현을 정의하는 패턴
- SQL이나 통신 프로토콜과 같은 것을 개발할 대 사용
*/
public class InterpreterDemo {
    public static void main(String[] args) {
        Map<String, Integer> context = Map.of("x", 10, "y", 5);   // Context

        String[] sentences = { "x y + 3 -", "x 2 -" };            // 후위 표기식
        for (String s : sentences) {
            Expression expr = Parser.parse(s);                     // 문장 -> 표현식 트리
            System.out.println(s + " = " + expr.interpret(context));
        }
    }
}

// AbstractExpression
interface Expression {
    int interpret(Map<String, Integer> context);
}

// TerminalExpression : 숫자
class NumberExpr implements Expression {
    private final int value;
    NumberExpr(int value) { this.value = value; }
    public int interpret(Map<String, Integer> context) { return value; }
}

// TerminalExpression : 변수
class VariableExpr implements Expression {
    private final String name;
    VariableExpr(String name) { this.name = name; }
    public int interpret(Map<String, Integer> context) { return context.get(name); }
}

// NonterminalExpression : 덧셈
class AddExpr implements Expression {
    private final Expression left, right;
    AddExpr(Expression left, Expression right) { this.left = left; this.right = right; }
    public int interpret(Map<String, Integer> context) {
        return left.interpret(context) + right.interpret(context);
    }
}

// NonterminalExpression : 뺄셈
class SubExpr implements Expression {
    private final Expression left, right;
    SubExpr(Expression left, Expression right) { this.left = left; this.right = right; }
    public int interpret(Map<String, Integer> context) {
        return left.interpret(context) - right.interpret(context);
    }
}

// 문장을 표현식 트리로 만들어 주는 파서 (후위 표기법)
class Parser {
    static Expression parse(String postfix) {
        Deque<Expression> stack = new ArrayDeque<>();
        for (String token : postfix.trim().split("\\s+")) {
            if (token.equals("+")) {
                Expression right = stack.pop();
                Expression left = stack.pop();
                stack.push(new AddExpr(left, right));
            } else if (token.equals("-")) {
                Expression right = stack.pop();
                Expression left = stack.pop();
                stack.push(new SubExpr(left, right));
            } else if (token.matches("\\d+")) {
                stack.push(new NumberExpr(Integer.parseInt(token)));
            } else {
                stack.push(new VariableExpr(token));
            }
        }
        return stack.pop();
    }
}
