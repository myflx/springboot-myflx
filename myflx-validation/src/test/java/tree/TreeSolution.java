package tree;

import java.util.HashSet;
import java.util.Stack;
import java.util.TreeSet;

public class TreeSolution {
    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(2, new TreeNode(-1), null);
        int i = new TreeSolution().maxPathSum(treeNode);
        System.out.println(i);
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
     * 树的LCA
     * 不一定是BS无法直接用搜索树的概念
     * https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-tree/
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == p || root == q) return root;
        //同树判断
        Stack<TreeNode> stack = new Stack<>();
        if (isParenOfAnother(p, q, stack)) return p;
        if (isParenOfAnother(q, p, stack)) return q;
        HashSet<TreeNode> searched = new HashSet<>();
        stack.push(root.left);
        stack.push(root.right);

        TreeNode a = null;
        //后序遍历二次遍历根节点
        while (!stack.isEmpty()) {
            TreeNode peek = stack.peek();
            if (peek.left != null) {
                if (peek.left == p || peek.left == q) {
                    a = peek;
                    continue;
                }
                stack.push(peek.left);
            } else {
                while (!stack.isEmpty()) {
                    TreeNode pop = stack.pop();
                    if (pop.right != null) {
                        stack.push(pop.right);
                        break;
                    }
                }
            }
        }

        return root;
    }


    /**
     * 判断t是不是t2的parent
     */
    public boolean isParenOfAnother(TreeNode t, TreeNode t2, Stack<TreeNode> stack) {
        stack.push(t);
        while (!stack.isEmpty()) {
            TreeNode pop = stack.pop();
            if (pop == t2) return true;
            if (pop.right != null) stack.push(pop.right);
            if (pop.left != null) stack.push(pop.left);
        }
        return false;
    }
}
