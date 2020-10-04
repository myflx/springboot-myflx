package linkedlist;

public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.deleteNode(solution.baseNode.next.next);
        System.out.println(solution.baseNode);
    }


    private final ListNode baseNode = buildLinkedList(new int[]{4, 5, 1, 9});

    public void deleteNode(ListNode node) {
        if (node == null) {
            return;
        }
        ListNode currentNext = node.next;
        if (currentNext == null) return;
        node.val = currentNext.val;
        node.next = currentNext.next;
    }

    public ListNode buildLinkedList(int[] nums) {
        ListNode root = null;
        ListNode current = null;
        for (int num : nums) {
            if (root == null) {
                root = new ListNode(num);
                current = root;
            } else {
                current.next = new ListNode(num);
                current = root.next;
            }
        }
        return root;
    }
}
