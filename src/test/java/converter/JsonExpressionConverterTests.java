package converter;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JsonExpressionConverterTests {
	JsonExpressionConverter converter;

	@Before
	public void setup() {
		converter = new JsonExpressionConverter();
	}

	@Test
	public void TestEqualityOperatorWithNumber() {
		// arrange
		var jsonExpression = "{\"==\": [\"var:recipient.age\", 55]}";
		var expectedInfix = "var:recipient.age == 55";
		// act
		InvokeTarget(jsonExpression, expectedInfix);
	}

	@Test
	public void TestEqualityOperatorWithString() {
		// arrange
		var jsonExpression = "{\"==\": [\"var:recipient.gender\", \"male\"]}";
		var expectedInfix = "var:recipient.gender == \"male\"";
		// act
		InvokeTarget(jsonExpression, expectedInfix);
	}

	@Test
	public void TestInequalityOperatorWithNumber() {
		// arrange
		var jsonExpression = "{\"!=\": [\"var:recipient.age\", 55]}";
		var expectedInfix = "var:recipient.age != 55";
		// act
		InvokeTarget(jsonExpression, expectedInfix);
	}

	@Test
	public void TestInequalityOperatorWithString() {
		// arrange
		var jsonExpression = "{\"!=\": [\"var:recipient.gender\", \"male\"]}";
		var expectedInfix = "var:recipient.gender != \"male\"";
		// act
		InvokeTarget(jsonExpression, expectedInfix);
	}

	@Test
	public void TestMultiLevelGreaterThanAndSubtractionOperator() {
		// arrange
		var jsonExpression = "{\"<\": [{\"-\": [\"var:system.today\", \"var:recipient.dob\"]}, 60]}";
		var expectedInfix = "var:system.today - var:recipient.dob < 60";
		// act
		InvokeTarget(jsonExpression, expectedInfix);
	}

	@Test
	public void TestMultiLevelLessThanAndAdditionOperator() {
		// arrange
		var jsonExpression = "{\"<\": [{\"+\": [\"var:system.today\", \"var:recipient.dob\"]}, 60]}";
		var expectedInfix = "var:system.today + var:recipient.dob < 60";
		// act
		InvokeTarget(jsonExpression, expectedInfix);
	}

	@Test
	public void TestMultiLevelLessThanAndMultiplicationOperator() {
		// arrange
		var jsonExpression = "{\"<\": [{\"*\": [\"var:system.today\", \"var:recipient.dob\"]}, 60]}";
		var expectedInfix = "var:system.today * var:recipient.dob < 60";
		// act
		InvokeTarget(jsonExpression, expectedInfix);
	}

	@Test
	public void TestMultiLevelLessThanAndDivisionOperator() {
		// arrange
		var jsonExpression = "{\"<\": [{\"/\": [\"var:system.today\", \"var:recipient.dob\"]}, 60]}";
		var expectedInfix = "var:system.today / var:recipient.dob < 60";
		// act
		InvokeTarget(jsonExpression, expectedInfix);
	}

	@Test
	public void TestMultiLevelAndOperator() {
		// arrange
		var jsonExpression = "{\"and\": [{\">=\": [{\"-\": [\"var:system.today\", \"var:recipient.dob\"]}, 60]},{\"<=\": [\"var:payment.insurance\", 2000]}]}";
		var expectedInfix = "(var:system.today - var:recipient.dob >= 60) && (var:payment.insurance <= 2000)";
		// act
		InvokeTarget(jsonExpression, expectedInfix);
	}

	@Test
	public void TestMultiLevelOrOperator() {
		// arrange
		var jsonExpression = "{\"or\": [{\">=\": [{\"-\": [\"var:system.today\", \"var:recipient.dob\"]}, 60]},{\"<=\": [\"var:payment.insurance\", 2000]}]}";
		var expectedInfix = "(var:system.today - var:recipient.dob >= 60) || (var:payment.insurance <= 2000)";
		// act
		InvokeTarget(jsonExpression, expectedInfix);
	}

	private void InvokeTarget(String jsonExpression, String expectedInfix) {
		// act
		var infix = converter.convertToInfix(jsonExpression);
		// assert
		assertEquals(expectedInfix, infix);
	}
}
