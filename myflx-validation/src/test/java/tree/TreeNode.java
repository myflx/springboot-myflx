package tree;

/**
 * @author LuoShangLin
 */
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;
    public int delta;

    public TreeNode(int data) {
        this.val = data;
    }

    public TreeNode(int data, TreeNode left, TreeNode right) {
        this.val = data;
        this.left = left;
        this.right = right;
    }

    public TreeNode getAddDeltaTreeNode() {
        final TreeNode treeNode = new TreeNode(val + delta);
        if (left != null) {
            left.delta = this.delta;
            treeNode.left = left.getAddDeltaTreeNode();
        }
        if (right != null) {
            right.delta = this.delta;
            treeNode.right = right.getAddDeltaTreeNode();
        }
        return treeNode;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append(val);
        sb.append(", left=").append(left);
        sb.append(", right=").append(right);
        sb.append('}');
        return sb.toString();
    }
}
