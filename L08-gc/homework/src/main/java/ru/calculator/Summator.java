package ru.calculator;

public class Summator {
    private int sum = 0;
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private int someValue = 0;
    private int offset = 0;

    public void calc(int data) {
        offset++;
        if (offset == 6_600_000) {
            offset = 0;
        }

        sum += data;

        sumLastThreeValues = data + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = data;
        for (var idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (data + 1) - sum);
            someValue = Math.abs(someValue) + offset;
        }
    }

    public int getSum() {
        return sum;
    }

    public int getPrevValue() {
        return prevValue;
    }

    public int getPrevPrevValue() {
        return prevPrevValue;
    }

    public int getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public int getSomeValue() {
        return someValue;
    }
}
