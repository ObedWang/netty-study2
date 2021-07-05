package expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : wangebie
 * @date : 2021/7/2 10:31
 */
public class OrExpression implements Expression {
    private final List<Expression> expressionList;
    public static final String OPERATION = "||";

    public OrExpression(String expression) {
        String[] elements = expression.split("\\|\\|");
        expressionList = new ArrayList<>();
        for (String element : elements) {
            expressionList.add(new AndExpression(element));
        }
    }

    public OrExpression(List<Expression> expressions) {
        expressionList = expressions;
    }

    @Override
    public boolean interpret(Map<String, Long> stats) {
        for (Expression expression : expressionList) {
            if (expression.interpret(stats)) {
                return true;
            }
        }
        return false;
    }


}
