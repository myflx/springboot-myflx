package linkedlist;

public class ListNode {
    public int val;
    public ListNode pre;
    public ListNode next;

    public ListNode(int x) {
        val = x;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    @Override
    public String toString() {
        return val + "-" + next;
    }
}
