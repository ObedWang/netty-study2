package expression;

import java.util.Map;

/**
 * @author : wangebie
 * @date : 2021/7/2 10:15
 */
public interface Expression {
    /**
     * judge
     *
     * @param stats param
     * @return obj
     */
    boolean interpret(Map<String, Long> stats);


}
