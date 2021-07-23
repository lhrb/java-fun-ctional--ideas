package com.github.lhrb.lenses;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;


public class Lens<T, R>
{
    private final Function<T,R> getFn;
    private final BiFunction<T,R,T> setFn;

    private Lens(Function<T, R> getFn, BiFunction<T, R, T> setFn)
    {
        this.getFn = getFn;
        this.setFn = setFn;
    }

    public static <T,R> Lens<T,R> create(Function<T, R> getFn, BiFunction<T, R, T> setFn)
    {
        return new Lens<>(getFn,setFn);
    }

    public R get(T t)
    {
        return getFn.apply(t);
    }

    public T set(T t, R r)
    {
        return setFn.apply(t, r);
    }

    private T mod(T t, Function<R, R> f)
    {
        return set(t, f.apply(get(t)));
    }


    public <V> Lens<V, R> compose(Lens<V, T> before) {
        Objects.requireNonNull(before);
        return new Lens<>(
                (v) -> get(before.get(v)),
                (v, r) -> before.mod(v, t -> set(t, r))
        );
    }

    public <V> Lens<T, V> andThen(Lens<R, V> after) {
        Objects.requireNonNull(after);
        return after.compose(this);
    }
}
