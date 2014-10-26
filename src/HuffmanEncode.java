import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by L on 2014/9/24.
 */
public class HuffmanEncode {
    public static void main(String args[]) throws IOException {

        //File IO
        File inputFile = new File("Lena.raw");
        FileInputStream fileInputStream = new FileInputStream("Lena.raw");
        FileOutputStream fileOutputStream = new FileOutputStream("test.txt");

        Map<Integer, String> map = new HashMap<Integer, String>();

//        //read data
//        int tempReader;
//        int originlData[] = new int[257];
//
//        for (int i = 0; i < 512 * 512; i++) {
//            tempReader = fileInputStream.read();
//            if (tempReader < 0)
//                break;
//            originlData[tempReader]++;
//        }

        //Translate frequncyTable into CodeTree
        FrequencyTable frequencyTable = getFrequencies(inputFile);
        frequencyTable.increment(256);
        CodeTree codeTree = frequencyTable.buildCodeTree();
        CanonicalCode canonicalCode = new CanonicalCode(codeTree, 257);
        codeTree = canonicalCode.toCodeTree();

        //map = codeTree.getMap();
        //fileInputStream.close();

        //fileInputStream = new FileInputStream("Lena.raw");

        //Output Bitstream
        InputStream in = new BufferedInputStream(new FileInputStream(inputFile));
        BitOutputStream bitOutputStream = new BitOutputStream(fileOutputStream);

        writeCode(bitOutputStream, canonicalCode);
        compress(codeTree, in, bitOutputStream);

        bitOutputStream.close();
        in.close();

//        for (int i = 0; i < 512 * 512; i++) {
//            tempReader = fileInputStream.read();
//            if (map.containsKey(tempReader)) {
//                String tempString = map.get(tempReader) + "a";
//                for (int j = 0; j < tempString.length() - 1; j++) {
//                    bitOutputStream.write(Integer.parseInt(tempString.substring(j, j + 1)));
//                }
//            }
//        }
    }

    private static FrequencyTable getFrequencies(File file) throws IOException {
        FrequencyTable freq = new FrequencyTable(new int[257]);
        InputStream input = new BufferedInputStream(new FileInputStream(file));
        try {
            while (true) {
                int b = input.read();
                if (b == -1)
                    break;
                freq.increment(b);
            }
        } finally {
            input.close();
        }
        return freq;
    }

    static void writeCode(BitOutputStream out, CanonicalCode canonCode) throws IOException {
        for (int i = 0; i < canonCode.getSymbolLimit(); i++) {
            int val = canonCode.getCodeLength(i);
            // For this file format, we only support codes up to 255 bits long
            if (val >= 256)
                throw new RuntimeException("The code for a symbol is too long");

            // Write value as 8 bits in big endian
            for (int j = 7; j >= 0; j--)
                out.write((val >>> j) & 1);
        }
    }

    static void compress(CodeTree code, InputStream in, BitOutputStream out) throws IOException {
        HuffmanEncoder enc = new HuffmanEncoder(out);
        enc.codeTree = code;
        while (true) {
            int b = in.read();
            if (b == -1)
                break;
            enc.write(b);
        }
        enc.write(256);  // EOF
    }
}
