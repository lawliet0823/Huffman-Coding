import java.io.*;
import java.util.Arrays;

/**
 * Created by L on 2014/10/26.
 */
public class AdaptiveHuffmanDecode {
    public static void main(String args[]) throws IOException {
        File inputFile = new File("test.txt");
        File outputFile = new File("output.raw");

        BitInputStream bitInputStream = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

        int[] initFreqs = new int[257];
        Arrays.fill(initFreqs, 1);

        FrequencyTable frequencyTable = new FrequencyTable(initFreqs);
        HuffmanDecoder huffmanDecoder = new HuffmanDecoder(bitInputStream);
        huffmanDecoder.codeTree = frequencyTable.buildCodeTree();
        int count = 0;
        while (true) {
            int symbol = huffmanDecoder.read();
            if (symbol == 256) {
                break;
            }
            outputStream.write(symbol);

            frequencyTable.increment(symbol);
            count++;
            if (count < 262144 && isPowerOf2(count) || count % 262144 == 0) {
                huffmanDecoder.codeTree = frequencyTable.buildCodeTree();
            }
            if (count % 262144 == 0) {
                frequencyTable = new FrequencyTable(initFreqs);
            }
        }

        bitInputStream.close();
        outputStream.close();
    }

    private static boolean isPowerOf2(int x) {
        return x > 0 && (x & -x) == x;
    }
}
