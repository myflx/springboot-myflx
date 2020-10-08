package stack;

import linkedlist.ListNode;

import java.util.ArrayList;
import java.util.List;

public class MinStack {
    ListNode minNode;
    List<ListNode> list = new ArrayList<>();

    public MinStack() {

    }

    /**
     * 容易存在节点关系维护差错
     *
     * @param x
     */
    public void push(int x) {
        ListNode node = new ListNode(x);
        if (list.size() == 0) {
            minNode = node;
        } else {
            if (minNode.val > x) {
                minNode.pre = node;
                node.next = minNode;
                minNode = node;
            } else {
                ListNode current = minNode;
                while (current.next != null && current.next.val < x) {
                    current = current.next;
                }
                ListNode cn = current.next;
                current.next = node;
                node.pre = current;
                node.next = cn;
                if (cn != null){
                    cn.pre = node;
                }
            }
        }
        list.add(node);
    }

    public void pop() {
        if (list.size() == 0) {
            return;
        }
        ListNode remove = list.remove(list.size() - 1);
        if (remove.pre == null) {
            minNode = remove.next;
            if (remove.next != null) {
                remove.next.pre = null;
            }
        } else {
            remove.pre.next = remove.next;
            if (remove.next != null) {
                remove.next.pre = remove.pre;
            }
        }
    }

    public int top() {
        if (list.size() == 0) {
            throw new IllegalArgumentException("no data");
        }
        return list.get(list.size() - 1).val;
    }

    public int getMin() {
        if (minNode == null) {
            throw new IllegalArgumentException("no data");
        }
        return minNode.val;
    }

    public static void main(String[] args) {
        MinStack minStack = new MinStack();
        minStack.push(395);
        minStack.push(276);
        minStack.push(29);
        minStack.push(-482);
        minStack.pop();

        minStack.push(-108);
        minStack.push(-251);
        minStack.push(-439);
        minStack.push(370);
        minStack.pop();
        minStack.pop();
        minStack.pop();

        minStack.getMin();   //--> 返回 -3.
    }
}
