package tree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class TreeSolution {
    public static void main(String[] args) {
        final TreeSolution treeSolution = new TreeSolution();
        final TreeNode root = new TreeNode(1, new TreeNode(2), new TreeNode(3));
        root.left.left = new TreeNode(3, new TreeNode(4), new TreeNode(5));
        root.right.right = new TreeNode(6, new TreeNode(7), new TreeNode(8));
        treeSolution.lowestCommonAncestor(root, root.left.left, root.right.right.left);
    }

    /**
     * https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-tree/
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == p || root == q) {
            return root;
        }
        final Stack<TreeNode> stack = new Stack<>();
        if (isParentOfAnother(p, q, stack)) return p;
        if (isParentOfAnother(q, p, stack)) return q;

        final HashMap<Integer, TreeNode> map = new HashMap<>();
        HashSet<TreeNode> visited = new HashSet<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            final TreeNode pop = stack.pop();
            if (pop.right != null) {
                map.put(pop.right.val, pop);
                if (pop.right == p || pop.right == q) {
                    TreeNode tmp = map.get(pop.right.val);
                    while (tmp != null) {
                        if (!visited.add(tmp)) {
                            return tmp;
                        }
                        tmp = map.get(tmp.val);
                    }
                } else {
                    stack.push(pop.right);
                }
            }
            if (pop.left != null) {
                map.put(pop.left.val, pop);
                if (pop.left == p || pop.left == q) {
                    TreeNode tmp = map.get(pop.left.val);
                    while (tmp != null) {
                        if (!visited.add(tmp)) {
                            return tmp;
                        }
                        tmp = map.get(tmp.val);
                    }
                } else {
                    stack.push(pop.left);
                }
            }
        }
        return root;
    }

    public boolean isParentOfAnother(TreeNode t1, TreeNode t2, Stack<TreeNode> stack) {
        stack.push(t1);
        while (!stack.isEmpty()) {
            final TreeNode pop = stack.pop();
            if (pop == t2) return true;
            if (pop.right != null) stack.push(pop.right);
            if (pop.left != null) stack.push(pop.left);
        }
        return false;
    }
}
