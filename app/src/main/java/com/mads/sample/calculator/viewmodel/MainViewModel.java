package com.mads.sample.calculator.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mads.sample.calculator.model.HistoryItem;
import com.mads.sample.calculator.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    public MutableLiveData<String> mEnteredExpression;
    public MutableLiveData<List<HistoryItem>> mLastTenOperations;
    private char mLastInputChar;
    private String mLastResult;

    public MainViewModel() {
        mEnteredExpression = new MutableLiveData<>();
        mLastTenOperations = new MutableLiveData<>();
    }

    public void clearExpression() {
        if (mEnteredExpression.getValue() != null && !mEnteredExpression.getValue().equals("")) {
            mEnteredExpression.setValue("");
        }
    }

    public MutableLiveData<List<HistoryItem>> getHistoryItems() {
        return mLastTenOperations;
    }

    public void inputValue(char value) {
        if (mEnteredExpression.getValue() != null) {
            if (mEnteredExpression.getValue().equals("Can't divide by 0") || mEnteredExpression.getValue().equals("Invalid expression")) {
                mEnteredExpression.setValue("");
                if (value != 'x' && value != '+' && value != '/') {
                    mEnteredExpression.setValue("" + value);
                }
            } else if (mEnteredExpression.getValue().equals("")) {
                if (value != 'x' && value != '+' && value != '/') {
                    mEnteredExpression.setValue("" + value);
                }
            } else if (!isCharAnOperator(mLastInputChar) || (isCharAnOperator(mLastInputChar) && !isCharAnOperator(value))) {
                mEnteredExpression.setValue(mEnteredExpression.getValue().concat("" + value));
            } else if (isCharAnOperator(mLastInputChar) && isCharAnOperator(value)) {
                if (mLastInputChar != value) {
                    String substring = mEnteredExpression.getValue().substring(0, mEnteredExpression
                            .getValue().length() - 1);
                    mEnteredExpression.setValue(substring + value);
                }
            }
        } else {
            if (value != 'x' && value != '+' && value != '/') {
                mEnteredExpression.setValue("" + value);
            }
        }
        mLastInputChar = value;
    }

    public void onEqualsTapped() {
        if (mEnteredExpression.getValue() != null) {
            String result = calculate(mEnteredExpression.getValue());
            if (!result.equals("Can't divide by 0") && !result.equals("Invalid expression")) {
                mLastResult = result;
            }
            if (mLastTenOperations.getValue() != null) {
                if (mLastTenOperations.getValue().size() == 10) {
                    mLastTenOperations.getValue().remove(0);
                }
                mLastTenOperations.getValue().add(new HistoryItem(mEnteredExpression.getValue(), result));
                mLastTenOperations.setValue(mLastTenOperations.getValue());
            } else {
                List<HistoryItem> historyItems = new ArrayList<>();
                historyItems.add(new HistoryItem(mEnteredExpression.getValue(), result));
                mLastTenOperations.setValue(historyItems);
            }
            mEnteredExpression.setValue(result);
        }
    }

    public void onAnsButtonTapped() {
        if (mLastResult != null) {
            mEnteredExpression.setValue(mLastResult);
        } else {
            mEnteredExpression.setValue("No last result!");
        }
    }

    private boolean isCharAnOperator(char val) {
        return val == 'x' || val == '+' || val == '/' || val == '-';
    }

    private String calculate(String expression) {
        List<String> strings = new ArrayList<>();
        int m = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while (m < expression.length()) {
            if (!isCharAnOperator(expression.charAt(m)) || (isCharAnOperator(expression.charAt(m)) && m == 0) ||
                    isCharAnOperator(expression.charAt(m)) && (expression.charAt(m - 1) == 'x' || expression.charAt(m - 1) == '/')) {
                stringBuilder.append(expression.charAt(m));
                if (m == expression.length() - 1) {
                    strings.add(stringBuilder.toString());
                    stringBuilder.setLength(0);
                }
            } else {
                strings.add(stringBuilder.toString());
                strings.add(String.valueOf(expression.charAt(m)));
                stringBuilder.setLength(0);
            }
            m++;
        }
        System.out.println(strings);
        int i = 0;
        while (isStringContainingOperator(strings)) {
            String lookingForOperator = null;
            if (strings.contains(Constant.MULTIPLY_OPERATOR)) {
                lookingForOperator = Constant.MULTIPLY_OPERATOR;
            } else if (strings.contains(Constant.ADD_OPERATOR)) {
                lookingForOperator = Constant.ADD_OPERATOR;
            } else if (strings.contains(Constant.DIVIDE_OPERATOR)) {
                lookingForOperator = Constant.DIVIDE_OPERATOR;
            } else if (strings.contains(Constant.SUBTRACT_OPERATOR)) {
                lookingForOperator = Constant.SUBTRACT_OPERATOR;
            }
            try {
                if (isOperator(strings.get(i)) && lookingForOperator.equals(strings.get(i))) {
                    System.out.println("This is operator");
                    switch (strings.get(i)) {

                        case Constant.MULTIPLY_OPERATOR:
                            double firstMultiplyOperand, secondMultiplyOperand;
                            boolean isFirstOperandNegative;
                            if (i != 1 && strings.get(i - 2).equals("-")) {
                                firstMultiplyOperand = Double.parseDouble("-" + strings.get(i - 1));
                                isFirstOperandNegative = true;
                            } else {
                                isFirstOperandNegative = false;
                                firstMultiplyOperand = Double.parseDouble(String.valueOf(strings.get(i - 1)));
                            }
                            if (strings.get(i + 1).equals("-")) {
                                secondMultiplyOperand = Double.parseDouble("-" + strings.get(i + 2));
                                strings.remove(i);
                            } else {
                                secondMultiplyOperand = Double.parseDouble(String.valueOf(strings.get(i + 1)));
                            }
                            double product = firstMultiplyOperand * secondMultiplyOperand;
                            if (isFirstOperandNegative) {
                                strings.set(i - 1, String.valueOf(Math.abs(product)));
                                if (product < 0) {
                                    strings.set(i - 2, "-");
                                } else {
                                    strings.set(i - 2, "+");
                                }
                            } else {
                                strings.set(i - 1, String.valueOf(product));
                            }
                            strings.remove(i);
                            strings.remove(i);
                            System.out.println("New strings list:" + strings);
                            i = 0;
                            break;

                        case Constant.ADD_OPERATOR:
                            double firstAddOperand, secondAddOperand;
                            boolean isFirstAddOperandNegative;
                            if (i != 1 && strings.get(i - 2).equals("-")) {
                                firstAddOperand = Double.parseDouble("-" + strings.get(i - 1));
                                isFirstAddOperandNegative = true;
                            } else {
                                isFirstAddOperandNegative = false;
                                firstAddOperand = Double.parseDouble(String.valueOf(strings.get(i - 1)));
                            }
                            if (strings.get(i + 1).equals("-")) {
                                secondAddOperand = Double.parseDouble("-" + strings.get(i + 2));
                                strings.remove(i);
                            } else {
                                secondAddOperand = Double.parseDouble(String.valueOf(strings.get(i + 1)));
                            }
                            double sum = firstAddOperand + secondAddOperand;
                            if (isFirstAddOperandNegative) {
                                strings.set(i - 1, String.valueOf(Math.abs(sum)));
                                if (sum < 0) {
                                    strings.set(i - 2, "-");
                                } else {
                                    strings.set(i - 2, "+");
                                }
                            } else {
                                strings.set(i - 1, String.valueOf(sum));
                            }
                            strings.remove(i);
                            strings.remove(i);
                            System.out.println("New strings list:" + strings);
                            i = 0;
                            break;

                        case Constant.DIVIDE_OPERATOR:
                            double firstDivideOperand = Double.parseDouble(String.valueOf(strings.get(i - 1)));
                            double secondDivideOperand = Double.parseDouble(String.valueOf(strings.get(i + 1)));
                            strings.remove(i);
                            strings.remove(i);
                            if (secondDivideOperand == 0) {
                                return "Can't divide by 0";
                            }
                            strings.set(i - 1, String.valueOf(firstDivideOperand / secondDivideOperand));
                            System.out.println("New strings list:" + strings);
                            i = 0;
                            break;

                        case Constant.SUBTRACT_OPERATOR:
                            double firstSubtractOperand = Double.parseDouble(String.valueOf(strings.get(i - 1)));
                            double secondSubtractOperand = Double.parseDouble(String.valueOf(strings.get(i + 1)));
                            strings.remove(i);
                            strings.remove(i);
                            strings.set(i - 1, String.valueOf(firstSubtractOperand - secondSubtractOperand));
                            System.out.println("New strings list:" + strings);
                            i = 0;
                            break;
                    }
                } else {
                    i++;
                    System.out.println("In else:Value of i:" + i);
                }
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                return "Invalid expression";
            }
        }
        return strings.get(0);
    }

    private static boolean isStringContainingOperator(List<String> s) {
        return s.contains(Constant.MULTIPLY_OPERATOR) || s.contains(Constant.ADD_OPERATOR) ||
                s.contains(Constant.DIVIDE_OPERATOR) || s.contains(Constant.SUBTRACT_OPERATOR);
    }

    private static boolean isOperator(String val) {
        return val.equals(Constant.MULTIPLY_OPERATOR) || val.equals(Constant.ADD_OPERATOR) ||
                val.equals(Constant.DIVIDE_OPERATOR) || val.equals(Constant.SUBTRACT_OPERATOR);
    }
}
