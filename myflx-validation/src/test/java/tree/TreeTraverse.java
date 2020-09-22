package tree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

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
    public static void midOrderTravel(TreeNode treeNode) {
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
    public static void postOrderTravel(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        postOrderTravel(treeNode.leftChild);
        postOrderTravel(treeNode.rightChild);
        System.out.print(treeNode.getData());
    }

    /**
     * 前序遍历(利用栈实现)
     */
    public static void preOrderTravelWithStack(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(treeNode);
        while (!stack.isEmpty()) {
            final TreeNode pop = stack.pop();
            System.out.println(pop.getData());
            if (pop.rightChild != null) {
                stack.push(pop.rightChild);
            }
            if (pop.leftChild != null) {
                stack.push(pop.leftChild);
            }
        }
    }

    /**
     *         3
     *    2        8
     * 9   10  nil    4
     * 中序遍历(利用栈实现)
     */
    public static void midOrderTravelWithStack(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(treeNode);
        while (!stack.isEmpty()) {
            //新节点开始遍历的时候添加所有的左节孩子入栈
            TreeNode r = stack.peek().leftChild;
            while (r != null) {
                stack.push(r);
                r = r.leftChild;
            }
            //所有左孩子入栈完毕，一直pop
            while (!stack.isEmpty()){
                TreeNode pop = stack.pop();
                System.out.println(pop.getData());
                if (pop.rightChild != null){
                    //如果某个节点存在右孩子那么结束弹栈，从头开始进入下个循环
                    stack.push(pop.rightChild);
                    break;
                }
            }
        }
    }
    public static void midOrderTravelWithStack2(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(treeNode);
        while (!stack.isEmpty()) {
            //新节点开始遍历的时候添加所有的左节孩子入栈
            TreeNode r = stack.peek().leftChild;
            while (r != null) {
                stack.push(r);
                r = r.leftChild;
            }
            //所有左孩子入栈完毕，一直pop
            while (!stack.isEmpty()){
                TreeNode pop = stack.pop();
                System.out.println(pop.getData());
                if (pop.rightChild != null){
                    //如果某个节点存在右孩子那么结束弹栈，从头开始进入下个循环
                    stack.push(pop.rightChild);
                    break;
                }
            }
        }
    }

    /**
     *         3
     *    2        8
     * 9   10  nil    4
     * 后序遍历(利用栈实现)
     */
    public static void postOrderTravelWithStack(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(treeNode);
        while (!stack.isEmpty()) {
            TreeNode peek = stack.peek();
            if (peek.leftChild != null){
                stack.push(peek.leftChild);
            }else {
                while (!stack.isEmpty()){
                    TreeNode pop = stack.pop();
                    System.out.println(pop.getData());
                    if (pop.rightChild != null){
                        stack.push(pop.rightChild);
                        break;
                    }
                }
            }

        }
    }
    /**
     * 3
     * 2        8
     * 9   10  nil    4
     */
    public static void main(String[] args) {
        final TreeNode binaryTree = TreeTraverse.createBinaryTree(new LinkedList<>(Arrays.asList(3, 2, 9, null, null, 10, null, null, 8, null, 4)));
        System.out.println("前序遍历：  ");
        preOrderTravel(binaryTree);
        System.out.println("中序遍历：  ");
        midOrderTravel(binaryTree);
        System.out.println("后序遍历：  ");
        postOrderTravel(binaryTree);

        System.out.println("栈前序遍历：  ");
        preOrderTravelWithStack(binaryTree);
        System.out.println("栈中序遍历：  ");
        midOrderTravelWithStack(binaryTree);
        System.out.println("栈后序遍历：  ");
        postOrderTravelWithStack(binaryTree);
    }
}
