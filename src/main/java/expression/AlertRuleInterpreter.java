package expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : wangebie
 * @date : 2021/7/2 10:09
 */
public class AlertRuleInterpreter {
    private Expression expression;

    /**
     * key1>100&&key2<1000||key3==200
     *
     * @param ruleExpression
     */
    public AlertRuleInterpreter(String ruleExpression) {

        expression = new OrExpression(ruleExpression);
    }

    /**
     * key1 ->103
     * key2 ->987
     *
     * @param stats param
     * @return bool
     */
    public boolean interpret(Map<String, Long> stats) {
        return expression.interpret(stats);
    }
}
