package converter;

import java.util.*;
import com.google.gson.*;

public class JsonExpressionConverter {

  public static final String VAR_IDENTIFIER = "var:";
  public static final String AND_OPERATOR = "and";
  public static final String OR_OPERATOR = "or";
  JsonParser parser;

  /**
   * ctor
   */
  public JsonExpressionConverter() {
    super();
    parser = new JsonParser();
  }

  /**
   * Converts a json expression into an infix string
   * 
   * @param jsonExpression
   * @return infix expression string
   */
  public String convertToInfix(String jsonExpression) {
    var jsonElement = parser.parse(jsonExpression);
    return convertToInfix(jsonElement);
  }

  /**
   * Recursive function which converts a JsonElement json expression into an infix
   * one
   * 
   * @param json expression
   * @return infix expression string
   */
  private String convertToInfix(JsonElement json) {
    var jsonObj = (JsonObject) json;
    Set<Map.Entry<String, JsonElement>> set = jsonObj.entrySet();
    Iterator<Map.Entry<String, JsonElement>> iterator = set.iterator();

    Map.Entry<String, JsonElement> entry = iterator.next();
    String key = entry.getKey();
    JsonElement value = entry.getValue();

    var operandArray = value.getAsJsonArray();
    var leftOperand = operandArray.get(0);
    var rightOperand = operandArray.get(1);

    String infix = "";

    // TODO: handle "in", "not-in", "matches" etc separately

    var childLeftExpression = leftOperand.isJsonObject() ? convertToInfix(leftOperand) : getValue(leftOperand);
    var childRightExpression = rightOperand.isJsonObject() ? convertToInfix(rightOperand) : getValue(rightOperand);

    // in case of AND & OR, expressions should be surrounded by brackets
    if (key.equals(AND_OPERATOR) || key.equals(OR_OPERATOR)) {
      childLeftExpression = "(" + childLeftExpression + ")";
      childRightExpression = "(" + childRightExpression + ")";
    }

    infix = childLeftExpression + " " + getOperator(key) + " " + childRightExpression;

    return infix;
  }

  /**
   * Process JsonElement object and extracts value
   * 
   * @param element JsonElement
   * @return String value. Includes quotation marks if the value is a string but
   *         not a variable
   */
  private String getValue(JsonElement element) {
    if (((JsonPrimitive) element).isString() && !element.getAsString().startsWith(VAR_IDENTIFIER))
      return "\"" + element.getAsString() + "\"";
    else
      return element.getAsString();
  }

  /**
   * Returns the operator which Drools understands
   * 
   * @param key
   * @return
   */
  private String getOperator(String operator) {
    switch (operator) {
      case "and":
        return "&&";
      case "or":
        return "||";
      default:
        return operator;
    }
  }
}