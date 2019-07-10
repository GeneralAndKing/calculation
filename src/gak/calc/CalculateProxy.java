package gak.calc;

import java.math.BigDecimal;

/**
 * 代理类，屏蔽大数类和普通类的差异性
 *
 * @author <a href="https://github.com/RuiFG">Rui</a>
 * @date 19-7-9 上午11:39
 */
class CalculateProxy {
    /**
     * 计算
     */
    BigDecimal calculate(String x, String y, Character symbol) {
        double num1;
        double num2;
        switch (symbol) {
            case Symbol.ADD:
                return new BigDecimal(x).add(new BigDecimal(y));
            case Symbol.SUBTRACT:
                return new BigDecimal(x).subtract(new BigDecimal(y));
            case Symbol.MULTIPLY:
                return new BigDecimal(x).multiply(new BigDecimal(y));
            case Symbol.DIVIDE:
                return new BigDecimal(x).divide(new BigDecimal(y), BigDecimal.ROUND_HALF_UP);
            case Symbol.POW:
                return new BigDecimal(x).pow(Integer.valueOf(y));
            case Symbol.ROOT:
                num1 = Double.parseDouble(x);
                num2 = Double.parseDouble(y);
                return new BigDecimal(Math.pow(num2, 1.0 / num1));
            case Symbol.REMAINDER:
                num1 = Double.parseDouble(x);
                num2 = Double.parseDouble(y);
                return new BigDecimal(num1).divideAndRemainder(new BigDecimal(num2))[1];
            case Symbol.FACTORIAL:
                return factorial(x);
            case Symbol.LOG:
                num1 = Double.parseDouble(x);
                return new BigDecimal(Math.log(num1));
            case Symbol.LN:
                num1 = Double.parseDouble(x);
                return new BigDecimal(Math.log10(num1));
            case Symbol.COS:
                num1 = Double.parseDouble(x);
                return new BigDecimal(Math.cos(num1));
            case Symbol.SIN:
                num1 = Double.parseDouble(x);
                return new BigDecimal(Math.sin(num1));
            case Symbol.TAN:
                num1 = Double.parseDouble(x);
                return new BigDecimal(Math.tan(num1));
            case Symbol.NEGATE:
                return new BigDecimal(x).negate();
            default:
                throw new RuntimeException();
        }
    }

    private BigDecimal factorial(String x) {
        BigDecimal answer = new BigDecimal("1");
        Integer num = Integer.valueOf(x);
        for (Integer i = 2; i <= num; i++) {
            answer = answer.multiply(new BigDecimal(i));
        }
        return answer;
    }

}