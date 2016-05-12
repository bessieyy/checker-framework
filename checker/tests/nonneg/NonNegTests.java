import java.util.Random;

import org.checkerframework.checker.nonneg.qual.*;

class NonNegTests {

	void hierarchyTest1(@NonNegative int a) {
		a = 1;
		//:: error: (assignment.type.incompatible)
		a = -1;
	}

	void hierarchyTest2(@UnknownInt int a) {
		a = 1;
		a = -1;
	}
	
	void introductionRuleTest1(@NonNegative int a) {
		int b = -1;
		//:: error: (assignment.type.incompatible)
		a = b;
	}
	
	void introductionRuleTest2(@NonNegative int a) {
		int b = 1;
		a = b;
	}
	
	void DataflowTest1(@NonNegative int a) {
		Random r = new Random();
		int b = r.nextInt();
		//:: error: (assignment.type.incompatible)
		a = b;
		if (b >= 0) {
			a = b;
		}
	}
	
	void DataflowTest2(@NonNegative int a) {
		int b = 1;
		b--;
		a = b; // error
	}
}
