package tree;

import java.util.ArrayList;
import java.util.List;

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

    public static void main(String[] args) {
        TreeNode treeNode = BSTTree.sortedArrayToBST(new Integer[]{-10, -3, 0, 5, 9});
        TreeNode treeNode1 = new BSTTree().balanceBST(treeNode);
        System.out.println(treeNode1);
    }
}
