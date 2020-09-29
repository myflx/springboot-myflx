package tree;


import java.util.ArrayList;
import java.util.List;

public class TreeBuilder {
    public List<TreeNode> generateTrees(int n) {
        List<TreeNode> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.addAll(doGenerateTrees(i, n));
        }
        return list;
    }

    private List<TreeNode> doGenerateTrees(int root, int n) {
        List<TreeNode> list = new ArrayList<>();
        List<TreeNode> lefts = createTreeNode(0, root - 1);
        List<TreeNode> rights = createTreeNode(root + 1, n);
        for (TreeNode left : lefts) {
            for (TreeNode right : rights) {
                list.add(new TreeNode(root, left, right));
            }
        }
        return list;
    }

    /**
     * (1,2) 0-0 2-2
     * (2,2) 0-1 3-2
     * <p>
     * (1,3) 0-0 2-3
     * (2,3) 0-1 3-3
     * (3,3) 0-2 4-3
     *
     * @return
     */
    private List<TreeNode> createTreeNode(int s, int e) {
        List<TreeNode> list = new ArrayList<>();
        if (s == 0 && s == e) {
            list.add(null);
            return list;
        }
        if (s >= e) {
            list.add(new TreeNode(e));
            return list;
        }
        while (e-- > s) {
            list.addAll(generateTrees(e));
        }
        return list;
    }
}
