package tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author LuoShangLin
 * @Date 2020/9/25 11:39
 * @Since V2.13.0
 */
public class TreeBuilder {

    /**
     * 每个
     */
    static Map<Integer, List<TreeNode>> CACHE_MAP = new HashMap<>();

    static {
        CACHE_MAP.put(1, new ArrayList<>());
        CACHE_MAP.put(2, new ArrayList<>());
        CACHE_MAP.get(1).add(new TreeNode(1));
        CACHE_MAP.get(2).add(new TreeNode(1, null, new TreeNode(2)));
        CACHE_MAP.get(2).add(new TreeNode(2, new TreeNode(1), null));
    }

    public static void main(String[] args) {
        System.out.println(new TreeBuilder().generateTrees(0));
    }

    public List<TreeNode> generateTrees(int n) {
        if (n == 0) {
            return new ArrayList<>();
        }
        final List<TreeNode> treeNodes = CACHE_MAP.get(n);
        if (treeNodes != null) {
            return treeNodes;
        }
        List<TreeNode> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            final List<TreeNode> leftTreeNodes = doGenerateTrees(i - 1, 0);
            final List<TreeNode> rightTreeNodes = doGenerateTrees(n - i, i);
            if (rightTreeNodes.isEmpty()) {
                rightTreeNodes.add(null);
            }
            if (leftTreeNodes.isEmpty()) {
                leftTreeNodes.add(null);
            }
            for (TreeNode leftTreeNode : leftTreeNodes) {
                for (TreeNode rightTreeNode : rightTreeNodes) {
                    list.add(new TreeNode(i, leftTreeNode, rightTreeNode));
                }
            }
        }
        CACHE_MAP.put(n, list);
        System.out.println("n=" + n + "长度：" + list.size());
        return list;
    }

    public List<TreeNode> doGenerateTrees(int n, int delta) {
        List<TreeNode> generateTrees = generateTrees(n);
        if (delta == 0) {
            return generateTrees;
        }
        List<TreeNode> outList = new ArrayList<>();
        for (TreeNode treeNode : generateTrees) {
            TreeNode newNode = null;
            if (treeNode != null) {
                treeNode.delta = delta;
                newNode = treeNode.getAddDeltaTreeNode();
            }
            outList.add(newNode);
        }
        return outList;
    }


    public int numTrees(int n) {
        long G = 1;
        for (int i = 0; i < n; i++) {
            G = G * 2 * (2 * i + 1) / (i + 2);
        }
        return (int) G;
    }

    public int numTrees2(int n) {
        if (n < 0) {
            return 0;
        }
        if (n <= 1) {
            return 1;
        }
        int[] G = new int[n + 1];
        G[0] = 1;
        G[1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                G[i] += G[j - 1] * G[i - j];
            }
        }
        return G[n];
    }


}
