import java.util.*;

/**
 * Created by L on 2014/9/25.
 */
public final class CodeTree {
    public final InternalNode root;
    private List<List<Integer>> codes;
    private static Map<Integer, String> map = new HashMap<Integer, String>();

    public CodeTree(InternalNode root, int symbolLimit) {
        if (root == null) {
            throw new NullPointerException("Argument is null");
        }
        this.root = root;
        codes = new ArrayList<List<Integer>>();
        for (int i = 0; i < symbolLimit; i++) {
            codes.add(null);
        }
        buildCodeList(root, new ArrayList<Integer>());
    }

    private void buildCodeList(Node node, List<Integer> prefix) {
        if (node instanceof InternalNode) {
            InternalNode internalNode = (InternalNode) node;

            prefix.add(0);
            buildCodeList(internalNode.leftChild, prefix);
            prefix.remove(prefix.size() - 1);

            prefix.add(1);
            buildCodeList(internalNode.rightChild, prefix);
            prefix.remove(prefix.size() - 1);

        } else if (node instanceof Leaf) {
            Leaf leaf = (Leaf) node;
            if (leaf.symbol >= codes.size())
                throw new IllegalArgumentException("Symbol exceeds symbol limit");
            if (codes.get(leaf.symbol) != null)
                throw new IllegalArgumentException("Symbol has more than one code");
            codes.set(leaf.symbol, new ArrayList<Integer>(prefix));

        } else {
            throw new AssertionError("Illegal node type");
        }
    }

    public List<Integer> getCode(int symbol) {
        if (symbol < 0)
            throw new IllegalArgumentException("Illegal symbol");
        else if (codes.get(symbol) == null)
            throw new IllegalArgumentException("No code for given symbol");
        else
            return codes.get(symbol);
    }

    public Map getMap() {
        StringBuilder sb = new StringBuilder();
        getMap("", root, sb);
        return map;
    }

    private void getMap(String prefix, Node node, StringBuilder sb) {
        if (node instanceof InternalNode) {
            InternalNode internalNode = (InternalNode) node;
            getMap(prefix + "0", internalNode.leftChild, sb);
            getMap(prefix + "1", internalNode.rightChild, sb);
        } else if (node instanceof Leaf) {
            map.put(((Leaf) node).symbol, prefix);
        } else {
            throw new AssertionError("Illegal node type");
        }
    }
}
