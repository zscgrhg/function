package com.example.func;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionBuilder<T, R> implements Function<T, R> {

    private final Function<T, R> function;

    private FunctionBuilder(Function<T, R> function) {
        this.function = function;
    }

    private static <T, R> FunctionBuilder<T, R> with(Function<T, R> function) {
        return new FunctionBuilder<>(function);
    }


    @Override
    public R apply(T t) {
        return function.apply(t);
    }

    @Override
    public <V> FunctionBuilder<V, R> compose(Function<? super V, ? extends T> before) {
        return new FunctionBuilder<>(function.compose(before));
    }

    @Override
    public <V> FunctionBuilder<T, V> andThen(Function<? super R, ? extends V> after) {
        return new FunctionBuilder<>(function.andThen(after));
    }

    public Predicate<T> test(Predicate<R> predicate) {
        Function<? super R, Boolean> after = (R r) -> predicate.test(r);
        return function.andThen(after)::apply;
    }

    public static void main(String[] args) {
        List<People> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                data.add(new People(String.valueOf(i), i));
            }
        }

        data.stream()
                .filter(with(People::getName)
                        .test(Objects::nonNull))
                .forEach(System.out::println);

    }
}
