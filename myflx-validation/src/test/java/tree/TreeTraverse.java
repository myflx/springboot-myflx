package tree;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 对二叉树进行遍历
 * 深度遍历：前序遍历，中序遍历，后顺序遍历 （均相对于根节点来说的，根节点的输出顺序不同。子节点始终是左右遍历的顺序）
 * 广度遍历：层序遍历
 */
public class TreeTraverse {

    /**
     * 构建二叉树
     *
     * @param inputList inputList
     * @return 树节点
     */
    public static TreeNode createBinaryTree(LinkedList<Integer> inputList) {
        if (inputList == null || inputList.isEmpty()) {
            return null;
        }
        TreeNode treeNode = null;
        final Integer data = inputList.removeFirst();
        if (data != null) {
            treeNode = new TreeNode(data);
            treeNode.leftChild = createBinaryTree(inputList);
            treeNode.rightChild = createBinaryTree(inputList);
        }
        return treeNode;
    }

    /**
     * 前序遍历
     */
    public static void preOrderTravel(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        System.out.print(treeNode.getData());
        preOrderTravel(treeNode.leftChild);
        preOrderTravel(treeNode.rightChild);
    }

    /**
     * 中序遍历
     */
    public static void midOrderTravel(TreeNode treeNode){
        if (treeNode == null) {
            return;
        }
        midOrderTravel(treeNode.leftChild);
        System.out.print(treeNode.getData());
        midOrderTravel(treeNode.rightChild);
    }

    /**
     * 后序遍历
     */
    public static void postOrderTravel(TreeNode treeNode){
        if (treeNode == null) {
            return;
        }
        postOrderTravel(treeNode.leftChild);
        postOrderTravel(treeNode.rightChild);
        System.out.print(treeNode.getData());
    }

    /**
     *            3
     *       2        8
     *    9   10  nil    4
     */
    public static void main(String[] args) {
        final TreeNode binaryTree = TreeTraverse.createBinaryTree(new LinkedList<>(Arrays.asList(3, 2, 9, null, null, 10, null, null, 8, null, 4)));
        System.out.println("前序遍历：  ");
        preOrderTravel(binaryTree);
        System.out.println("中序遍历：  ");
        midOrderTravel(binaryTree);
        System.out.println("后序遍历：  ");
        postOrderTravel(binaryTree);
    }
}
