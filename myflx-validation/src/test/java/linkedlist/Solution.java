package linkedlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Solution {
    private final ListNode l1 = buildLinkedList(new int[]{1, 2, 3});
    private final ListNode l2 = buildLinkedList(new int[]{1, 3, 4, 6, 9, 12});
    private final ListNode l3 = buildLinkedList(new int[]{1, 2, 3, 4});
    private final ListNode l4 = buildLinkedList(new int[]{2, 1, 3, 7, 4});

    public static void main(String[] args) {
        System.out.println(10000 ^ 100001);
        Solution solution = new Solution();
        /*solution.deleteNode(solution.baseNode.next.next);
        System.out.println(solution.baseNode);
        ListNode listNode = solution.reverseList(solution.baseNode);
        System.out.println(listNode);*/

        /*ListNode listNode = solution.mergeTwoLists(solution.l1, solution.l2);
        System.out.println(listNode);*/

        /*System.out.println(solution.sortList(solution.l4));*/
        System.out.println(solution.rotateRight(solution.l1, 2000000000));
    }


    public ListNode rotateRight(ListNode head, int k) {
        if (k == 0 || head == null || head.next == null) {
            return head;
        }
        int len = 1;
        ListNode p = head;
        while (p.next != null) {
            p = p.next;
            len++;
        }
        p.next = head;
        //计算迭代长度
        len = (k / len + 1) * len - k;
        while (len-- > 0) {
            p = p.next;
        }
        ListNode q = p.next;
        p.next = null;
        return q;
    }

    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode fNode = head.next;
        ListNode sNode = head;
        while (fNode != sNode) {
            if (fNode == null || fNode.next == null) {
                return false;
            }
            sNode = sNode.next;
            fNode = fNode.next.next;
        }
        return true;
    }

    /**
     * 思路不要固化
     * 不一定发现起点才证明有环，环上的任何一个节点都重复都能证明
     */
    public boolean hasCycle2(ListNode head) {
        HashMap<ListNode, Integer> map = new HashMap<>();
        int index = 0;
        while (head != null) {
            if (map.containsKey(head)) {
                return true;
            }
            map.put(head, index++);
            head = head.next;
        }
        return false;
    }


    public ListNode detectCycle(ListNode head) {
        HashSet<ListNode> set = new HashSet<>();
        while (head != null) {
            if (!set.add(head)) {
                return head;
            }
            head = head.next;
        }
        return null;
    }

    /**
     * 寻找相交节点
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == headB) {
            return headA;
        }
        if (headA == null || headB == null) {
            return null;
        }
        ListNode a = headA;
        ListNode b = headB;
        while (a != b) {
            a = a == null ? headA : a.next;
            b = b == null ? headB : b.next;
        }
        return a;
    }

    /**
     * 寻找相交节点
     */
    public ListNode getIntersectionNode2(ListNode headA, ListNode headB) {
        if (headA == headB) {
            return headA;
        }
        HashSet<ListNode> set = new HashSet<>();
        while (headA != null) {
            set.add(headA);
            headA = headA.next;
        }
        while (headB != null) {
            if (set.contains(headB)) {
                return headB;
            }
            headB = headB.next;
        }
        return null;
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


    /**
     * 在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序。
     * https://leetcode-cn.com/problems/sort-list/
     */
    public ListNode sortList2(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode current = new ListNode(head.val);
        ListNode last = current;
        ListNode tmp = head.next;
        while (tmp != null) {
            if (tmp.val >= last.val) {
                last.next = new ListNode(tmp.val);
                last = last.next;
            } else if (tmp.val < current.val) {
                ListNode listNode = new ListNode(tmp.val);
                listNode.next = current;
                current = listNode;
            } else {
                ListNode listNode = current;
                while (listNode.next.val < tmp.val) {
                    listNode = listNode.next;
                }
                //listNode.next.val >= tmp
                ListNode next = listNode.next;
                listNode.next = new ListNode(tmp.val);
                listNode.next.next = next;

            }
            tmp = tmp.next;
        }
        return current;
    }


    public static ListNode sortList3(ListNode head) {//2-1-3-7-4
        ListNode dummyHead = new ListNode(Integer.MIN_VALUE);//d-2-1-3-7-4
        dummyHead.next = head;//
        // 先统计长度
        int len = 0;
        ListNode p = head;
        while (p != null) {
            len++;
            p = p.next;
        }
        // 循环开始切割和合并
        for (int i = 1; i < len; i <<= 1) {
            ListNode cur = dummyHead.next;
            ListNode tail = dummyHead;
            while (cur != null) {
                ListNode left = cur;
                ListNode right = cut(left, i);
                cur = cut(right, i);
                tail.next = merge(left, right);
                while (tail.next != null) {
                    tail = tail.next;
                }
            }
        }
        return dummyHead.next;
    }


    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        //因为快节点比慢节点快一倍所以此时循环结束慢为链表的中间位置
        ListNode right = slow.next;
        slow.next = null;
        return merge(sortList(head), sortList(right));
    }

    /**
     * 借助数据排序最快
     */
    public static ListNode sortList4(ListNode head) {
        List<Integer> list = new ArrayList<>();
        ListNode p = head, q = head;
        while (p != null) {
            list.add(p.val);
            p = p.next;
        }
        Collections.sort(list);
        int i = 0;
        while (q != null) {
            q.val = list.get(i++);
            q = q.next;
        }
        return head;
    }


    /**
     * --n获取前一个
     * n--获取后一个
     * 切割前n个元素，返回后半部分
     */
    public static ListNode cut(ListNode head, int n) {
        if (n <= 0) return head;
        ListNode p = head;
        while (--n > 0 && p != null) {
            p = p.next;
        }
        if (p == null) return null;
        ListNode next = p.next;
        p.next = null;
        return next;
    }


    public static ListNode merge(ListNode listNode1, ListNode listNode2) {
        ListNode dummyHead = new ListNode(Integer.MIN_VALUE);
        ListNode p = dummyHead;
        while (listNode1 != null && listNode2 != null) {
            if (listNode1.val < listNode2.val) {
                p.next = listNode1;
                listNode1 = listNode1.next;
            } else {
                p.next = listNode2;
                listNode2 = listNode2.next;
            }
            p = p.next;
        }
        p.next = listNode1 == null ? listNode2 : listNode1;
        return dummyHead.next;
    }
}

