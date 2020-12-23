/*
This program is designed to turn an infix expression with unary operators or some implied operators
into a postfix expression that then can be evaluated by the program to produce the result of the
infix expression to the user. The program will also tell the user whether or not the input is valid
for the program or not.
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Arrays;
import java.util.Stack;

public class Calculator {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String exp = getExpression(sc);

    // wil proceed to program if input is good
    if (checkMatchingParenthesis(exp) && containsProperChars(exp) && noInvalidOpLocations(exp)) {
      System.out.println("Converting to postfix...");
      // converts input to postfix
      String[] splitExpression = getProcessedInput(exp);
      System.out.println("Equation::> " + getReadablePostfix(splitExpression));
      System.out.println("Evaluating... " + "\nResult: " + calculatePostfix(splitExpression));
    } else {
      System.out.println("The input you enter was malformed. Exiting....");
    }
  }

  // exp/expression has elements of individual operands and operators
  // Ex. 2+(3*10)/(1-2) turns into [2,+,(,3,*,10,),/,(,1,-,2,)]
  public static String[] convertToPostfix(ArrayList<String> exp) {
    // appending spaces to be able to separate postfix operands/operators later
    StringBuilder postfix = new StringBuilder();
    Stack<String> opStack = new Stack<>();

    // op for operator or operand
    for (String op : exp) {
      if (isOperand(op)) {
        postfix.append(op).append(" ");
      }
      // if operator, pop from stack into postfix, until a left parenthesis or an operator of
      // greater precedence is on top of the stack, then push
      if (isOperator(op)) {
        while (!opStack.isEmpty()
            && (getPrec(op) <= getPrec(opStack.peek()))
            && !opStack.peek().equals("(")) {
          postfix.append(opStack.pop()).append(" ");
        }
        opStack.push(op);
      }
      if (op.equals("(")) {
        opStack.push(op);
      }
      // closing of expression (")") is in expression, pop to postfix until corresponding left
      // parenthesis is on top of stack (then pop it off w/o adding to postfix) or if the stack is
      // empty
      if (op.equals(")")) {
        while (!opStack.isEmpty() && !opStack.peek().equals("(")) {
          postfix.append(opStack.pop()).append(" ");
        }
        opStack.pop();
      }
    }
    // pop any remaining operators/operands off stack into postfix
    while (!opStack.isEmpty()) {
      postfix.append(opStack.pop()).append(" ");
    }
    // returns array of individual operands and operators, split by whitespace
    return postfix.toString().split(" ");
  }

  public static double calculatePostfix(String[] splitExp) {
    Stack<String> numStack = new Stack<>();
    for (String op : splitExp) {
      if (isOperand(op)) {
        numStack.push(op);
      }
      // when operator is touched, pop two numbers off and complete operation
      if (isOperator(op)) {
        double num1 = Double.parseDouble(numStack.pop());
        double num2 = Double.parseDouble(numStack.pop());
        double result = doOperation(num1, num2, op);
        numStack.push(String.valueOf(result));
      }
    }
    return Double.parseDouble(numStack.pop());
  }

  // returns the input with no whitespace, no omitted operators, and separated operands/operators
  // as a array of strings, where each element is an operand/operator
  public static String[] getProcessedInput(String exp) {
    return convertToPostfix(addOmittedOperators(separateUnaryOp(separateOps(exp))));
  }

  // gets user input and trims white space
  public static String getExpression(Scanner sc) {
    System.out.println("Enter your expression\n" + "-".repeat(15));
    // deleting all whitespace in user input
    return sc.nextLine().replaceAll("\\s", "");
  }

  // separates given input by individual operand or operator and trims white space
  // 5*2*(3+7*(2+1)) --> [5, *, 2, *, (, 3, +, 7, *, (, 2, +, 1, ), )]
  public static ArrayList<String> separateOps(String exp) {
    // regex for separating mathematical expression
    Pattern pattern = Pattern.compile("-?[0-9.]+|[A-Za-z]+|[-+*/()]|&&|[|]{2}");
    Matcher match = pattern.matcher(exp);
    ArrayList<String> separatedExp = new ArrayList<>();
    while (match.find()) {
      separatedExp.add(match.group());
    }
    // returned ArrayList, where each element is an individual operand/operator
    return separatedExp;
  }

  // regex mistakes [3, -, 2] for [3, -2], this fixes that
  // checks for operand right before minus sign and splits the -2 into - and 2 to get proper form
  public static ArrayList<String> separateUnaryOp(ArrayList<String> exp) {
    for (int i = 0; i < exp.size(); i++) {
      String token = exp.get(i);
      if (token.charAt(0) == '-'
          && i != 0
          && (isOperand(exp.get(i - 1)) || exp.get(i - 1).equals(")"))) {
        exp.set(i, token.substring(1));
        exp.add(i, "-");
      } else if (token.charAt(0) == '+' && i == 0) {
        exp.set(i, token.substring(1));
      } else if (token.charAt(0) == '+'
          && i != 0
          && (isParenthesis(exp.get(i - 1)) == 1 || isOperator(exp.get(i - 1)))) {
        exp.set(i, token.substring(1));
      }
    }
    return exp;
  }

  // adds omitted operators
  // Ex. 2(3+2) --> 2*(3+2) or (2+2)(3-1) --> (2+2)*(3-1)
  public static ArrayList<String> addOmittedOperators(ArrayList<String> exp) {
    for (int i = 0; i < exp.size(); i++) {
      // iterates through expression to find a number adjacent to a left parenthesis
      // adds symbol for multiplication between the two
      if (isParenthesis(exp.get(i)) == 1 && i != 0 && isOperand(exp.get(i - 1))) {
        exp.add(i, "*");
        // adds implied multiply by -1 if - precedes left parentheses
      } else if (isParenthesis(exp.get(i)) == 1 && i != 0 && exp.get(i - 1).equals("-")) {
        exp.set(i - 1, "-1");
        exp.add(i, "*");
        // adds implied "*" between adjacent closing parenthesis and opening parenthesis
      } else if (isParenthesis(exp.get(i)) == 2
          && (i != exp.size() - 1)
          && isParenthesis(exp.get(i + 1)) == 1) {
        exp.add(i + 1, "*");
      }
    }
    return exp;
  }

  // returns >0 for string is a parenthesis
  public static int isParenthesis(String op) {
    if (op == null) {
      return -1;
    } else if (op.equals("(")) {
      return 1;
    } else if (op.equals(")")) {
      return 2;
    } else {
      return -1;
    }
  }

  // method used to get precedence of given operators
  // multiplication and division have greater precedence than adding/subtracting
  public static int getPrec(String exp) {
    switch (exp) {
      case "*":
      case "/":
        return 5;
      case "+":
      case "-":
        return 2;
    }
    return 0;
  }

  // checks whether given string is operand (including numbers with unary operators)
  public static boolean isOperand(String exp) {
    try {
      Double.valueOf(exp);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  // returns whether a string is an accepted operator
  public static boolean isOperator(String check) {
    switch (check) {
      case "+":
      case "-":
      case "*":
      case "/":
        return true;
    }
    return false;
  }

  // completes ([operand1] [operator] [operand2]) and returns result
  public static double doOperation(double num2, double num1, String operator) {
    switch (operator) {
      case "*":
        return num1 * num2;
      case "/":
        return num1 / num2;
      case "+":
        return num1 + num2;
      case "-":
        return num1 - num2;
    }
    return -1;
  }

  // checks to see if given input contains anything other than numbers or valid
  // operators/parenthesis
  public static boolean containsProperChars(String expression) {
    return expression.matches("^[0-9./()[-*+]]+$");
  }

  // checks user input to make sure that there isn't any invalid operand locations
  public static boolean noInvalidOpLocations(String exp) {
    for (int i = 0; i < exp.length(); i++) {
      // checks for operator at end of input - not allowed
      if (isOperator(exp.charAt(exp.length() - 1) + "")) {
        return false;
        // not valid if three operators are in a row
      } else if (isOperator(exp.charAt(i) + "")
          && (i < exp.length() - 2)
          && isOperator(exp.charAt(i + 1) + "")
          && isOperator(exp.charAt(i + 2) + "")) {
        return false;
      }
    }
    return true;
  }

  // returns postfix in readable way, without brackets or commas
  public static String getReadablePostfix(String[] postfix) {
    return Arrays.toString(postfix).replace(",", "").replace("[", "").replace("]", "").trim();
  }

  // checks to make sure user enters input with equal number of opening and closing parenthesis
  private static boolean checkMatchingParenthesis(String expression) {
    Stack<Character> stack = new Stack<>();
    // converts expression to only parenthesis
    expression = expression.replaceAll("^[0-9/[-*+]]+$", "");

    for (int i = 0; i < expression.length(); i++) {
      char currentChar = expression.charAt(i);
      // push opening parenthesis
      if (currentChar == '(') {
        stack.push(currentChar);
        // pop when a closing is hit
      } else if (currentChar == ')') {
        stack.pop();
      }
    }
    // if matching stack should be empty
    return stack.isEmpty();
  }
}
