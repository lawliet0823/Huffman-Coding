import java.io.*;
import java.util.Arrays;

/**
 * Created by L on 2014/10/25.
 */
public class AdaptiveHuffmanEncode {
    public static void main(String args[]) throws IOException {
        File inputFile = new File("Lena.raw");
        File outputFile = new File("test.txt");
        InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
        BitOutputStream bitOutputStream = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));

        int[] initFreqs = new int[257];
        Arrays.fill(initFreqs, 1);
        FrequencyTable frequencyTable = new FrequencyTable(initFreqs);
        HuffmanEncoder huffmanEncoder = new HuffmanEncoder(bitOutputStream);
        huffmanEncoder.codeTree = frequencyTable.buildCodeTree();
        int count = 0;
        while (true) {
            int temp = inputStream.read();
            if (temp == -1) {
                break;
            }
            huffmanEncoder.write(temp);
            frequencyTable.increment(temp);
            count++;
            if (count < 262144 && isPowerOf2(count) || count % 262144 == 0) {
                huffmanEncoder.codeTree = frequencyTable.buildCodeTree();
            }
            if (count % 262144 == 0) {
                frequencyTable = new FrequencyTable(initFreqs);
            }
        }
        huffmanEncoder.write(256);

        inputStream.close();
        bitOutputStream.close();
    }

    private static boolean isPowerOf2(int x) {
        return x > 0 && (x & -x) == x;
    }
}
