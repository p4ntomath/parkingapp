package com.example.parkingapp;

public class Quartet<A, B, C, D> {
    public A first;
    private final B second;
    private final C third;
    private final D fourth;
    public boolean isBooked;

    public Quartet(A first, B second, C third, D fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        isBooked = false;
    }

    public void setIsBooked(boolean state) {
        isBooked = state;
    }

    public boolean getIsBooked() {
        return isBooked;
    }


    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    public C getThird() {
        return third;
    }

    public D getFourth() {
        return fourth;
    }

    @Override
    public String toString() {
        return "Quartet{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                ", fourth=" + fourth +
                '}';
    }
}
