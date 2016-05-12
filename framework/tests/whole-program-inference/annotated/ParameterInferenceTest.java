import tests.wholeprograminference.qual.Top;
import tests.wholeprograminference.qual.Parent;
import tests.wholeprograminference.qual.*;
public class ParameterInferenceTest {

    void test1() {
        @Parent int parent = (@Parent int) 0;
        expectsParentNoSignature(parent);
    }

    void expectsParentNoSignature(@Parent int t) {
        @Parent int parent = t;
    }

    void test2() {
        @Top int top = (@Top int) 0;
        expectsTopNoSignature(top);
    }

    void expectsTopNoSignature(@Top int t) {}

}
