package linkedlist;

import tree.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class Solution {
    private final ListNode baseNode = buildLinkedList(new int[]{1, 0, 1});
    private final ListNode l1 = buildLinkedList(new int[]{1, 2, 3, 4});
    private final ListNode l2 = buildLinkedList(new int[]{5, 6, 4});
    private final ListNode l3 = buildLinkedList(new int[]{1, 2, 3, 4});
    private final ListNode l4 = buildLinkedList(new int[]{2, 1, 3, 7, 4});


    public static void main(String[] args) {
        System.out.println(10000 ^ 100001);
        Solution solution = new Solution();
        /*solution.deleteNode(solution.baseNode.next.next);
        System.out.println(solution.baseNode);*/
        /*System.out.println(solution.reverseBetween(solution.l1, 1, 5));*/

        /*ListNode listNode = solution.mergeTwoLists(solution.l1, solution.l2);
        System.out.println(listNode);*/

        /*System.out.println(solution.sortList(solution.l4));*/
        /*System.out.println(solution.rotateRight(solution.l1, 2000000000));*/
        System.out.println(Arrays.toString(solution.splitListToParts(solution.l1, 2)));
    }

    public ListNode detectCycle666(ListNode head) {
        if (head == null) return null;
        ListNode f = head, s = head;
        while (f.next != null && f.next.next != null) {
            s = s.next;
            f = f.next.next;
            if (s == f) {
                break;
            }
        }
        if (f.next == null || f.next.next == null) {
            return null;
        }
        f = head;
        while (s != f) {
            s = s.next;
            f = f.next;
        }
        return s;
    }

    public ListNode addTwoNumbersPlus(ListNode l1, ListNode l2) {
        Stack<ListNode> stack1 = new Stack<>();
        Stack<ListNode> stack2 = new Stack<>();
        while (l1 != null || l2 != null) {
            if (l1 != null) {
                stack1.push(l1);
                l1 = l1.next;
            }
            if (l2 != null) {
                stack2.push(l2);
                l2 = l2.next;
            }
        }
        ListNode pre = null;
        int preNumber = 0;
        while (stack1.size() > 0 || stack2.size() > 0) {
            int sum = preNumber;
            if (stack1.size() > 0) {
                sum += stack1.pop().val;
            }
            if (stack2.size() > 0) {
                sum += stack2.pop().val;
            }
            preNumber = sum / 10;
            pre = new ListNode(sum % 10, pre);
        }
        if (preNumber > 0) {
            pre = new ListNode(preNumber, pre);
        }
        return pre;
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1);
        ListNode dummyL = dummy;
        int preNum = 0;
        while (l1 != null || l2 != null) {
            int sum = preNum;
            if (l1 != null) {
                sum += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                sum += l2.val;
                l2 = l2.next;
            }
            dummyL.next = new ListNode(sum % 10);
            dummyL = dummyL.next;
            preNum = sum / 10;
        }
        if (preNum > 0) {
            dummyL.next = new ListNode(preNum);
        }
        return dummy.next;
    }

    public ListNode oddEvenList(ListNode head) {
        ListNode ldummy = new ListNode(-1);
        ListNode llast = ldummy;
        ListNode rdummy = new ListNode(-1);
        ListNode rlast = rdummy;
        int count = 1;
        while (head != null) {
            ListNode next = head.next;
            head.next = null;
            if ((count++ & 1) == 1) {
                llast.next = head;
                llast = head;
            } else {
                rlast.next = head;
                rlast = head;
            }
            head = next;
        }
        llast.next = rdummy.next;
        return ldummy.next;
    }

    public ListNode insertionSortList(ListNode head) {
        ListNode pn = new ListNode(-1);
        ListNode h = head;
        while (h != null) {
            ListNode next = h.next;
            h.next = null;
            //插入head中进行排序
            ListNode p = pn.next;
            ListNode pre = pn;
            while (p != null) {
                if (h.val > p.val) {
                    ListNode preN = pre.next;
                    pre.next = h;
                    h.next = preN;
                    break;
                }
                pre = p;
                p = p.next;
            }
            if (p == null) {
                pre.next = h;
            }

            h = next;
        }
        ListNode pre = null;
        h = pn.next;
        while (h != null) {
            ListNode next = h.next;
            h.next = pre;
            pre = h;
            h = next;
        }
        return pre;
    }

    public void reorderList(ListNode head) {
        if (head == null) return;
        ListNode s = head;
        ListNode f = head;
        //找到中心点并断开连接
        while (f.next != null && f.next.next != null) {
            s = s.next;
            f = f.next.next;
        }
        //翻转
        ListNode foot = null;
        ListNode right = s.next;
        s.next = null;
        while (right != null) {
            ListNode next = right.next;
            right.next = foot;
            foot = right;
            right = next;
        }
        //合并
        ListNode last = new ListNode(-1);
        while (head != null && foot != null) {
            ListNode lnext = head.next;
            ListNode rnext = foot.next;
            //关联
            head.next = foot;
            foot.next = null;
            last.next = head;
            //next
            last = foot;
            head = lnext;
            foot = rnext;
        }
        last.next = head;
    }

    public void reorderList2(ListNode head) {
        Deque<ListNode> stack = new ArrayDeque<>();
        ListNode h = head;
        while (h != null) {
            ListNode next = h.next;
            h.next = null;
            stack.offer(h);
            h = next;
        }

        ListNode lastNode = new ListNode(-1);
        while (stack.size() > 0) {
            ListNode listNode = stack.pollFirst();
            listNode.next = stack.pollLast();
            lastNode.next = listNode;
            lastNode = lastNode.next.next;
        }
    }

    /**
     * 有序链表转平衡BST
     */
    public TreeNode sortedListToBST(ListNode head) {
        if (head == null) return null;
        if (head.next == null) return new TreeNode(head.val);
        if (head.next.next == null) return new TreeNode(head.next.val, new TreeNode(head.val), null);
        ListNode s = new ListNode(-1, head);
        ListNode f = head;
        while (f.next != null && f.next.next != null) {
            s = s.next;
            f = f.next.next;
        }
        ListNode mid = s.next;
        s.next = null;
        return new TreeNode(mid.val, sortedListToBST(head), sortedListToBST(mid.next));
    }

    public ListNode reverseBetween(ListNode head, int m, int n) {
        int diff = n - m + 1;
        ListNode dummy = new ListNode(-1, head);
        ListNode h = dummy;
        while (h != null && m-- > 1) {
            h = h.next;
        }
        ListNode hl = h;
        h = h.next;
        ListNode pre = null;
        ListNode preLast = null;
        while (h != null && diff-- > 0) {
            ListNode next = h.next;
            h.next = null;
            if (pre == null) {
                pre = h;
                preLast = h;
            } else {
                h.next = pre;
                pre = h;
            }
            h = next;
        }
        if (hl != null) {
            hl.next = pre;
        }
        if (preLast != null) {
            preLast.next = h;
        }
        return dummy.next;
    }

    public ListNode partition(ListNode head, int x) {
        ListNode dummy = new ListNode(-1);
        ListNode dummyLast = dummy;
        ListNode right = null;
        ListNode rightLast = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = null;
            if (head.val < x) {
                dummyLast.next = head;
                dummyLast = dummyLast.next;
            } else {
                if (rightLast != null) {
                    rightLast.next = head;
                    rightLast = rightLast.next;
                } else {
                    right = head;
                    rightLast = head;
                }
            }
            head = next;
        }
        dummyLast.next = right;
        return dummy.next;
    }

    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(-1);
        ListNode l = dummy;

        int count = 0;
        ListNode current = null;
        ListNode cl = null;
        ListNode h = head;
        while (h != null) {
            ListNode next = h.next;
            h.next = current;
            current = h;
            if (cl == null) {
                cl = h;
            }
            if (++count == k) {
                l.next = current;
                l = cl;
                current = null;
                cl = null;
                count = 0;
            }
            h = next;
        }
        ListNode current1 = null;
        while (current != null) {
            ListNode next = current.next;
            current.next = current1;
            current1 = current;
            current = next;
        }
        l.next = current1;
        return dummy.next;
    }

    public ListNode deleteDuplicates2(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode dummy = new ListNode(-1, head);
        ListNode before = dummy;
        ListNode pre = head;
        head = head.next;
        while (head != null) {
            if (head.val == pre.val) {
                before.next = head.next;
                if (head.next != null && head.next.val != pre.val) pre = before;
            } else {
                before = pre;
                pre = head;
            }
            head = head.next;
        }
        return dummy.next;
    }

    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(-1, head);
        int count = 0;
        ListNode h = head;
        ListNode pre = h;
        ListNode ppre = dummy;
        while (h != null) {
            count++;
            h = h.next;
            if (count == 2) {
                ppre.next = swap(pre);
                ppre = ppre.next.next;
                pre = h;
                count = 0;
            }
        }
        return dummy.next;
    }

    private ListNode swap(ListNode current) {
        ListNode dummy = new ListNode(-1, current.next);
        current.next = dummy.next.next;
        dummy.next.next = current;
        return dummy.next;
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null || head.next == null) return null;
        ListNode h = head;
        int count = 0;
        while (h != null) {
            count++;
            h = h.next;
        }
        ListNode dummy = new ListNode(-1, head);
        h = dummy.next;
        ListNode pre = dummy;
        count = count - n;
        while (count-- > 0) {
            pre = h;
            h = h.next;
        }
        pre.next = h.next;
        return dummy.next;
    }

    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) return true;
        ListNode s = head;
        ListNode f = head;
        //找中心
        while (f.next != null && f.next.next != null) {
            s = s.next;
            f = f.next.next;
        }
        //中间链表下移，断开连接
        ListNode snxt = s.next;
        s.next = null;
        s = snxt;
        //翻转s
        ListNode pre = null;
        ListNode p = s;
        while (p != null) {
            ListNode next = p.next;
            p.next = pre;
            pre = p;
            p = next;
        }

        //同向比较
        while (pre != null && head != null) {
            if (pre.val != head.val) {
                return false;
            }
            pre = pre.next;
            head = head.next;
        }
        return true;
    }

    public boolean isPalindrome3(ListNode head) {
        if (head == null || head.next == null) return true;
        final Deque<ListNode> que = new ArrayDeque<>();
        que.push(head);
        ListNode listNode = que.peekLast();
        while (listNode != null) {
            listNode = listNode.next;
            if (listNode != null) {
                que.addLast(listNode);
            }
        }
        while (que.size() >= 2) {
            final ListNode listNode1 = que.pollFirst();
            final ListNode listNode2 = que.pollLast();
            if (listNode1.val != listNode2.val) {
                return false;
            }
        }
        return true;
    }

    public boolean isPalindrome2(ListNode head) {
        if (head == null || head.next == null) return true;
        ListNode f = head;
        ListNode pre = head;
        while (f.next != null) {
            pre = f;
            f = f.next;
        }
        ListNode next = head.next;
        head.next = null;
        pre.next = null;
        return (head.val == f.val) && isPalindrome2(next);
    }


    int[] list = new int[10000];

    public int[] reversePrint(ListNode head) {
        if (head == null) return null;
        int count = 0;
        while (head != null) {
            count++;
            list[list.length - count] = head.val;
            head = head.next;
        }
        int[] ret = new int[count];
        System.arraycopy(list, 10000 - count, ret, 0, ret.length);
        return ret;
    }

    public int getDecimalValue(ListNode head) {
        if (head == null) return 0;
        int ret = 0;
        while (head != null) {
            ret = (ret << 1) + head.val;
            head = head.next;
        }
        return ret;
    }

    public int getDecimalValue2(ListNode head) {
        if (head == null) return 0;
        StringBuilder sb = new StringBuilder();
        while (head != null) {
            sb.append(head.val);
            head = head.next;
        }
        return Integer.parseInt(sb.toString(), 2);
    }


    public ListNode getKthFromEnd(ListNode head, int k) {
        if (head == null) return null;
        ListNode h = head;
        int count = 0;
        while (head != null) {
            head = head.next;
            count++;
        }
        count = count - k;
        while (count-- > 0) {
            h = h.next;
        }
        return h;
    }

    /**
     * 返回中间节点
     */
    public ListNode middleNode(ListNode head) {
        if (head == null || head.next == null) return null;
        List<ListNode> list = new ArrayList<>();
        while (head != null) {
            list.add(head);
            head = head.next;
        }
        return list.get(list.size() / 2);
    }

    public ListNode removeDuplicateNodes(ListNode head) {
        if (head == null) return null;
        HashSet<Integer> set = new HashSet<>();
        set.add(head.val);
        ListNode dummy = new ListNode(-1, head);
        ListNode pre = head;
        ListNode h = head.next;
        while (h != null) {
            if (!set.add(h.val)) {
                h = h.next;
                continue;
            }
            pre.next = h;
            pre = pre.next;
            h = h.next;
        }
        pre.next = null;
        return dummy.next;
    }

    public ListNode deleteDuplicates(ListNode head) {
        if (head == null) return null;
        ListNode dummy = new ListNode(-1, head);
        ListNode pre = head;
        ListNode h = head.next;
        while (h != null) {
            if (h.val != pre.val) {
                pre.next = h;
                pre = pre.next;
            }
            h = h.next;
        }
        pre.next = null;
        return dummy.next;
    }

    public ListNode removeElements2(ListNode head, int val) {
        ListNode dummy = new ListNode(-1);
        ListNode pre = dummy;
        ListNode h = head;
        while (h != null) {
            ListNode next = h.next;
            h.next = null;
            if (h.val != val) {
                pre.next = h;
                pre = pre.next;
            }
            h = next;
        }
        return dummy.next;
    }

    public ListNode reverseList99(ListNode head) {
        if (head == null) return null;
        ListNode ret = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = ret;
            ret = head;
            head = next;
        }
        return ret;
    }


    public ListNode deleteNode(ListNode head, int val) {
        if (head == null) return null;
        if (head.val == val) return head.next;
        ListNode pre = head;
        ListNode n = head;
        while (n != null && n.val != val) {
            pre = n;
            n = n.next;
        }
        if (n != null) {
            pre.next = n.next;
        }
        return head;
    }

    public ListNode removeElements(ListNode head, int val) {
        if (head == null) return null;
        ListNode dummyNode = new ListNode(-1);
        ListNode pre = dummyNode;
        while (head != null) {
            ListNode next = head.next;
            head.next = null;
            if (head.val != val) {
                pre.next = head;
                pre = pre.next;
            }
            head = next;
        }
        return dummyNode.next;
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

    public ListNode mergeKLists2(ListNode[] lists) {
        ListNode ans = null;
        for (int i = 0; i < lists.length; ++i) {
            ans = mergeTwoLists(ans, lists[i]);
        }
        return ans;
    }

    public ListNode mergeTwoLists22(ListNode l1, ListNode l2) {
        //哑结点
        ListNode ns = new ListNode(-1);
        ListNode nsl = ns;//始终指向最后一个节点
        while (l1 != null && l2 != null) {
            if (l1.val > l2.val) {
                nsl.next = l2;
                l2 = l2.next;
            } else {
                nsl.next = l1;
                l1 = l1.next;
            }
            nsl = nsl.next;
        }
        //统一处理迭代中非空逻辑排除出来的节点
        nsl.next = l1 == null ? l2 : l1;
        return ns.next;
    }


    public ListNode mergeKLists(ListNode[] lists) {
        return mergeKListsHelp(lists, 0, lists.length - 1);
    }

    public ListNode mergeKListsHelp(ListNode[] lists, int s, int e) {
        if (s == e) {
            return lists[s];
        }
        if (s > e) {
            return null;
        }
        return mergeTwoLists(mergeKListsHelp(lists, s, (s + e) / 2), mergeKListsHelp(lists, (s + e) / 2 + 1, e));
    }

    /**
     * 返回倒数第k个节点
     */
    public int kthToLast(ListNode head, int k) {
        int count = 1;
        ListNode c = head;
        while (c.next != null) {
            c = c.next;
            count++;
        }
        c.next = head;
        count = count + 1 - k;
        while (count-- > 0) {
            c = c.next;
        }
        return c.val;
    }

    public int kthToLast100(ListNode head, int k) {
        List<ListNode> list = new ArrayList<>();
        while (head != null) {
            list.add(head);
            head = head.next;
        }
        return list.get(list.size() - k).val;
    }

    public ListNode[] splitListToParts(ListNode root, int k) {
        ListNode[] ret = new ListNode[k];
        if (k <= 0) return ret;
        int count = 0;
        ListNode h = root;
        while (h != null) {
            count++;
            h = h.next;
        }
        int len = count / k;
        int left = count % k;
        //执行分隔
        h = root;
        int i = 0;
        while (i < k) {
            int currentLen = (left--) > 0 ? len + 1 : len;
            if (currentLen == 0 || h == null) {
                ret[i++] = null;
                continue;
            }
            ListNode pre = new ListNode(-1, h);
            ListNode p = h;
            while (currentLen > 0 && p != null) {
                currentLen--;
                pre = p;
                p = p.next;
            }
            ListNode next = pre.next;
            pre.next = null;
            ret[i++] = h;
            h = next;
        }
        return ret;
    }
}



