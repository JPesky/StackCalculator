import static org.junit.Assert.*;
import org.junit.Test;

public class CalculatorTest {
  // basic addition and subtraction
  String[] exp = Calculator.getProcessedInput("2+3+10+1+2");
  String[] exp2 = Calculator.getProcessedInput("1+2");
  String[] exp3 = Calculator.getProcessedInput("192+356-31");
  String[] exp4 = Calculator.getProcessedInput("2000+1-0");
  // basic addition and subtraction with parenthesis
  String[] exp5 = Calculator.getProcessedInput("2+(3+10)+(1+2)");
  String[] exp6 = Calculator.getProcessedInput("(192+356)-31");
  String[] exp7 = Calculator.getProcessedInput("2000+(1-0)");
  String[] exp8 = Calculator.getProcessedInput("1+(2+1)");
  // multiplication and division
  String[] exp9 = Calculator.getProcessedInput("2*3/10*1*2");
  String[] exp10 = Calculator.getProcessedInput("1/2");
  String[] exp11 = Calculator.getProcessedInput("192/356*31");
  String[] exp12 = Calculator.getProcessedInput("2000/1*0");
  // multiplication and division with parenthesis and addition/subtraction
  String[] exp13 = Calculator.getProcessedInput("2+(3*10)/(1-2)");
  String[] exp14 = Calculator.getProcessedInput("(192+356)/31");
  String[] exp15 = Calculator.getProcessedInput("2000+(1*0)");
  String[] exp16 = Calculator.getProcessedInput("1+(2/1)");
  // harder arithmetic
  String[] exp17 = Calculator.getProcessedInput("50+(21*3)+17*(2+31)+(2*7)+(2*(3+(7+2)))");
  String[] exp18 = Calculator.getProcessedInput("20*2*(3+7)+(192+356)/31");
  String[] exp19 = Calculator.getProcessedInput("((20*3)+2)*5");
  String[] exp20 = Calculator.getProcessedInput("2(50+(2*(3+(7+2))))+2");
  // harder arithmetic with negatives
  String[] exp21 = Calculator.getProcessedInput("-50+(-21*3)+17*(2-31)+(-2*7)+(2*(3+(7-2)))");
  String[] exp22 = Calculator.getProcessedInput("-20*2*(-3+7)+(192+356)/31");
  String[] exp23 = Calculator.getProcessedInput("((-20*3)-2)*(-5)");
  String[] exp24 = Calculator.getProcessedInput("2(-50+(-2*(-3+(-7+2))))+2");
  // basic with negative
  String[] exp25 = Calculator.getProcessedInput("-2+3+10+1+2");
  String[] exp26 = Calculator.getProcessedInput("-1+-2");
  String[] exp27 = Calculator.getProcessedInput("-192+356-31");
  String[] exp28 = Calculator.getProcessedInput("-2000+1-0");
  // expressions with unary operators and implied operators
  String[] exp29 = Calculator.getProcessedInput("+50+(+21*3)+17*(+2+31)+(-2*7)+(2*(3+(7+2)))");
  String[] exp30 = Calculator.getProcessedInput("-20*2(+3+7)+(-192+356)/-31");
  String[] exp31 = Calculator.getProcessedInput("(-(+20*3)+2)*5");
  String[] exp32 = Calculator.getProcessedInput("+2(50+(2*(-3+(7+2))))+2");
  String[] exp33 = Calculator.getProcessedInput("(-2+4)(3+(-17*(2+1)))");

  @Test
  public void addSubtract() {
    assertEquals(2 + 3 + 10 + 1 + 2, Calculator.calculatePostfix(exp), 0.01);
    assertEquals(1 + 2, Calculator.calculatePostfix(exp2), 0.01);
    assertEquals(192 + 356 - 31, Calculator.calculatePostfix(exp3), 0.01);
    assertEquals(2000.0 + 1 - 0, Calculator.calculatePostfix(exp4), 0.01);
  }

  @Test
  public void addSubtractWithParentheses() {
    assertEquals(2 + (3 + 10) + (1 + 2), Calculator.calculatePostfix(exp5), 0.01);
    assertEquals((192 + 356) - 31, Calculator.calculatePostfix(exp6), 0.01);
    assertEquals(2000 + (1.0 - 0), Calculator.calculatePostfix(exp7), 0.01);
    assertEquals(1 + (2 + 1), Calculator.calculatePostfix(exp8), 0.01);
  }

  @Test
  public void basicWithNegative() {
    assertEquals(-2 + 3 + 10 + 1 + 2, Calculator.calculatePostfix(exp25), 0.01);
    assertEquals(-1 + -2, Calculator.calculatePostfix(exp26), 0.01);
    assertEquals(-192 + 356 - 31, Calculator.calculatePostfix(exp27), 0.01);
    assertEquals(-2000 + 1 - 0, Calculator.calculatePostfix(exp28), 0.01);
  }

  @Test
  public void multiplyDivide() {
    assertEquals(2.0 * 3 / 10 * 1 * 2, Calculator.calculatePostfix(exp9), 0.1);
    assertEquals(1.0 / 2, Calculator.calculatePostfix(exp10), 0.1);
    assertEquals(192.0 / 356 * 31, Calculator.calculatePostfix(exp11), 0.1);
    assertEquals(2000.0 / 1 * 0, Calculator.calculatePostfix(exp12), 0.1);
  }

  @Test
  public void allArithmeticWithParentheses() {
    assertEquals(2 + (3.0 * 10) / (1 - 2), Calculator.calculatePostfix(exp13), 0.1);
    assertEquals((192 + 356) / 31.0, Calculator.calculatePostfix(exp14), 0.1);
    assertEquals(2000 + (1.0 * 0), Calculator.calculatePostfix(exp15), 0.1);
    assertEquals(1 + (2.0 / 1), Calculator.calculatePostfix(exp16), 0.1);
  }

  @Test
  public void hardArithmetic() {
    assertEquals(50 + (21 * 3) + 17 * (2 + 31) + (2 * 7) + (2 * (3 + (7 + 2))), Calculator.calculatePostfix(exp17), 0.1);
    assertEquals(20 * 2 * (3 + 7) + (192 + 356) / 31.0, Calculator.calculatePostfix(exp18), 0.1);
    assertEquals(((20 * 3) + 2) * 5, Calculator.calculatePostfix(exp19), 0.1);
    assertEquals(2 * (50 + (2 * (3 + (7 + 2)))) + 2, Calculator.calculatePostfix(exp20), 0.1);
  }

  @Test
  public void hardArithmeticWithNegative() {
    assertEquals(-50 + (-21 * 3) + 17 * (2 - 31) + (-2 * 7) + (2 * (3 + (7 - 2))), Calculator.calculatePostfix(exp21), 0.1);
    assertEquals(-20 * 2 * (-3 + 7) + (192 + 356) / 31.0, Calculator.calculatePostfix(exp22), 0.1);
    assertEquals(((-20 * 3) - 2) * (-5), Calculator.calculatePostfix(exp23), 0.1);
    assertEquals(2 * (-50 + (-2 * (-3 + (-7 + 2)))) + 2, Calculator.calculatePostfix(exp24), 0.1);
  }

  @Test
  public void impliedOperators() {
    assertEquals(+50+(+21*3)+17*(+2+31)+(-2*7)+(2*(3+(7+2))), Calculator.calculatePostfix(exp29), 0.1);
    assertEquals(-20 * 2 * (+3 + 7) + (-192 + 356) / -31.0, Calculator.calculatePostfix(exp30), 0.1);
    assertEquals((-1 * (+20 * 3) + 2) * 5, Calculator.calculatePostfix(exp31), 0.1);
    assertEquals(+2 * (50 + (2 * (-3 + (7 + 2)))) + 2, Calculator.calculatePostfix(exp32), 0.1);
    assertEquals((-2 + 4) * (3 + (-17 * (2 + 1))), Calculator.calculatePostfix(exp33), 0.1);
  }
}
