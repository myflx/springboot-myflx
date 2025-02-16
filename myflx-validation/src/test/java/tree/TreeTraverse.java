package tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;

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
            treeNode.left = createBinaryTree(inputList);
            treeNode.right = createBinaryTree(inputList);
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
        System.out.print(treeNode.val);
        preOrderTravel(treeNode.left);
        preOrderTravel(treeNode.right);
    }

    /**
     * 中序遍历
     */
    public static void midOrderTravel(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        midOrderTravel(treeNode.left);
        System.out.print(treeNode.val);
        midOrderTravel(treeNode.right);
    }

    public static void midOrderTravel(TreeNode treeNode, List<Integer> list) {
        if (treeNode == null) {
            return;
        }
        midOrderTravel(treeNode.left, list);
        list.add(treeNode.val);
        midOrderTravel(treeNode.right, list);
    }

    /**
     * 后序遍历
     */
    public static void postOrderTravel(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        postOrderTravel(treeNode.left);
        postOrderTravel(treeNode.right);
        System.out.print(treeNode.val);
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
            System.out.println(pop.val);
            if (pop.right != null) {
                stack.push(pop.right);
            }
            if (pop.left != null) {
                stack.push(pop.left);
            }
        }
    }

    /**
     * 3
     * 2        8
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
            TreeNode r = stack.peek().left;
            while (r != null) {
                stack.push(r);
                r = r.left;
            }
            //所有左孩子入栈完毕，一直pop
            while (!stack.isEmpty()) {
                TreeNode pop = stack.pop();
                System.out.println(pop.val);
                if (pop.right != null) {
                    //如果某个节点存在右孩子那么结束弹栈，从头开始进入下个循环
                    stack.push(pop.right);
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
            TreeNode r = stack.peek().left;
            while (r != null) {
                stack.push(r);
                r = r.left;
            }
            //所有左孩子入栈完毕，一直pop
            while (!stack.isEmpty()) {
                TreeNode pop = stack.pop();
                System.out.println(pop.val);
                if (pop.right != null) {
                    //如果某个节点存在右孩子那么结束弹栈，从头开始进入下个循环
                    stack.push(pop.right);
                    break;
                }
            }
        }
    }

    /**
     * 3
     * 2        8
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
            if (peek.left != null) {
                stack.push(peek.left);
            } else {
                while (!stack.isEmpty()) {
                    TreeNode pop = stack.peek();
                    if (!traced.contains(pop) && pop.right != null) {
                        traced.add(pop);
                        stack.push(pop.right);
                        break;
                    } else {
                        stack.pop();
                        System.out.println(pop.val);
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
        while (!stack.isEmpty()) {
            TreeNode peek = stack.peek();
            if (peek == null) {
                stack.pop();
                final TreeNode pop = stack.pop();
                System.out.println(pop.val);
                continue;
            }
            stack.push(null);
            if (peek.right != null) {
                stack.push(peek.right);
            }

            if (peek.left != null) {
                stack.push(peek.left);
            }
        }
    }


    /**
     * 3
     * 2        8
     * 9   10  nil    4
     * 层序遍历(队列实现)
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ret = new ArrayList<>();
        if (Objects.isNull(root)) {
            return ret;
        }
        TreeNode split = new TreeNode(0);
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        queue.offer(split);
        while (!queue.isEmpty()) {
            List<Integer> out = new ArrayList<>();
            TreeNode poll = queue.poll();
            while (poll != null && split != poll) {
                out.add(poll.val);
                if (poll.left != null) {
                    queue.offer(poll.left);
                }
                if (poll.right != null) {
                    queue.offer(poll.right);
                }
                poll = queue.poll();
            }
            if (!queue.isEmpty()) {
                queue.offer(split);
            }
            ret.add(out);
        }
        return ret;
    }


    /**
     * 3
     * 2        8
     * 9   10  nil    4
     * 层序遍历(递归实现)
     */
    public List<List<Integer>> levelOrder2(TreeNode root) {
        if (Objects.isNull(root)) {
            return null;
        }
        List<List<Integer>> ret = new ArrayList<>();
        doLevelOrder(root, ret, 0);
        return ret;
    }

    public void doLevelOrder(TreeNode node, List<List<Integer>> ret, int level) {
        if (Objects.isNull(node)) {
            return;
        }
        if (ret.size() <= level) {
            ret.add(new ArrayList<>());
        }
        ret.get(level).add(node.val);
        doLevelOrder(node.left, ret, level + 1);
        doLevelOrder(node.right, ret, level + 1);
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
            if (node.left == null && node.right == null) {
                return depth;
            }
            if (node.left != null) {
                queue.offer(new QueueNode(node.left, depth + 1));
            }
            if (node.right != null) {
                queue.offer(new QueueNode(node.right, depth + 1));
            }
        }

        return 0;
    }

    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) {
            return 1;
        }
        final int leftLen = minDepth(root.left);
        final int rightLen = minDepth(root.right);
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
     *
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
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size-- > 0) {
                final TreeNode poll = queue.poll();
                if (poll.left != null) {
                    queue.offer(poll.left);
                }
                if (poll.right != null) {
                    queue.offer(poll.right);
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
        if (root.left == null && root.right == null) {
            return 1;
        }
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }


    /**
     * 转为单链表
     *
     * @param root root
     */
    public void flatten2(TreeNode root) {
        if (root == null) {
            return;
        }
        List<TreeNode> list = new ArrayList<>();
        doPreOrderTraversal(list, root);
        for (int i = 0; i < list.size(); i++) {
            final TreeNode node = list.get(i);
            node.left = null;
            node.right = i == list.size() - 1 ? null : list.get(i + 1);
        }
    }

    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }

        TreeNode current = root;
        while (current != null) {
            TreeNode left = current.left;
            if (left != null) {
                TreeNode pre = left;
                while (pre.right != null) {
                    pre = pre.right;
                }
                pre.right = current.right;
                current.left = null;
                current.right = left;
            }
            current = current.right;
        }
    }

    private void doPreOrderTraversal(List<TreeNode> list, TreeNode root) {
        if (root == null) {
            return;
        }
        list.add(root);
        doPreOrderTraversal(list, root.left);
        doPreOrderTraversal(list, root.right);
    }

    public void preorderTraversal(TreeNode root, List<TreeNode> list) {
        if (root != null) {
            list.add(root);
            preorderTraversal(root.left, list);
            preorderTraversal(root.right, list);
        }
    }

    public boolean isBalanced(TreeNode root) {
        if (root == null) {
            return true;
        }
        length(root, root);
        return root.balanced && root.notBalanceCount == 0;
    }

    public int length(TreeNode current, TreeNode root) {
        if (current == null) {
            return 0;
        }
        final int ll = length(current.left, root);
        final int rl = length(current.right, root);
        current.balanced = Math.abs(ll - rl) <= 1;
        root.notBalanceCount += current.balanced ? 0 : 1;
        return Math.max(ll, rl) + 1;
    }


    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        if (p.val != q.val) {
            return false;
        }
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    /**
     * 3
     * / \
     * 9  20
     * /  \
     * 15   7
     *
     * @param root root
     * @return list
     */
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        if (Objects.isNull(root)) {
            return null;
        }
        List<List<Integer>> ret = new ArrayList<>();
        doLevelOrder(root, ret, 0);
        Collections.reverse(ret);
        return ret;
    }


    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        if (root.left == null && root.right == null) {
            return true;
        }
        if (root.left == null || root.right == null) {
            return false;
        }

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root.left);
        stack.push(root.right);
        while (!stack.isEmpty()) {
            final TreeNode rNode = stack.pop();
            final TreeNode lNode = stack.pop();
            if (rNode == null && lNode == null) {
                continue;
            }
            if (rNode == null || lNode == null) {
                return false;
            }

            if (rNode.val != lNode.val) {
                return false;
            }
            stack.push(lNode.left);
            stack.push(rNode.right);

            stack.push(rNode.left);
            stack.push(lNode.right);
        }
        return true;
    }

    public TreeNode sortedArrayToBST(int[] nums) {
        return null;
    }


    public boolean isValidBST2(TreeNode root) {
        if (root == null || (root.left == null && root.right == null)) {
            return true;
        }
        if (root.left != null && root.val <= root.left.val) {
            return false;
        }
        if (root.right != null && root.val >= root.right.val) {
            return false;
        }

        List<Integer> orderList = new ArrayList<>();
        midOrderTravel(root, orderList);
        for (int i = 0; i < orderList.size(); i++) {
            if (i > 0 && orderList.get(i - 1) >= orderList.get(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidBST(TreeNode root) {
        return helper(root, null, null);
    }

    public boolean helper(TreeNode root, Integer s, Integer e) {
        if (root == null) {
            return true;
        }
        if (s != null && root.val <= s) {
            return false;
        }
        if (e != null && root.val >= e) {
            return false;
        }
        if (!helper(root.left, s, root.val)) {
            return false;
        }
        if (!helper(root.right, root.val, e)) {
            return false;
        }
        return true;
    }


    public TreeNode buildTree2(int[] preorder, int[] inorder) {
        if (preorder == null || inorder == null || preorder.length == 0 || inorder.length == 0) {
            return null;
        }
        if (preorder.length != inorder.length) {
            return null;
        }
        if (preorder.length == 1) {
            return new TreeNode(preorder[0]);
        }
        int val = preorder[0];
        TreeNode root = new TreeNode(val);
        int s = 0;
        for (int i = 0; i < inorder.length; i++) {
            if (val == inorder[i]) {
                s = i;
                break;
            }
        }
        int[] lp = s > 0 ? new int[s] : null;
        int[] rp = inorder.length - 1 - s > 0 ? new int[inorder.length - 1 - s] : null;
        int[] li = s > 0 ? new int[s] : null;
        int[] ri = inorder.length - 1 - s > 0 ? new int[inorder.length - 1 - s] : null;

        if (lp != null) {
            System.arraycopy(preorder, 1, lp, 0, lp.length);
        }
        if (rp != null) {
            System.arraycopy(preorder, s + 1, rp, 0, rp.length);
        }
        if (li != null) {
            System.arraycopy(inorder, 0, li, 0, li.length);
        }
        if (ri != null) {
            System.arraycopy(inorder, s + 1, ri, 0, ri.length);
        }
        root.left = buildTree(lp, li);
        root.right = buildTree(rp, ri);
        return root;
    }


    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder == null || inorder == null || preorder.length == 0 || inorder.length == 0) {
            return null;
        }
        if (preorder.length != inorder.length) {
            return null;
        }
        if (preorder.length == 1) {
            return new TreeNode(preorder[0]);
        }
        return buildTreeHelper(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1);
    }

    public TreeNode buildTreeHelper(int[] preorder, int ps, int pe, int[] inorder, int is, int ie) {
        if (ps > pe || is > ie || pe == preorder.length || ie == inorder.length) {
            return null;
        }
        //偏移量
        int delta = ps - is;
        int val = preorder[ps];
        TreeNode treeNode = new TreeNode(val);
        if (pe == ps) {
            return treeNode;
        }
        int midIndex = 0;
        for (int i = is; i < inorder.length; i++) {
            if (val == inorder[i]) {
                midIndex = i;
                break;
            }
        }
        treeNode.left = buildTreeHelper(preorder, ps + 1, midIndex + delta, inorder, is, midIndex - 1);
        treeNode.right = buildTreeHelper(preorder, midIndex + delta + 1, pe, inorder, midIndex + 1, ie);
        return treeNode;
    }

    public void recoverTree3(TreeNode root) {
        if (root == null) {
            return;
        }
        TreeNode x = null;
        TreeNode y = null;
        TreeNode pred = null;
        Stack<TreeNode> stack = new Stack<>();
        while (!stack.isEmpty() || root != null) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            if (pred != null && pred.val > root.val) {
                y = root;
                if (x == null) {
                    x = pred;
                } else {
                    break;
                }
            }
            pred = root;
            root = root.right;
        }
        if (x != null && y != null) {
            int tmp = x.val;
            x.val = y.val;
            y.val = tmp;
        }
    }

    public void recoverTree2(TreeNode root) {
        List<TreeNode> orderStructure = new ArrayList<>();
        TreeMap<Integer, Integer> mapping = new TreeMap<>();
        midOrderTravel(root, orderStructure, mapping);
        Iterator<Map.Entry<Integer, Integer>> iterator = mapping.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> next = iterator.next();
            if (next.getKey() != orderStructure.get(i).val) {
                int tmp = orderStructure.get(i).val;
                orderStructure.get(i).val = next.getKey();
                orderStructure.get(next.getValue() - 1).val = tmp;
                return;
            }
            i++;
        }
    }

    public static void midOrderTravel(TreeNode treeNode, List<TreeNode> list, TreeMap<Integer, Integer> mapping) {
        if (treeNode == null) {
            return;
        }
        midOrderTravel(treeNode.left, list, mapping);
        list.add(treeNode);
        mapping.put(treeNode.val, mapping.size() + 1);
        midOrderTravel(treeNode.right, list, mapping);
    }

    public void swap(TreeNode x, TreeNode y) {
        int tmp = x.val;
        x.val = y.val;
        y.val = tmp;
    }

    public void recoverTree(TreeNode root) {
        TreeNode x = null;
        TreeNode y = null;
        TreeNode pred = null;
        TreeNode predecessor = null;
        while (root != null) {
            if (root.left != null) {
                //分支①
                //寻找前驱节点
                predecessor = root.left;
                while (predecessor.right != null && predecessor.right != root) {
                    predecessor = predecessor.right;
                }
                if (predecessor.right == null) {
                    //分支②
                    //前驱节点关联到当前根节点
                    predecessor.right = root;
                    root = root.left;
                } else {
                    if (pred != null && root.val < pred.val) {
                        y = root;
                        if (x == null) {
                            x = pred;
                        }
                    }
                    pred = root;
                    predecessor.right = null;
                    root = root.right;
                }
            } else {
                //分支③
                //找到左边的叶子节点（左孩子为空，右孩子关联了父节点）
                if (pred != null && pred.val > root.val) {
                    y = root;
                    if (x == null) {
                        x = pred;
                    } else {
                        //不能break掉，否则存在的循环依赖就没法剔除
                    }
                }
                pred = root;
                //导致后续循环再次回到父节点的父节点进入分支①④
                root = root.right;

            }
        }
        if (x != null && y != null) {
            int tmp = x.val;
            x.val = y.val;
            y.val = tmp;
        }
    }


    /**
     * 锯齿形层次遍历
     *
     * @param root
     * @return
     */
    public List<List<Integer>> zigzagLevelOrder2(TreeNode root) {
        List<List<Integer>> ret = new ArrayList<>();
        if (root == null) {
            return ret;
        }
        ret.add(new ArrayList<>());
        ret.get(0).add(root.val);
        //order大于0代表正序小于0代表反序
        int order = -1;
        Queue<Stack<TreeNode>> queue = new ArrayDeque<>();
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        queue.offer(stack);
        List<Integer> levelList = new ArrayList<>();
        while (!queue.isEmpty()) {
            Stack<TreeNode> nextStack = new Stack<>();
            Stack<TreeNode> poll = queue.poll();
            while (!poll.isEmpty()) {
                TreeNode pop = poll.pop();
                if (order < 0) {
                    if (pop.right != null) {
                        nextStack.push(pop.right);
                        levelList.add(pop.right.val);
                    }
                    if (pop.left != null) {
                        nextStack.push(pop.left);
                        levelList.add(pop.left.val);
                    }
                } else {
                    if (pop.left != null) {
                        nextStack.push(pop.left);
                        levelList.add(pop.left.val);
                    }
                    if (pop.right != null) {
                        levelList.add(pop.right.val);
                        nextStack.push(pop.right);
                    }
                }
            }
            if (!levelList.isEmpty()) {
                ret.add(new ArrayList<>(levelList));
                levelList.clear();
            }
            if (!nextStack.isEmpty()) {
                queue.offer(nextStack);
            }
            order = -order;
        }
        return ret;
    }

    /**
     * 锯齿形层次遍历
     * 层次遍历+按条件反转
     *
     * @param root
     * @return
     */
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        if (root == null) {
            return new ArrayList<>();
        }
        List<List<Integer>> ret = levelOrder2(root);
        for (int i = 0; i < ret.size(); i++) {
            List<Integer> list = ret.get(i);
            if (i % 2 != 0) {
                Collections.reverse(list);
            }
        }
        return ret;
    }

    /**
     * 3
     * 2        8
     * 9   10  nil    4
     * [1,2,5,3,4,null,6]
     */
    public static void main(String[] args) {
        /*final TreeNode binaryTree = TreeTraverse.createBinaryTree(new LinkedList<>(Arrays.asList(3, 2, 9, null, null, 10, null, null, 8, null, 4)));*/
        /*final TreeNode binaryTree = TreeTraverse.createBinaryTree(new LinkedList<>(Arrays.asList(1, 2, 3, null, null, 4, null, null, 5, null, 6)));*/
        final TreeNode binaryTree = TreeTraverse.createBinaryTree(new LinkedList<>(Arrays.asList(3, 1, null, null, 4, 2)));
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
        /*System.out.println(treeTraverse.maxDepth(binaryTree));
        treeTraverse.flatten(binaryTree);*/
        /*treeTraverse.isSymmetric(binaryTree);*/
        /*final TreeNode node = treeTraverse.buildTree(new int[]{3, 9, 2, 8, 20, 15, 7}, new int[]{2, 9, 8, 3, 15, 20, 7});*/
        treeTraverse.recoverTree(binaryTree);
    }
}
