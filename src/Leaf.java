/**
 * Created by L on 2014/9/25.
 */
public final class Leaf extends Node {
    public final int symbol;

    public Leaf(int symbol) {
        if (symbol < 0) {
            throw new IllegalArgumentException("Illegal symbol value");
        }
        this.symbol = symbol;
    }
}
