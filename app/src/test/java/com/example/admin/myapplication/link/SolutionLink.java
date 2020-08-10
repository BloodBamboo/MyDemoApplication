package com.example.admin.myapplication.link;

import org.junit.Test;

/**
 * create by yx
 * on 2020/6/24
 */
public class SolutionLink {
    class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    /**
     * 141链表是否有环
     */
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }

        ListNode fast = head.next;
        ListNode low = head;
        while (fast != low) {
            if (fast == null || fast.next == null) {
                return false;
            }
            fast = fast.next.next;
            low = low.next;
        }
        return true;
    }

    /**
     * 面试题 02.08. 环路检测
     * 给定一个有环链表，实现一个算法返回环路的开头节点。
     * 有环链表的定义：在链表中某个节点的next元素指向在它前面出现过的节点，则表明该链表存在环路。
     */
    public ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }

        ListNode fast = head.next;
        ListNode low = head;
        while (fast != low) {
            if (fast == null || fast.next == null) {
                return null;
            }
            fast = fast.next.next;
            low = low.next;
        }

        fast = head;
        low = low.next;
        while (fast != low) {
            fast = fast.next;
            low = low.next;
        }
        return fast;
    }

    /**
     * 反转单链表
     */
    public ListNode reverseList(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode newHead = null;
        ListNode next = head;
        ListNode tmp = null;
        while (next != null) {
            tmp = next.next;
            next.next = newHead;
            newHead = next;
            next = tmp;
        }
        return newHead;
    }


    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    /**
     * 109. 有序链表转换二叉搜索树
     * 给定一个单链表，其中的元素按升序排序，将其转换为高度平衡的二叉搜索树。
     * <p>
     * 本题中，一个高度平衡二叉树是指一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过 1。
     */
    public TreeNode sortedListToBST(ListNode head) {
        if (head == null) {
            return null;
        }

        ListNode mid = findMid(head);
        TreeNode root = new TreeNode(mid.val);
        if (head == mid) {
            return root;
        }

        root.left = sortedListToBST(head);
        root.right = sortedListToBST(mid.next);

        return root;
    }

    private ListNode findMid(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode fast = head;
        ListNode low = head;
        ListNode pre = null;
        while (fast != null && fast.next != null) {
            pre = low;
            fast = fast.next.next;
            low = low.next;
        }

        if (pre != null) {
            pre.next = null;
        }

        return low;
    }

    /**
     * 61. 旋转链表
     * 给定一个链表，旋转链表，将链表每个节点向右移动 k 个位置，其中 k 是非负数。
     */
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null) {
            return null;
        }

        if (k == 0 || head.next == null) {
            return head;
        }

        ListNode tail = head;
        int size = 1;
        while (tail.next != null) {
            tail = tail.next;
            size++;
        }

        if (k == size || k % size == 0) {
            return head;
        }

        if (k > size) {
            k = size - (k % size);
        } else {
            k = size - k;
        }

        tail = head;
        while (k > 1) {
            tail = tail.next;
            k--;
        }

        ListNode newHead = tail.next;
        tail.next = null;
        tail = newHead;
        while (tail.next != null) {
            tail = tail.next;
        }

        tail.next = head;

        return newHead;
    }

    /**
     * 203. 移除链表元素
     * 删除链表中等于给定值 val 的所有节点。
     */
    public ListNode removeElements(ListNode head, int val) {
        if (head == null) {
            return head;
        }

        ListNode tmpHead = new ListNode(-1);
        ListNode tail = tmpHead;
        while (head != null) {
            if (head.val != val) {
                tail.next = head;
                tail = head;
            }
            head = head.next;
        }

        return tmpHead.next;
    }

    /**
     * 387. 字符串中的第一个唯一字符
     * 给定一个字符串，找到它的第一个不重复的字符，并返回它的索引。如果不存在，则返回 -1。
     */
    public int firstUniqChar(String s) {
        int map[] = new int[122];
        char array[] = s.toCharArray();
        for (char c : array) {
            map[c]++;
        }

        for (int i = 0, count = array.length; i < count; i++) {
            if (map[array[i]] == 1) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 844. 比较含退格的字符串
     * 给定 S 和 T 两个字符串，当它们分别被输入到空白的文本编辑器后，判断二者是否相等，并返回结果。 # 代表退格字符。
     * <p>
     * 注意：如果对空文本输入退格字符，文本继续为空。
     */
    public boolean backspaceCompare(String S, String T) {
        if ((S == null || S.length() == 0) && (T != null || T.length() != 0)) {
            return false;
        }
        if ((T == null || T.length() == 0) && (S != null || S.length() != 0)) {
            return false;
        }

        char s_array[] = S.toCharArray();
        char t_array[] = T.toCharArray();
        char s1[] = new char[S.length()];
        char t1[] = new char[T.length()];
        int s1_index = 0;
        int t1_index = 0;
        for (char c : s_array) {
            if (c == '#') {
                if (s1_index > 0) {
                    s1_index--;
                }
            } else {
                s1[s1_index++] = c;
            }
        }

        for (char c : t_array) {
            if (c == '#') {
                if (t1_index > 0) {
                    t1_index--;
                }
            } else {
                t1[t1_index++] = c;
            }
        }

        if (s1_index == t1_index) {
            while (s1_index > 0) {
                s1_index--;
                if (t1[s1_index] != s1[s1_index]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 148. 排序链表
     * 在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序。
     */
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        int length = 0;
        ListNode prev = head;
        while (prev != null) {
            prev = prev.next;
            length++;
        }
        int intv = 1;
        prev = new ListNode(0);
        prev.next = head;
        ListNode[] array;
        ListNode current;
        ListNode tail;
        ListNode left;
        ListNode right;
        while (intv < length) {
            current = prev.next;
            tail = prev;
            while (current != null) {
                left = current;
                right = cut(left, intv);
                current = cut(right, intv);

                array = merge(left, right);
                tail.next = array[0];
                tail = array[1];
            }
            intv <<= 1;
        }

        return prev.next;
    }

    private ListNode cut(ListNode head, int n) {
        while (head != null && n > 1) {
            head = head.next;
            n--;
        }

        if (head == null) {
            return null;
        }

        ListNode result = head.next;
        head.next = null;
        return result;
    }

    //按升序合并
    private ListNode[] merge(ListNode left, ListNode right) {
        ListNode head = new ListNode(0);
        ListNode result = head;
        while (left != null && right != null) {
            if (left.val < right.val) {
                head.next = left;
                head = left;
                left = left.next;
            } else {
                head.next = right;
                head = right;
                right = right.next;
            }
        }
        head.next = left == null ? right : left;
        while (head.next != null) {
            head = head.next;
        }
        return new ListNode[]{result.next, head};
    }

    /**
     * 160. 相交链表
     * 编写一个程序，找到两个单链表相交的起始节点。
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode pA = headA;
        ListNode pB = headB;
        while (pA != pB) {
            pA = pA == null ? headB : pA.next;
            pB = pB == null ? headA : pB.next;
        }

        return pA;
    }

    @Test
    public void t5() {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(3);
        l1.next = l2;
        ListNode l3 = new ListNode(0);
        l2.next = l3;
        ListNode l4 = new ListNode(-3);
        l3.next = l4;

        SolutionLink solutionLink = new SolutionLink();
        ListNode r = solutionLink.sortList(l1);
        while (r != null) {
            System.out.println(r.val + "->");
            r = r.next;
        }
    }
}
