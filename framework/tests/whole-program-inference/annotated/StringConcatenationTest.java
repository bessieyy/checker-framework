import tests.wholeprograminference.qual.Sibling1;
import tests.wholeprograminference.qual.*;
public class StringConcatenationTest {

    private @Sibling1 String options_str;
    private @Sibling1 String options_str2;

    void foo() {
        options_str = getSibling1();
        options_str2 += getSibling1();
    }

    void test() {
        expectsSibling1(options_str);
        expectsSibling1(options_str2);
    }

    void expectsSibling1(@Sibling1 String t) {}

    @Sibling1 String getSibling1() {
        //:: warning: (cast.unsafe)
        return (@Sibling1 String) " ";
    }

}