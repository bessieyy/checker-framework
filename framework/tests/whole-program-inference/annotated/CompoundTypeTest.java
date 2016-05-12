import tests.wholeprograminference.qual.Sibling2;
import tests.wholeprograminference.qual.Sibling1;
import tests.wholeprograminference.qual.*;

public class CompoundTypeTest {
    // The default type for fields is @DefaultType.
    @Sibling1
    Object @Sibling2 [] field;

    void assign() {
        field = getCompoundType();
    }

    void test() {
        expectsCompoundType(field);
    }

    void expectsCompoundType(@Sibling1 Object @Sibling2 [] obj) {}

    @Sibling1 Object @Sibling2 [] getCompoundType() {
        //:: warning: (cast.unsafe)
        @Sibling1 Object @Sibling2 [] out = (@Sibling1 Object @Sibling2 []) new Object[1];
        return out;
    }

}

