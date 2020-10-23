package tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BSTTree {
    public TreeNode balanceBST(TreeNode root) {
        List<Integer> outList = new ArrayList<>();
        midOrderTravel(root, outList);
        Integer[] ts = outList.toArray(new Integer[0]);
        return sortedArrayToBST(ts);
    }

    private void midOrderTravel(TreeNode root, List<Integer> outList) {
        if (root == null) {
            return;
        }
        midOrderTravel(root.left, outList);
        outList.add(root.val);
        midOrderTravel(root.right, outList);
    }

    public static TreeNode sortedArrayToBST(Integer[] nums) {
        return sortedArrayToBST(nums, 0, nums.length - 1);
    }

    public static TreeNode sortedArrayToBST(Integer[] nums, int s, int e) {
        if (e < s) {
            return null;
        }
        int mid = (s + e) / 2;
        TreeNode treeNode = new TreeNode(nums[mid]);
        treeNode.left = sortedArrayToBST(nums, s, mid - 1);
        treeNode.right = sortedArrayToBST(nums, mid + 1, e);
        return treeNode;
    }


    /**
     * 最近公共祖先
     * 关键点：搜索树
     * 一次循环
     */
    public TreeNode lowestCommonAncestor3(TreeNode root, TreeNode p, TreeNode q) {
        if (p.left == q || p.right == q) {
            return p;
        }
        if (q.left == p || q.right == p) {
            return q;
        }
        if (p == root || q == root) {
            return root;
        }
        TreeNode current = root;
        while (current != null) {
            if (current.val > p.val && current.val > q.val) {
                current = current.left;
                continue;
            }
            if (current.val < p.val && current.val < q.val) {
                current = current.right;
                continue;
            }
            if ((current.val < p.val && current.val > q.val) || (current.val > p.val && current.val < q.val)) {
                return current;
            }
            if (current.val == p.val || current.val == q.val) {
                return current;
            }
        }
        return current;
    }

    /**
     * 最近公共祖先
     * 关键点：搜索树
     * 二次循环
     */
    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        if (p.left == q || p.right == q) {
            return p;
        }
        if (q.left == p || q.right == p) {
            return q;
        }
        if (p == root || q == root) {
            return root;
        }
        List<TreeNode> path = new ArrayList<>();
        TreeNode current = root;
        while (current != null) {
            path.add(current);
            if (current.val == p.val) {
                break;
            }
            if (current.val < p.val) {
                current = current.right;
            } else {
                current = current.left;
            }
        }

        List<TreeNode> path2 = new ArrayList<>();
        current = root;
        while (current != null) {
            path2.add(current);
            if (current.val == q.val) {
                break;
            }
            if (current.val < q.val) {
                current = current.right;
            } else {
                current = current.left;
            }
        }
        TreeNode lca = null;
        for (int i = 0; (i < path.size() && i < path2.size()); i++) {
            if (path.get(i) == path2.get(i)) {
                lca = path.get(i);
            } else {
                break;
            }
        }
        return lca;
    }


    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (p.val == root.val) return p;
        if (q.val == root.val) return q;
        if (p.val > root.val && q.val > root.val) {
            return lowestCommonAncestor(root.right, p, q);
        } else if (p.val < root.val && q.val < root.val) {
            return lowestCommonAncestor(root.left, p, q);
        } else {
            return root;
        }
    }


    /**
     * 230. 二叉搜索树中第K小的元素
     * https://leetcode-cn.com/problems/kth-smallest-element-in-a-bst/
     */
    public int kthSmallest(TreeNode root, int k) {
        int val = root.val;
        //中序遍历
        final Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            final TreeNode peek = stack.peek();
            if (peek.left != null) {
                stack.push(peek.left);
            } else {
                while (!stack.isEmpty()) {
                    TreeNode pop = stack.pop();
                    val = pop.val;
                    if (--k == 0) {
                        return val;
                    }
                    if (pop.right != null) {
                        stack.push(pop.right);
                        break;
                    }
                }
            }
        }
        return val;
    }

    public static void main(String[] args) {
        /*TreeNode treeNode = BSTTree.sortedArrayToBST(new Integer[]{-10, -3, 0, 5, 9});
        TreeNode treeNode1 = new BSTTree().balanceBST(treeNode);
        System.out.println(treeNode1);*/
        TreeNode binaryTree = new TreeNode(6, new TreeNode(2, new TreeNode(0), new TreeNode(4, new TreeNode(3), new TreeNode(5))), new TreeNode(8, new TreeNode(7), new TreeNode(9)));
        TreeNode treeNode = new BSTTree().lowestCommonAncestor(binaryTree, binaryTree.left.right.left, binaryTree.left.right.right);
        System.out.println(treeNode.val == 4);
    }
}
