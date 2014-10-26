import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by L on 2014/10/22.
 */
public class HuffmanDecode {
    public static void main(String args[]) throws IOException {

        File inputFile = new File("test.txt");
        File outputFile = new File("output.raw");

        BitInputStream bitInputStream = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)));

        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
        CanonicalCode canonCode = readCode(bitInputStream);
        CodeTree code = canonCode.toCodeTree();
        decompress(code, bitInputStream, outputStream);
        outputStream.close();
        bitInputStream.close();
    }

    static CanonicalCode readCode(BitInputStream in) throws IOException {
        int[] codeLengths = new int[257];
        for (int i = 0; i < codeLengths.length; i++) {
            // For this file format, we read 8 bits in big endian
            int val = 0;
            for (int j = 0; j < 8; j++)
                val = val << 1 | in.readNoEof();
            codeLengths[i] = val;
        }
        return new CanonicalCode(codeLengths);
    }


    static void decompress(CodeTree code, BitInputStream in, OutputStream out) throws IOException {
        HuffmanDecoder dec = new HuffmanDecoder(in);
        dec.codeTree = code;
        while (true) {
            int symbol = dec.read();
            if (symbol == 256)  // EOF symbol
                break;
            out.write(symbol);
        }
    }
}

