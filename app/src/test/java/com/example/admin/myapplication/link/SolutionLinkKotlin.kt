package com.example.admin.myapplication.link

import java.util.*
import kotlin.collections.HashMap

/**
 *create by yx
 *on 2020/6/24
 */
class SolutionLinkKotlin {

    /**
     * 141链表是否有环
     */
    fun hasCycle(head: ListNode?): Boolean {
        if (head?.next == null) {
            return false
        }

        var fast = head.next
        var low = head
        while (fast != low) {
            if (fast?.next == null) {
                return false
            }
            fast = fast?.next?.next
            low = low?.next
        }

        return true
    }

    /**
     * 面试题 02.08. 环路检测
     * 给定一个有环链表，实现一个算法返回环路的开头节点。
     * 有环链表的定义：在链表中某个节点的next元素指向在它前面出现过的节点，则表明该链表存在环路。
     */

    fun detectCycle(head: ListNode?): ListNode? {
        if (head?.next == null) return null

        var fast = head
        var low = head
        while (fast?.next != null) {
            fast = fast?.next?.next
            low = low?.next
            if (fast == low) break
        }
        if (fast?.next == null) return null
        fast = head
        while (fast != low) {
            fast = fast?.next
            low = low?.next
        }
        return fast
    }

    fun reverseList(head: ListNode?): ListNode? {
        if (head == null) {
            return null
        }
        var newHead: ListNode? = null
        var next = head
        var tmp: ListNode? = null
        while (next != null) {
            tmp = next.next
            next.next = newHead
            newHead = next
            next = tmp
        }
        return newHead
    }

    /**
     * 面试题 02.02. 返回倒数第 k 个节点
     * 实现一种算法，找出单向链表中倒数第 k 个节点。返回该节点的值。
     */
    fun kthToLast(head: ListNode?, k: Int): Int {
        if (head == null || k < 0) {
            return -1
        }

        var fast = head
        var low = head
        var tmp = k
        while (tmp != 0) {
            fast = fast?.next
            tmp--
        }

        while (fast != null) {
            fast = fast?.next
            low = low?.next
        }

        return low!!.`val`
    }

    /**
     * 109. 有序链表转换二叉搜索树
     * 给定一个单链表，其中的元素按升序排序，将其转换为高度平衡的二叉搜索树。
     * <p>
     * 本题中，一个高度平衡二叉树是指一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过 1。
     * 此方法跟链表递归方法可能不一样，因为构建的树存在多种可能
     */
    class TreeNode(var `val`: Int) {
        var left: TreeNode? = null
        var right: TreeNode? = null
    }

    class ListNode(var `val`: Int) {
        var next: ListNode? = null
    }

    fun sortedListToBST(head: ListNode?): TreeNode? {
        if (head == null) {
            return null
        }

        val array = list2Array(head);

        return convertListToBST(array, 0, array.size - 1)
    }

    fun convertListToBST(array: ArrayList<Int>, left: Int, right: Int): TreeNode? {
        if (left > right) {
            return null
        }

        val mid = (left + right) / 2

        val root = TreeNode(array[mid])
        if (left == right) {
            return root
        }

        root.left = convertListToBST(array, left, mid - 1)
        root.right = convertListToBST(array, mid + 1, right)


        return root
    }

    fun list2Array(head: ListNode?): ArrayList<Int> {
        var next = head
        val array = arrayListOf<Int>()
        while (next != null) {
            array.add(next.`val`)
            next = next.next
        }
        return array
    }

    /**
     * 61. 旋转链表
     * 给定一个链表，旋转链表，将链表每个节点向右移动 k 个位置，其中 k 是非负数。
     */
    fun rotateRight(head: ListNode?, k: Int): ListNode? {
        if (head?.next == null || k == 0) {
            return head
        }

        var size = 1
        var tail = head
        while (tail?.next != null) {
            tail = tail.next
            size++
        }

        if (size == k) {
            return head
        }

        tail?.next = head
        var tailIndex = size - (k % size) - 1
        tail = head
        while (tailIndex > 0) {
            tail = tail?.next
            tailIndex--
        }

        val newHead = tail?.next!!
        tail?.next = null

        return newHead
    }

    /**
     * 203. 移除链表元素
     * 删除链表中等于给定值 val 的所有节点。
     */
    fun removeElements(head: ListNode?, `val`: Int): ListNode? {
        if (head == null) {
            return head
        }

        val tmp = ListNode(-1)
        var pre: ListNode = tmp
        var current: ListNode? = head
        tmp.next = current
        while (current != null) {
            if (current.`val` == `val`) {
                pre.next = current.next
            } else {
//                pre.next = current
                pre = current
            }

            current = current.next
        }

        return tmp.next
    }

    /**
     * 387. 字符串中的第一个唯一字符
     * 给定一个字符串，找到它的第一个不重复的字符，并返回它的索引。如果不存在，则返回 -1。
     */
    fun firstUniqChar(s: String): Int {
        if (s.isEmpty()) {
            return -1
        }

        val map: HashMap<Char, Int> = HashMap()
        val arrayC = s.toCharArray()
        for (c in arrayC) {
            map[c] = map.getOrDefault(c, 0) + 1
        }

        for (i in arrayC.indices) {
            if (map[arrayC[i]] == 1) {
                return i
            }
        }

        return -1
    }

    /**
     * 844. 比较含退格的字符串
     * 给定 S 和 T 两个字符串，当它们分别被输入到空白的文本编辑器后，判断二者是否相等，并返回结果。 # 代表退格字符。
     * <p>
     * 注意：如果对空文本输入退格字符，文本继续为空。
     */
    fun backspaceCompare(S: String, T: String): Boolean {
        val stack1: Stack<Char> = Stack()
        val stack2: Stack<Char> = Stack()
        for (c in S) {
            if (c == '#') {
                if (stack1.isNotEmpty()) stack1.pop()
            } else {
                stack1.push(c)
            }
        }

        for (c in T) {
            if (c == '#') {
                if (stack2.isNotEmpty()) stack2.pop()
            } else {
                stack2.push(c)
            }
        }

        if (stack1.size != stack2.size) {
            return false
        }
        while (stack1.isNotEmpty()) {
            if (stack1.pop() != stack2.pop()) {
                return false
            }
        }
        return true
    }


    /**
     * 160. 相交链表
     * 编写一个程序，找到两个单链表相交的起始节点。
     */
    fun getIntersectionNode(headA: ListNode?, headB: ListNode?): ListNode? {
        var pA = headA
        var pB = headB
        while (pA != pB) {
            pA = if (pA == null) headB else pA.next
            pB = if (pB == null) headA else pB.next
        }

        return pA
    }

    /**
     * 148. 排序链表
     * 在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序。
     */
    fun sortList(head: ListNode?): ListNode? {
        if (head?.next == null) return head

        var size = 0
        var p = head
        while (p != null) {
            p = p.next
            size++
        }

        var step = 1
        p = ListNode(0)
        p.next = head
        var current: ListNode?
        var tail: ListNode?
        var left: ListNode?
        var right: ListNode?
        var two: Array<ListNode?>
        while (step < size) {
            current = p.next
            tail = p
            while (current != null) {
                left = current
                right = cut(left, step)
                current = cut(right, step)

                two = merge(left, right)
                tail!!.next = two[0]
                tail = two[1]
            }
            step *= 2
        }

        return p.next
    }

    private fun cut(head: ListNode?, n: Int): ListNode? {
        if (head == null) return null
        var tmp = head
        var length = n
        while (tmp != null && length > 1) {
            tmp = tmp.next
            length--
        }

        if (tmp == null) return null

        val r = tmp.next
        tmp.next = null
        return r
    }

    //按升序合并
    private fun merge(left: ListNode?, right: ListNode?): Array<ListNode?> {
        if (left == null && right == null) {
            return arrayOf(null, null)
        }

        var tmp: ListNode = ListNode(0)
        var tmpLeft = left
        var tmpRight = right
        val r = tmp
        while (tmpLeft != null && tmpRight != null) {
            if (tmpLeft.`val` < tmpRight.`val`) {
                tmp.next = tmpLeft
                tmpLeft = tmpLeft.next
            } else {
                tmp.next = tmpRight
                tmpRight = tmpRight.next
            }
            tmp = tmp.next!!
        }
        tmp.next = tmpLeft ?: tmpRight

        while (tmp.next != null) {
            tmp = tmp.next!!
        }
        return arrayOf(r.next, tmp)
    }

    /**
     * 21. 合并两个有序链表
     */
    fun mergeTwoLists(l1: ListNode?, l2: ListNode?): ListNode? {
        if (l1 == null) {
            return l2
        } else if (l2 == null) {
            return l1
        }

        return if (l1.`val` < l2.`val`) {
            l1.next = mergeTwoLists(l1.next, l2)
            l1
        } else {
            l2.next = mergeTwoLists(l1, l2.next)
            l2
        }
    }
}