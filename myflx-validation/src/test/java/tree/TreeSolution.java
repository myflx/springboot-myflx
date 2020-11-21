package tree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.TreeSet;

public class TreeSolution {
    public static void main(String[] args) {
        final TreeSolution treeSolution = new TreeSolution();
        final TreeNode root = new TreeNode(1, new TreeNode(2), new TreeNode(3));
        root.left.left = new TreeNode(3, new TreeNode(4), new TreeNode(5));
        root.right.right = new TreeNode(6, new TreeNode(7), new TreeNode(8));
        treeSolution.lowestCommonAncestor(root, root.left.left, root.right.right.left);
    }


    /**
     * 124. 二叉树中的最大路径和
     * 给定一个非空二叉树，返回其最大路径和。
     * <p>
     * 本题中，路径被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。该路径至少包含一个节点，且不一定经过根节点。
     * <p>
     * https://leetcode-cn.com/problems/binary-tree-maximum-path-sum/
     */
    public int maxPathSum(TreeNode root) {
        TreeSet<Integer> reachableTree = new TreeSet<>();
        TreeSet<Integer> unReachableTree = new TreeSet<>();
        maxPathSumHelp(root, reachableTree, unReachableTree);
        if (unReachableTree.isEmpty()) {
            return reachableTree.last();
        } else {
            return Math.max(reachableTree.last(), unReachableTree.last());
        }
    }

    public void maxPathSumHelp(TreeNode root, TreeSet<Integer> reachableTree, TreeSet<Integer> unReachableTree) {
        if (root.left == null && root.right == null) {
            reachableTree.add(root.val);
            return;
        }
        TreeSet<Integer> reachableTreeL = new TreeSet<>();
        if (root.left != null) {
            maxPathSumHelp(root.left, reachableTreeL, unReachableTree);
        }
        TreeSet<Integer> reachableTreeR = new TreeSet<>();
        if (root.right != null) {
            maxPathSumHelp(root.right, reachableTreeR, unReachableTree);
        }
        if (reachableTreeL.isEmpty() && !reachableTreeR.isEmpty()) {
            unReachableTree.add(reachableTreeR.last());
            reachableTree.add(reachableTreeR.last() + root.val);
        } else if (!reachableTreeL.isEmpty() && reachableTreeR.isEmpty()) {
            unReachableTree.add(reachableTreeL.last());
            reachableTree.add(reachableTreeL.last() + root.val);
        } else {
            unReachableTree.add(reachableTreeL.last());
            unReachableTree.add(reachableTreeR.last());
            unReachableTree.add(reachableTreeL.last() + reachableTreeR.last() + root.val);
            reachableTree.add(reachableTreeL.last() + root.val);
            reachableTree.add(reachableTreeR.last() + root.val);
        }
        reachableTree.add(root.val);
        while (unReachableTree.size() > 1) {
            unReachableTree.pollFirst();
        }
        while (reachableTree.size() > 1) {
            reachableTree.pollFirst();
        }
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
