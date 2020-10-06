package linkedlist;

public class Solution {
    private final ListNode l1 = buildLinkedList(new int[]{1, 2, 4, 5, 7, 8});
    private final ListNode l2 = buildLinkedList(new int[]{1, 3, 4, 6, 9, 12});

    public static void main(String[] args) {
        Solution solution = new Solution();
        /*solution.deleteNode(solution.baseNode.next.next);
        System.out.println(solution.baseNode);
        ListNode listNode = solution.reverseList(solution.baseNode);
        System.out.println(listNode);*/

        ListNode listNode = solution.mergeTwoLists(solution.l1, solution.l2);
        System.out.println(listNode);
    }

    /**
     * 列表合并优化版
     */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode prehead = new ListNode(-1);
        ListNode prev = prehead;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                prev.next = l1;
                l1 = l1.next;
            } else {
                prev.next = l2;
                l2 = l2.next;
            }
            prev = prev.next;
        }
        // 合并后 l1 和 l2 最多只有一个还未被合并完，我们直接将链表末尾指向未合并完的链表即可
        prev.next = l1 == null ? l2 : l1;

        return prehead.next;
    }

    /**
     * 合并链表-同时迭代
     */
    public ListNode mergeTwoLists4(ListNode l1, ListNode l2) {
        //不知道哪个节点打头又不想增加多余判断，使用冗余节点是个好思路
        ListNode ns = new ListNode(-1);
        ListNode nsl = ns;//始终指向最后一个节点
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                nsl.next = new ListNode(l1.val);
                l1 = l1.next;
            } else {
                nsl.next = new ListNode(l2.val);
                l2 = l2.next;
            }
            nsl = nsl.next;
        }
        //统一处理迭代中非空逻辑排除出来的节点
        nsl.next = l1 == null ? l2 : l1;
        return ns.next;
    }

    /**
     * 合并链表-同时迭代
     */
    public ListNode mergeTwoLists3(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        ListNode b = l1;
        ListNode s = l2;
        if (l1.val < l2.val) {
            b = l2;
            s = l1;
        }
        ListNode ns = new ListNode(s.val);
        s = s.next;
        ListNode nsl = ns;//始终指向最后一个节点
        while (b != null || s != null) {
            if (b == null) {
                nsl.next = s;
                break;
            }
            if (s == null) {
                nsl.next = b;
                break;
            }
            if (b.val < s.val) {
                nsl.next = new ListNode(b.val);
                b = b.next;
            } else {
                nsl.next = new ListNode(s.val);
                s = s.next;
            }
            nsl = nsl.next;
        }
        return ns;
    }

    /**
     * 合并链表
     */
    public ListNode mergeTwoLists2(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        ListNode b = l1;
        ListNode s = l2;
        if (l1.val < l2.val) {
            b = l2;
            s = l1;
        }
        ListNode ns = new ListNode(s.val);
        ListNode nsl = ns;
        while (s.next != null && s.next.val < b.val) {
            nsl.next = new ListNode(s.next.val);
            s = s.next;
            nsl = nsl.next;
        }
        ListNode sNext = s.next;
        ListNode bNext = b.next;
        nsl.next = new ListNode(b.val, mergeTwoLists(sNext, bNext));
        return ns;
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

