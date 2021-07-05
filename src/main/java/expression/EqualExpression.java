package expression;

import java.util.Map;

/**
 * @author : wangebie
 * @date : 2021/7/2 10:30
 */
public class EqualExpression implements Expression {
    private final String key;
    private final Long value;
    private static final int ELEMENT_NUM = 3;
    public static final String OPERATION = "==";

    public EqualExpression(String expression) {
        String[] elements = expression.trim().split("\\s+");
        if (elements.length != ELEMENT_NUM || !OPERATION.equals(elements[1])) {
            throw new RuntimeException();
        }
        key = elements[0];
        value = Long.parseLong(elements[2]);
    }

    @Override
    public boolean interpret(Map<String, Long> stats) {
        Long paramValue = stats.get(key);
        if (paramValue == null) {
            return false;
        }
        return paramValue.equals(value);
    }


}
