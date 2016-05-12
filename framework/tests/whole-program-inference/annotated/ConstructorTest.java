import tests.wholeprograminference.qual.Top;
import tests.wholeprograminference.qual.*;
public class ConstructorTest {

    public ConstructorTest(@Top int top) {}

    void test() {
        @Top int top = (@Top int) 0;
        new ConstructorTest(top);
    }

}
