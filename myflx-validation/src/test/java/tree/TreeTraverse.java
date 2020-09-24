package tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
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
    public static void postOrderTravelWithStack(TreeNode root) {
        if (root == null) {
            return;
        }
        //记录是否回溯过
        List<TreeNode> traced = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode peek = stack.peek();
            if (peek.leftChild != null){
                stack.push(peek.leftChild);
            }else {
                while (!stack.isEmpty()){
                    TreeNode pop = stack.peek();
                    if (!traced.contains(pop) && pop.rightChild != null){
                        traced.add(pop);
                        stack.push(pop.rightChild);
                        break;
                    }else {
                        stack.pop();
                        System.out.println(pop.getData());
                    }
                }
            }

        }
    }


    public static void postOrderTravelWithStack2(TreeNode root) {
        if (root == null) {
            return;
        }
        //使用空节点记录是否回溯过
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()){
            TreeNode peek = stack.peek();
            if (peek == null){
                stack.pop();
                final TreeNode pop = stack.pop();
                System.out.println(pop.getData());
                continue;
            }
            stack.push(null);
            if (peek.rightChild != null){
                stack.push(peek.rightChild);
            }

            if (peek.leftChild != null){
                stack.push(peek.leftChild);
            }
        }
    }


    /**
     *         3
     *    2        8
     * 9   10  nil    4
     * 层序遍历(队列实现)
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ret = new ArrayList<>();
        if (Objects.isNull(root)){
            return ret;
        }
        TreeNode split = new TreeNode(0);
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        queue.offer(split);
        while (!queue.isEmpty()){
            List<Integer> out = new ArrayList<>();
            TreeNode poll = queue.poll();
            while (poll != null && split != poll){
                out.add(poll.getData());
                if (poll.leftChild != null){
                    queue.offer(poll.leftChild);
                }
                if (poll.rightChild != null){
                    queue.offer(poll.rightChild);
                }
                poll = queue.poll();
            }
            if (!queue.isEmpty()){
                queue.offer(split);
            }
            ret.add(out);
        }
        return ret;
    }


    /**
     *         3
     *    2        8
     * 9   10  nil    4
     * 层序遍历(递归实现)
     */
    public List<List<Integer>> levelOrder2(TreeNode root) {
        if (Objects.isNull(root)){
            return null;
        }
        List<List<Integer>> ret = new ArrayList<>();
        doLevelOrder(root,ret,0);
        return ret;
    }

    public void doLevelOrder(TreeNode node, List<List<Integer>> ret, int level) {
        if (Objects.isNull(node)) {
            return;
        }
        if (ret.size() <= level) {
            ret.add(new ArrayList<>());
        }
        ret.get(level).add(node.getData());
        doLevelOrder(node.leftChild, ret, level + 1);
        doLevelOrder(node.rightChild, ret, level + 1);
    }
    class QueueNode {
        TreeNode node;
        int depth;

        public QueueNode(TreeNode node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }


    /***
     * 第一个搜索到的叶子节点一定是最小深度
     * @param root
     * @return
     */
    public int minDepth1(TreeNode root) {
        if (root == null) {
            return 0;
        }

        Queue<QueueNode> queue = new LinkedList<QueueNode>();
        queue.offer(new QueueNode(root, 1));
        while (!queue.isEmpty()) {
            QueueNode nodeDepth = queue.poll();
            TreeNode node = nodeDepth.node;
            int depth = nodeDepth.depth;
            if (node.leftChild == null && node.rightChild == null) {
                return depth;
            }
            if (node.leftChild != null) {
                queue.offer(new QueueNode(node.leftChild, depth + 1));
            }
            if (node.rightChild != null) {
                queue.offer(new QueueNode(node.rightChild, depth + 1));
            }
        }

        return 0;
    }
    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.leftChild == null && root.rightChild == null) {
            return 1;
        }
        final int leftLen = minDepth(root.leftChild);
        final int rightLen = minDepth(root.rightChild);
        int len = 1;
        if (leftLen * rightLen > 0) {
            len += Math.min(rightLen, leftLen);
        } else if (leftLen > 0) {
            len += leftLen;
        } else {
            len += rightLen;
        }
        return len;
    }

    /**
     * 广度遍历到最后才知道那条路最长
     * @param root root
     * @return int
     */
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int len = 0;
        while (!queue.isEmpty()){
            int size = queue.size();
            while (size-- > 0){
                final TreeNode poll = queue.poll();
                if (poll.leftChild!= null){
                    queue.offer(poll.leftChild);
                }
                if (poll.rightChild != null){
                    queue.offer(poll.rightChild);
                }
            }
            len++;
        }
        return len;
    }
    public int maxDepth2(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.leftChild == null && root.rightChild == null) {
            return 1;
        }
        return 1 + Math.max(maxDepth(root.leftChild), maxDepth(root.rightChild));
    }
    /**
     *      3
     *    2        8
     * 9   10  nil    4
     */
    public static void main(String[] args) {
        final TreeNode binaryTree = TreeTraverse.createBinaryTree(new LinkedList<>(Arrays.asList(3, 2, 9, null, null, 10, null, null, 8, null, 4)));
        /*final TreeNode binaryTree = TreeTraverse.createBinaryTree(new LinkedList<>(Arrays.asList(1, 2)));*/
        /*System.out.println("前序遍历：  ");
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
        postOrderTravelWithStack2(binaryTree);

        System.out.println("层序遍历队列实现：  " + new TreeTraverse().levelOrder(binaryTree));
        System.out.println("层序遍历递归实现：  " + new TreeTraverse().levelOrder2(binaryTree));*/

        final TreeTraverse treeTraverse = new TreeTraverse();
        System.out.println(treeTraverse.maxDepth(binaryTree));
    }
}
