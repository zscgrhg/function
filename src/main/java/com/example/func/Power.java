package com.example.func;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class Power<T> implements Predicate<T>, Function<T, Boolean> {

    private final Predicate<T> p;

    private Power(Predicate<T> p) {
        this.p = p;
    }

    public static <T> Power<T> just(Predicate<T> p) {
        return new Power<>(p);
    }

    public static <T> Power<T> just(Function<T, Boolean> p) {
        return new Power<>(p::apply);
    }

    @Override
    public Boolean apply(T t) {
        return p.test(t);
    }

    @Override
    public <V> Power<V> compose(Function<? super V, ? extends T> before) {
        return new Power<>(v -> p.test(before.apply(v)));
    }


    @Override
    public boolean test(T t) {
        return p.test(t);
    }

    @Override
    public Power<T> and(Predicate<? super T> other) {
        return new Power<>(p.and(other));
    }

    @Override
    public Power<T> negate() {
        return new Power<>(p.negate());
    }

    @Override
    public Power<T> or(Predicate<? super T> other) {
        return new Power<>(p.or(other));
    }

    public static void main(String[] args) {
        List<People> data=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if(i%2==0){
                data.add(new People(String.valueOf(i),i));
            }
        }

        data.stream()
                .filter(Power.just(Objects::nonNull)
                        .compose(People::getName))
                .forEach(System.out::println);
    }
}
