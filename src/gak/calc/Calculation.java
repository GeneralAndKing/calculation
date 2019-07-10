package gak.calc;

import javafx.scene.input.KeyCode;

import java.math.BigDecimal;
import java.util.Stack;

/**
 * 计算类
 *
 * @author <a href="https://github.com/RuiFG">Rui</a>
 * @date 19-7-9 上午11:39
 */
public class Calculation {
    private String infix;
    private String suffix;
    private CalculateProxy calculateProxy = new CalculateProxy();
    private int defaultScale = 4;

    public static void main(String[] args) {
        Calculation demo = new Calculation();
//        System.out.println(demo.calculate("negate(50000!)+50000!"));
//        System.out.println(demo.calculate("cos(3)"));
        System.out.println(KeyCode.NUMPAD1);
    }

    /**
     * 语法解析
     **/
    private void analysis() {
        Character c;
        Stack<Character> stack = new Stack<>();
        StringBuilder sb = new StringBuilder();
        infix = infix.replaceAll(" +", "");
        infix = infix.replaceAll("negate", "g");
        infix = infix.replaceAll("cos", "c");
        infix = infix.replaceAll("sin", "s");
        infix = infix.replaceAll("tan", "t");
        infix = infix.replaceAll("log", "l");
        infix = infix.replaceAll("ln", "n");
        char[] s = infix.toCharArray();
        //我也不知道干嘛 干就完了
        for (int i = 0; i < s.length; i++) {
            c = s[i];
            switch (Symbol.priority(c)) {
                case 0:
                    sb.append(c);
                    stack.push(c);
                    break;
                case 4:
                    sb.append(Symbol.LEFT);
                    stack.push(c);
                    i += 1;
                    break;
                case 5:
                    c = stack.pop();
                    sb.append(Symbol.RIGHT);
                    if (Symbol.priority(c) == Symbol.priority(Symbol.COS)) {
                        sb.append(c);
                    }
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        while (stack.size() != 0) {
            c = stack.pop();
            sb.append(Symbol.RIGHT);
            if (Symbol.priority(c) == Symbol.priority(Symbol.COS)) {
                sb.append(c);
            }
        }
        infix = sb.toString();
    }


    /**
     * 中缀转后缀
     **/
    private void prepare() {
        StringBuilder sb = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        stack.push('=');
        char[] tempInfix = infix.toCharArray();
        //我也不知道干啥 刚就完事
        for (char c :
                tempInfix) {
            boolean isNumber = (c >= '0' && c <= '9') || c == 'E';
            boolean isPoint = c == '.';
            boolean isLogN = c == 'e';
            if (isNumber || isPoint || isLogN) {
                if (isLogN) {
                    sb.append(Math.E);
                } else {
                    sb.append(c);
                }
            } else {
                sb.append(" ");
                switch (Symbol.priority(c)) {
                    //处理左括号
                    case 0:
                        stack.push(c);
                        break;
                    //处理+ - * / 优先级
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        while (Symbol.priority(stack.peek()) > Symbol.priority(c)) {
                            sb.append(" ");
                            sb.append(stack.pop());
                            sb.append(" ");
                        }
                        stack.push(c);
                        break;
                    //处理右括号
                    case 5:
                        while (Symbol.priority(stack.peek()) != 0 && stack.size() > 1) {
                            sb.append(" ");
                            sb.append(stack.pop());
                            sb.append(" ");
                        }
                        stack.pop();
                        break;
                    default:
                        throw new RuntimeException();
                }
            }
        }
        while (stack.size() > 1) {
            sb.append(" ");
            sb.append(stack.pop());
            sb.append(" ");
        }
        stack.pop();
        suffix = sb.toString().replaceAll(" +", " ").trim();
    }

    public String calculate(String expression, Integer scale) {
        if (scale != null) {
            defaultScale = scale;
        }
        return calculate(expression);
    }

    public String calculate(String expression) {
        infix = "(" + expression + ")";
        try {
            analysis();
            prepare();
            return calculate();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return "error";
    }

    private String calculate() {
        Stack<String> stack = new Stack<>();
        String[] all = suffix.split(" ");
        String x, y;
        //哦 一堆循环 贼烦
        for (String s : all) {
            switch (Symbol.operation(s)) {
                case 0:
                    stack.push(s);
                    break;
                case 1:
                    x = stack.pop();
                    stack.push(operate(x, s));
                    break;
                case 2:
                    y = stack.pop();
                    x = stack.pop();
                    stack.push(operate(x, y, s));
                    break;
                default:
                    throw new RuntimeException();
            }
        }
        if (stack.size() != 1) {
            throw new RuntimeException();
        }
        return conversion(stack.pop());
    }

    private String operate(String x, String y, String symbol) {
        Character sb = symbol.charAt(0);
        BigDecimal answer = calculateProxy.calculate(x, y, sb);
        return answer.toString();
    }

    private String conversion(String s) {
        BigDecimal answer = new BigDecimal(s);
        answer = answer.setScale(defaultScale, BigDecimal.ROUND_HALF_UP);
        return answer.toString();
    }

    private String operate(String x, String symbol) {
        return operate(x, "", symbol);
    }
}
