package com.example.func;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class PredicateBuilder<T, R> implements Function<T, R> {

    private final Function<T, R> function;

    private PredicateBuilder(Function<T, R> function) {
        this.function = function;
    }

    private static <T, R> PredicateBuilder<T, R> with(Function<T, R> function) {
        return new PredicateBuilder<>(function);
    }


    @Override
    public R apply(T t) {
        return function.apply(t);
    }

    @Override
    public <V> PredicateBuilder<V, R> compose(Function<? super V, ? extends T> before) {
        return new PredicateBuilder<>(function.compose(before));
    }

    @Override
    public <V> PredicateBuilder<T, V> andThen(Function<? super R, ? extends V> after) {
        return new PredicateBuilder<>(function.andThen(after));
    }

    public Predicate<T> build(Predicate<R> predicate) {
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
                        .andThen(Function.identity())
                        .build(Objects::nonNull))
                .forEach(System.out::println);

    }
}
