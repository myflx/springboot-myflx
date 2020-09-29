package tree;

/**
 * @author LuoShangLin
 */
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int data) {
        this.val = data;
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    public int getVal() {
        return val;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TreeNode{");
        sb.append("data=").append(val);
        sb.append(", leftChild=").append(left);
        sb.append(", rightChild=").append(right);
        sb.append('}');
        return sb.toString();
    }
}
