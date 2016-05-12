import tests.wholeprograminference.qual.Sibling1;
import tests.wholeprograminference.qual.*;
//TODO: Like this one, some tests must verify that it contains the expected
// output after performing the whole-program inference.
public class MethodParameterInferenceTest {
    void foo(@Sibling1 int i) {
        i = getSibling1(); // The type of i must be inferred to @Sibling1.
    }

    @Sibling1 int getSibling1() {
        return (@Sibling1 int) 0;
    }
}
