package linkedlist;

public class MyLinkedList {
    private int len = 0;
    private ListNode dummyHead = new ListNode(-1);
    private ListNode dummyTail = new ListNode(-1);

    public MyLinkedList() {
        dummyHead.next = dummyTail;
        dummyTail.pre = dummyHead;
    }

    public int get(int index) {
        ListNode current = dummyHead.next;
        while (index-- > 0 && current != dummyTail) {
            current = current.next;
        }
        if (current != dummyTail) {
            return current.val;
        }
        return -1;
    }

    public void addAtHead(int val) {
        ListNode next = dummyHead.next;
        dummyHead.next = new ListNode(val, next);
        dummyHead.next.pre = dummyHead;
        next.pre = dummyHead.next;
    }

    public void addAtTail(int val) {
        ListNode pre = dummyTail.pre;
        pre.next = new ListNode(val, dummyTail);
        pre.next.pre = pre;
        dummyTail.pre = pre.next;
    }

    public void addAtIndex(int index, int val) {
        ListNode current = dummyHead.next;
        while (index-- > 0 && current != null) {
            current = current.next;
        }
        if (current == null) {
            return;
        }
        ListNode pre = current.pre;
        pre.next = new ListNode(val, current);
        pre.next.pre = pre;
        current.pre = pre.next;
    }

    public void deleteAtIndex(int index) {
        if (index < 0) {
            return;
        }
        ListNode current = dummyHead.next;
        while (index-- > 0 && current != null) {
            current = current.next;
        }
        if (current == dummyTail || current == null) {
            return;
        }
        ListNode pre = current.pre;
        ListNode next = current.next;
        pre.next = next;
        next.pre = pre;
        current.next = null;
        current.pre = null;
    }

    /**
     * ["get","deleteAtIndex","deleteAtIndex"]
     * [[5],[6],[4]]
     *
     * @param args
     */
    public static void main(String[] args) {
        MyLinkedList linkedList = new MyLinkedList();
        linkedList.addAtHead(2);
        linkedList.deleteAtIndex(1);
        linkedList.addAtHead(2);
        linkedList.addAtHead(7);
        linkedList.addAtHead(3);
        linkedList.addAtHead(2);
        linkedList.addAtHead(5);
        linkedList.addAtTail(5);
        linkedList.get(5);            //返回2
        linkedList.deleteAtIndex(6);  //现在链表是1-> 3
        linkedList.deleteAtIndex(4);  //现在链表是1-> 3
    }
}
