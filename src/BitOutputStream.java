import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by L on 2014/9/25.
 */
public final class BitOutputStream {

    private OutputStream output;

    private int currentByte;

    private int numBitsInCurrentByte;

    public BitOutputStream(OutputStream out) {
        if (out == null)
            throw new NullPointerException("Argument is null");
        output = out;
        currentByte = 0;
        numBitsInCurrentByte = 0;
    }

    public void write(int b) throws IOException {
        if (!(b == 0 || b == 1))
            throw new IllegalArgumentException("Argument must be 0 or 1 or 2");
        currentByte = currentByte << 1 | b;
        numBitsInCurrentByte++;
        if (numBitsInCurrentByte == 8) {
            output.write(currentByte);
            numBitsInCurrentByte = 0;
        }
    }

    public void close() throws IOException {
        while (numBitsInCurrentByte != 0)
            write(0);
        output.close();
    }
}
