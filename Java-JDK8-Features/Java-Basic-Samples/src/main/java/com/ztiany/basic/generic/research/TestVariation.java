package com.ztiany.basic.generic.research;

public class TestVariation {

    static class TypeA<T extends Number> {

        public T value;

    }

    static void acceptTypeA1(TypeA<?> typeA) {

    }

    static <T extends Number> void acceptTypeA2(TypeA<T> typeA) {

    }

    public static void main(String[] args) {
        acceptTypeA1(new TypeA<Integer>());
    }

}
