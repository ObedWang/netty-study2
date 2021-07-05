package expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : wangebie
 * @date : 2021/7/2 10:34
 */
public class AndExpression implements Expression {
    private final List<Expression> expressionList;
    private static final String OPERATION = "&&";

    public AndExpression(String expression) {
        String[] elements = expression.split("&&");
        expressionList = new ArrayList<>();
        for (String element : elements) {
            expressionList.add(SimpleExpressionFactory.getExpression(element));
        }
    }

    public AndExpression(List<Expression> expressionList) {
        this.expressionList = expressionList;
    }

    @Override
    public boolean interpret(Map<String, Long> stats) {
        if (expressionList == null || expressionList.size() == 0) {
            return false;
        }
        for (Expression expression : expressionList) {
            if (!expression.interpret(stats)) {
                return false;
            }
        }
        return true;
    }

}
