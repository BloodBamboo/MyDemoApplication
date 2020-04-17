package com.example.admin.myapplication;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

//2020算法练习
public class TT {

    public void println(String s) {
        System.out.println(s);
    }

    public void println(int i) {
        System.out.println(i);
    }

    /**
     * 堆下沉，构建大根堆
     *
     * @param arr
     * @param parentIndex
     * @param length
     */
    public void down(int[] arr, int parentIndex, int length) {
        int temp = arr[parentIndex];
        int childIndex = 2 * parentIndex + 1;

        while (childIndex < length) {
//            println(i++ + "");
            if (childIndex + 1 < length && arr[childIndex] < arr[childIndex + 1]) {
                childIndex++;
            }

            if (temp > arr[childIndex]) {
                break;
            }

            arr[parentIndex] = arr[childIndex];
            parentIndex = childIndex;
            childIndex = 2 * parentIndex + 1;
        }

        arr[parentIndex] = temp;
    }

    int i = 0;

    /**
     * 堆排序，升序
     *
     * @param arr
     */
    public void sortHeap(int[] arr) {

        for (int i = arr.length / 2; i >= 0; i--) {
            down(arr, i, arr.length - 1);
        }
        i = 0;
        for (int i = arr.length - 1; i > 0; i--) {
            int temp = arr[i];
            arr[i] = arr[0];
            arr[0] = temp;

            down(arr, 0, i - 1);
        }
    }

    /**
     * 堆排序练习
     */
    @Test
    public void t1() {
        int[] arr = {1, 3, 2, 6, 5, 7, 8, 9, 10, 0};
        sortHeap(arr);
        println(Arrays.toString(arr));
    }


    /**
     * bitmap算法 又称位图算法
     */

    public static class MyBitmap {
        private long[] words;
        private int size;

        public MyBitmap(int size) {
            this.size = size;
            this.words = new long[getWordIndex(size) + 1];
        }

        public boolean getBit(int bitIndex) {
            if (bitIndex < 0 || bitIndex > size) {
                throw new IndexOutOfBoundsException("超出Bitmap范围");
            }

            int wordIndex = getWordIndex(bitIndex);
            //在 java 中，int 类型的数据长度为 32 位，如果将 int 类型左移或者右移大于或等于 32 位时，
            //并不会像预计的那样将数据全部填充为1或0。java 的处理方式是：当刚好为数据长度的整数倍时，即32、64······，
            //数据保持原来不变；其他情况下移动除以 32 余数的长度。同理 long 类型数据以 64 为变化基准。
            //在实际应用中，需要特别注意这点，当然也可以巧用此特性实现一些特殊算法的设计
            return (words[wordIndex] & (1L << bitIndex)) != 0;
        }

        public void setBit(int bitIndex) {
            if (bitIndex < 0 || bitIndex > size) {
                throw new IndexOutOfBoundsException("超出Bitmap范围");
            }
            int wordIndex = getWordIndex(bitIndex);

            words[wordIndex] |= (1L << bitIndex);
        }

        private int getWordIndex(int bitIindex) {
            //右移6位，相当于除以64
            return bitIindex >> 6;
        }
    }

    @Test
    public void t2() {
        MyBitmap myBitmap = new MyBitmap(128);
        myBitmap.setBit(127);
        myBitmap.setBit(1);
        println(myBitmap.getBit(127) + "");
        println(myBitmap.getBit(2) + "");

        int a = 3;
        int b = 5;
        int c = a & b;
        println(c + "");
        println((c | a) + "");
        println((c | b) + "");
//        long a = 1L;
//        println((a << 126) + "");
    }


    /**
     * Integer 自动装箱时如果范围在-127 128之间不创造新对象，超过创建新对象
     * 包装类型==比较的内存地址
     * equals判断类型是否相同，相同在比较值，类型不同直接返回false
     * 自动装箱就是调用该类型的valueOf方法，拆箱就是调用对应类的value值，比如Integer的intValue或者Long的longValue
     */
    @Test
    public void t3() {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        System.out.println(c == d); //true true的原因是使用了IntegerCache.cache 没有创建新对象，所以返回true
        System.out.println(e == f); //false 超过范围创建新对象所以返回false
        System.out.println(e.equals(f)); //true
        System.out.println(c == (a + b));//true 范围没超过128
        System.out.println(c.equals(a + b));//true 同类型，值比较
        System.out.println(g == (a + b));//true 此时类型不同，a+b的结果应该是进行了自动装箱成Long类型，又没超过范围，所以才会为true
        System.out.println(g.equals(a + b));//false 类型不同，直接返回false
    }

    /**
     * 位图算法，编程珠玑第一章
     */
    public ArrayList<Integer> bitSort(ArrayList<Integer> array) {
        byte[] bitList = new byte[n];
        long o = System.currentTimeMillis();
        for (int i = 0; i < array.size(); i++) {
            bitList[array.get(i)] = 1;
        }
        long c = System.currentTimeMillis();
        println((c - o) + "");
//        array.clear();
        int index = 0;
        for (int i = 0; i < n; i++) {
            if (bitList[i] == 1) {
                array.set(index, i);
                index++;
            }
        }
        o = System.currentTimeMillis();
        println((o - c) + "");
        return array;
    }

    /**
     * 随机生成k个不重复范围在0~n直接的数,实现原理，在一个0~n的有序数据中随机打乱0~k个位置的数据，进行调换，最后取k个值
     */
    public ArrayList<Integer> buildK(int k, int n) {
        ArrayList<Integer> result = new ArrayList<>();
        Random random = new Random();
        int[] x = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = i;
        }
        for (int i = 0; i < k; i++) {
            int r = random.nextInt(n - i) + i;
            int temp = x[i];
            x[i] = x[r];
            x[r] = temp;
        }

        for (int i = 0; i < k; i++) {
            result.add(x[i]);
        }
        return result;
    }

    //位图总长
    int n = 10000000;
    //随机不重复数量
    int k = 1000000;

    @Test
    public void bitSortTest() {

        ArrayList<Integer> array = buildK(k, n);
//        ArrayList<Integer> array = new ArrayList<>();
//        for (int i = k - 1; i >= 0; i--) {
//            array.add(i);
//        }

        long o = System.currentTimeMillis();
        array = bitSort(array);
        long c = System.currentTimeMillis();
        println((c - o) + "");
        println(array.size() + "");
//        for (int i = 0; i < array.size(); i++) {
//            println(array.get(i) + "");
//        }
//
//        long o1 = System.currentTimeMillis();
//        Collections.sort(array);
//        long c1 = System.currentTimeMillis();
//        println((c1 - o1) + "");
//        println(array.size() + "");
//        int[] x = new int[k];
//        for (int j = 0; j < array.size(); j++) {
//            x[j] = array.get(j);
//        }
//        o = System.currentTimeMillis();
//        sortHeap(x);
//        c = System.currentTimeMillis();
//        println((c - o) + "");
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    @Test
    public void t4() {
//        int[] p = {1, 2, 4, 7, 3, 5, 6, 8};
//        int[] i = {4, 7, 2, 1, 5, 3, 8, 6};
//        TreeNode treeNode = reConstructBinaryTree(p, i);
//
//        left(treeNode);

//        push(1);
//        push(2);
//        push(3);
//        println(pop());
//        println(pop());
//        push(4);
//        println(pop());
//        push(5);
//        println(pop());
//        println(pop());


//        println(JumpFloorII(4));
        int[] a = new int[]{2, 4, 6, 1, 3, 5, 7};//{1,2,3,4,5,6,7};
        reOrderArray(a);
        for (int i : a) {
            System.out.println(i);
        }

    }

    //变态跳问题
    public int JumpFloorII(int target) {
        if (target < 1) {
            return 0;
        }
        int sum = 1;
        int first = 1;
        //f(n) = f(1)+f(2)+f(3)+...+f(n-1) + 1
        for (int j = 1; j < target; j++) {
            sum += first;
            first = sum;
        }

        return sum;
    }


    public int JumpFloor(int target) {
        if (target <= 0) return 0;
        if (target == 1) return 1;
        if (target == 2) return 2;
        int one = 1;
        int two = 2;
        int result = 0;
        for (int i = 2; i < target; i++) {
            result = one + two;
            one = two;
            two = result;
        }
        return result;
    }

    /**
     * 斐波那契数列
     *
     * @param n
     * @return
     */
    public int Fibonacci(int n) {
        if (n == 0) {
            return 0;
        }
        if (n < 3) {
            return 1;
        }
        return Fibonacci(n - 1) + Fibonacci(n - 2);
    }

    /**
     * 通过2个栈实现入队出队
     */
    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> stack2 = new Stack<Integer>();

    public void push(int node) {
        stack1.push(node);
    }

    public int pop() {
        if (stack2.isEmpty()) {
            while (!stack1.isEmpty()) {
                stack2.push(stack1.pop());
            }
            return stack2.pop();
        } else {
            return stack2.pop();
        }
    }


    /**
     * 二叉树前序遍历
     *
     * @param treeNode
     */
    public void left(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        println(treeNode.val + "");
        left(treeNode.left);
        left(treeNode.right);
    }

    //例如输入前序遍历序列{1,2,4,7,3,5,6,8}和中序遍历序列{4,7,2,1,5,3,8,6},重建二叉树
    public TreeNode reConstructBinaryTree(int[] pre, int[] in) {
        return rebuild_tree(pre, in, 0, pre.length - 1, 0, in.length - 1);
    }


    public TreeNode rebuild_tree(int[] pre, int[] in, int pre_start, int pre_end, int in_start, int in_end) {
        TreeNode head = null;
        if (pre_start > pre_end || in_start > in_end) {
            return head;
        }

        head = new TreeNode(pre[pre_start]);

        int i_mid = in_end;
        for (int i = in_start; i < in_end; i++) {
            if (in[i] == head.val) {
                i_mid = i;
                break;
            }
        }

        int left_length = i_mid - in_start;

        head.left = rebuild_tree(pre, in, pre_start + 1, pre_start + left_length, in_start, i_mid - 1);
        head.right = rebuild_tree(pre, in, pre_start + left_length + 1, pre_end, i_mid + 1, in_end);
        return head;
    }

    //    //数组{3,4,5,1,2}为{1,2,3,4,5}的一个旋转
    public int minNumberInRotateArray(int[] array) {
        int count = array.length;
        if (count == 0) {
            return 0;
        }
        for (int j = count - 1; j > 0; j--) {
            int pre = j - 1;
            if (pre > -1 && array[j] < array[pre]) {
                return array[j];
            }
        }
        return 0;
    }


    /**
     * 数据的正负次方幂
     *
     * @param base
     * @param exponent
     * @return
     */
    public double Power(double base, int exponent) {
        //次方为0时返回1
        if (exponent == 0) {
            return 1;
        }

        double r = base;
        for (int i = 1, count = Math.abs(exponent); i < count; i++) {
            r *= base;
        }
        if (exponent > 0) {
            return r;
        } else {
            return 1 / r;
        }
    }

    //[1,2,3,4,5,6,7] 2,4,6,1,3,5,7
    public void reOrderArray(int[] array) {
        if (array.length < 2) {
            return;
        }

        int mid = 0;
        if (array.length % 2 == 0) {
            mid = array.length / 2;
        } else {
            mid = array.length / 2 + 1;
        }

        int[] a = new int[mid];
        int[] b = new int[array.length - mid];
        int a_index = 0, b_index = 0;


        for (int i = 0, count = array.length; i < count; i++) {
            if (array[i] % 2 == 0) {
                b[b_index] = array[i];
                b_index++;
            } else {
                a[a_index] = array[i];
                a_index++;
            }
        }

        for (int i = 0, count = a.length; i < count; i++) {
            array[i] = a[i];
        }

        for (int i = 0, count = b.length; i < count; i++) {
            array[mid + i] = b[i];
        }
    }


    /**
     * 输入一个链表，输出该链表中倒数第k个结点
     */
    public ListNode FindKthToTail(ListNode head, int k) {
        if (head == null) {
            return head;
        }

        if (k == 0) {
            return null;
        }

        int count = 0;
        ListNode l = head;
        boolean start = false;
        k -= 1;
        while (head.next != null) {
            if (count == k || start) {
                start = true;
                l = l.next;
            }
            head = head.next;
            count++;
        }
        if (count < k) {
            return null;
        }
        return l;
    }

    public class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }

    /**
     * 翻转一个链表
     *
     * @param head
     * @return
     */
    public ListNode ReverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode p = head;
        ListNode n = head.next;
        p.next = null;
        while (n.next != null) {
            ListNode tmp = n.next;
            n.next = p;
            p = n;
            n = tmp;
        }
        //这一步很重要
        if (n.next == null) {
            n.next = p;
        }
        return n;
    }

    /**
     * 输入两个单调递增的链表，输出两个链表合成后的链表，当然我们需要合成后的链表满足单调不减规则。
     *
     * @param list1
     * @param list2
     * @return
     */
    public ListNode Merge(ListNode list1, ListNode list2) {
        ListNode heard;
        if (list1 == null && list2 == null) {
            return null;
        }
        if (list1 == null) {
            heard = list2;
            return heard;
        }

        if (list2 == null) {
            heard = list1;
            return heard;
        }

        if (list1.val < list2.val) {
            heard = list1;
            heard.next = Merge(list1.next, list2);
        } else {
            heard = list2;
            heard.next = Merge(list1, list2.next);
        }

        return heard;
    }

    /**
     * 判断root2是不是root1的子树
     *
     * @param root1
     * @param root2
     * @return
     */
    public boolean HasSubtree(TreeNode root1, TreeNode root2) {
        if (root2 == null) {
            return false;
        }
        if (root1 == null && root2 != null) {
            return false;
        }
        boolean isSub = false;
        if (root1.val == root2.val) {
            isSub = isSub(root1.left, root2.left) && isSub(root1.right, root2.right);
        }
        if (!isSub) {
            isSub = HasSubtree(root1.left, root2);
            if (!isSub) {
                isSub = HasSubtree(root1.right, root2);
            }
        }

        return isSub;

    }

    private boolean isSub(TreeNode root, TreeNode root2) {
        if (root2 == null) {
            return true;
        }
        if (root == null && root2 != null) {
            return false;
        }

        if (root.val == root2.val) {
            return isSub(root.left, root2.left) && isSub(root.right, root2.right);
        }
        return false;
    }

    /**
     * 操作给定的二叉树，将其变换为源二叉树的镜像。
     *
     * @param root
     */
    public void Mirror(TreeNode root) {
        if (root == null) {
            return;
        }
        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;
        Mirror(root.left);
        Mirror(root.right);
    }
//1 2 3 4
//5 6 7 8
//9 10 11 12
//13 14 15 16

    @Test
    public void t5() {

    }

    /**
     * 输入一个矩阵，按照从外向里以顺时针的顺序依次打印出每一个数字，
     * 例如，如果输入如下4 X 4矩阵： 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16
     * 则依次打印出数字1,2,3,4,8,12,16,15,14,13,9,5,6,7,11,10.
     * @param matrix
     * @return
     */
    public ArrayList<Integer> printMatrix(int[][] matrix) {
        ArrayList<Integer> list = new ArrayList<>();
        int row = matrix.length;
        int col = matrix[0].length;
        if (row == 0 || col == 0)  return null;
        int left = 0,top = 0,right = col - 1,bottom = row - 1;
        while(left <= right && top <= bottom){
            for(int i = left; i <= right; i++) {
                list.add(matrix[top][i]);
            }
            for (int i = top + 1; i <= bottom ; i++) {
                list.add(matrix[i][right]);
            }
            if (top != bottom){
                for (int i = right - 1; i >= left ; i--) {
                    list.add(matrix[bottom][i]);
                }
            }

            if (left != right) {
                for (int i = bottom - 1; i > top ; i--) {
                    list.add(matrix[i][left]);
                }
            }
            left++;
            top++;
            right--;
            bottom--;
        }

        return list;
    }


}


