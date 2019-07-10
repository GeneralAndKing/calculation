package gak.calc;

import java.util.HashMap;

/**
 * 我也不知道怎么说 贼TM恐怖的一堆符号
 *
 * @author <a href="https://github.com/RuiFG">Rui</a>
 * @date 19-7-9 上午11:39
 */
class Symbol {
    static final char ADD = '+';

    static final char SUBTRACT = '-';

    static final char MULTIPLY = '*';

    static final char DIVIDE = '/';

    static final char POW = '^';

    static final char ROOT = '√';

    static final char REMAINDER = '%';

    static final char FACTORIAL = '!';

    static final char LOG = 'l';

    static final char LN = 'n';

    static final char COS = 'c';

    static final char SIN = 's';

    static final char TAN = 't';

    static final char NEGATE = 'g';

    static final char LEFT = '(';

    static final char RIGHT = ')';

    private static final HashMap<Character, Integer> PRIORITY = new HashMap<>();
    private static final HashMap<Character, Integer> OPERATION = new HashMap<>();

    static {
        PRIORITY.put(LEFT, 0);
        PRIORITY.put(ADD, 1);
        PRIORITY.put(SUBTRACT, 1);
        PRIORITY.put(MULTIPLY, 2);
        PRIORITY.put(DIVIDE, 2);
        PRIORITY.put(POW, 3);
        PRIORITY.put(ROOT, 3);
        PRIORITY.put(REMAINDER, 3);
        PRIORITY.put(FACTORIAL, 3);
        PRIORITY.put(COS, 4);
        PRIORITY.put(SIN, 4);
        PRIORITY.put(TAN, 4);
        PRIORITY.put(LOG, 4);
        PRIORITY.put(LN, 4);
        PRIORITY.put(NEGATE, 4);
        PRIORITY.put(RIGHT, 5);

        OPERATION.put(REMAINDER, 2);
        OPERATION.put(ADD, 2);
        OPERATION.put(SUBTRACT, 2);
        OPERATION.put(MULTIPLY, 2);
        OPERATION.put(DIVIDE, 2);
        OPERATION.put(POW, 2);
        OPERATION.put(ROOT, 2);
        OPERATION.put(FACTORIAL, 1);
        OPERATION.put(COS, 1);
        OPERATION.put(SIN, 1);
        OPERATION.put(TAN, 1);
        OPERATION.put(LOG, 1);
        OPERATION.put(LN, 1);
        OPERATION.put(NEGATE, 1);
    }

    static int operation(String s) {
        char c = s.charAt(0);
        return OPERATION.getOrDefault(c, 0);
    }

    static int priority(Character c) {
        return PRIORITY.getOrDefault(c, -1);
    }

}
