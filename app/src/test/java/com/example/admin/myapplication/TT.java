package com.example.admin.myapplication;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//2020算法练习
public class TT {

    public void println(String s) {
        System.out.println(s);
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
        int[] p = {1, 2, 4, 7, 3, 5, 6, 8};
        int[] i = {4, 7, 2, 1, 5, 3, 8, 6};
        TreeNode treeNode = reConstructBinaryTree(p, i);

        left(treeNode);
    }


    public void left(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        println(treeNode.val+"");
        left(treeNode.left);
        left(treeNode.right);
    }

    //例如输入前序遍历序列{1,2,4,7,3,5,6,8}和中序遍历序列{4,7,2,1,5,3,8,6},重建二叉树
    public TreeNode reConstructBinaryTree(int[] pre, int[] in) {
        return rebuild_tree(pre, in, 0, pre.length -1, 0, in.length-1);
    }


    public TreeNode rebuild_tree(int[] pre, int[] in, int pre_start, int pre_end ,int in_start, int in_end) {
        TreeNode head = null;
        if (pre_start > pre_end || in_start > in_end) {
            return head;
        }

        head = new TreeNode(pre[pre_start]);

        int i_mid = in_end;
        for (int i = in_start ; i < in_end ; i++) {
            if (in[i] == head.val) {
                i_mid = i;
                break;
            }
        }

        int left_length  = i_mid - in_start;

        head.left = rebuild_tree(pre, in,pre_start + 1, pre_start + left_length, in_start, i_mid - 1);
        head.right = rebuild_tree(pre, in, pre_start + left_length + 1, pre_end, i_mid + 1, in_end);
        return head;
    }
}


