package expression;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author : wangebie
 * @date : 2021/7/2 10:44
 */
public class SimpleExpressionFactory {
    private static final Map<String, Function<String, Expression>> MAP = new HashMap<>(16);

    private SimpleExpressionFactory() {
    }

    static {
        MAP.put(GreaterExpression.OPERATION, GreaterExpression::new);
        MAP.put(LessExpression.OPERATION, LessExpression::new);
        MAP.put(EqualExpression.OPERATION, EqualExpression::new);
    }

    public static Expression getExpression(String expression) {
        for (Map.Entry<String, Function<String, Expression>> entry : MAP.entrySet()) {
            if (expression.contains(entry.getKey())) {
                return entry.getValue().apply(expression);
            }
        }
        return null;
    }

}
