package expression;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : wangebie
 * @date : 2021/7/2 10:12
 */
public class DemoTest {
    public static void main(String[] args) {
        String rule = "key1 > 100 && ( key2 < 30 || key3 < 100 ) || key4 == 89";
        AlertRuleInterpreter interpreter = new AlertRuleInterpreter(rule);
        Map<String, Long> stats = new HashMap<>(16);
        stats.put("key1", 101L);
        stats.put("key3", 121L);
        stats.put("key4", 88L);
        System.out.println(interpreter.interpret(stats));
    }
}
