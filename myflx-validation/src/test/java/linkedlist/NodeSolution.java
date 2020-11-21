package linkedlist;

import java.util.HashMap;
import java.util.Map;

public class NodeSolution {

    public Node copyRandomList11(Node head) {
        Node dummy = new Node(-1);
        Node last = dummy;
        Map<Node, Node> mapToNew = new HashMap<>();

        while (head != null) {
            last.next = new Node(head.val);
            last.next.random = head.random;
            mapToNew.put(head,last.next);
            last = last.next;
            head = head.next;
        }
        Node current = dummy.next;
        while (current != null){
            Node node = current.random;
            current.random = node == null ? null : mapToNew.get(node);
            current = current.next;
        }
        return dummy.next;
    }

    /**
     * 随机指针链表的深度复制
     */
    public Node copyRandomList(Node head) {
        final Node dummy = new Node(-1);
        Node last = dummy;
        Map<Node, Node> mapToNew = new HashMap<>();
        while (head != null) {
            last.next = new Node(head.val);
            last.next.random = head;
            mapToNew.put(head, last.next);
            last = last.next;
            head = head.next;
        }
        Node current = dummy.next;
        while (current != null) {
            final Node node = current.random;
            current.random = node.random == null ? null : mapToNew.get(node.random);
            current = current.next;
        }

        return dummy.next;
    }
}
