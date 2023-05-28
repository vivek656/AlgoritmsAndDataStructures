package common;

public interface TripleConsumer<X, Y, Z, R> {

    R apply(X x, Y y, Z z);

}
