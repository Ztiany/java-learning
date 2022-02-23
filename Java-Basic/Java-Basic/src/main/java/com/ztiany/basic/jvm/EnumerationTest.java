package com.ztiany.basic.jvm;

public class EnumerationTest {

    public static void main(String[] args) {
        Color color = Color.RED;
        switch (color) {
            case RED: {
                System.out.println("Haha");
            }
            case BLACK: {
            }
            case YELLOW: {
            }
        }
    }

    enum Color {
        RED, YELLOW, BLACK, WHITE
    }

}
