package linkedlist;

public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        /*solution.deleteNode(solution.baseNode.next.next);
        System.out.println(solution.baseNode);*/
        ListNode listNode = solution.reverseList(solution.baseNode);
        System.out.println(listNode);
    }


    public ListNode reverseList(ListNode head) {
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            return new ListNode(head.val);
        }
        return doReverseList(head, null);
    }

    public ListNode doReverseList(ListNode head, ListNode foot) {
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            //转换根节点
            ListNode root = new ListNode(head.val);
            root.next = foot;
            return root;
        }
        ListNode currentFoot = new ListNode(head.val);
        ListNode listNode = doReverseList(head.next, currentFoot);
        currentFoot.next = foot;
        return listNode;
    }


    public ListNode reverseList2(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode node = new ListNode(head.val);
        ListNode currentNode = head;
        while (currentNode.next != null) {
            ListNode listNode = new ListNode(currentNode.next.val);
            listNode.next = node;
            node = listNode;
            currentNode = currentNode.next;
        }
        return node;
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
                current = current.next;
            }
        }
        return root;
    }
}

