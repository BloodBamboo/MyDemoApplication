package com.example.admin.myapplication;

import org.junit.Test;

import java.util.Arrays;

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
            println(i++ + "");
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
        Integer a=1;
        Integer b=2;
        Integer c=3;
        Integer d=3;
        Integer e=321;
        Integer f=321;
        Long g=3L;
        System.out.println(c==d); //true true的原因是使用了IntegerCache.cache 没有创建新对象，所以返回true
        System.out.println(e==f); //false 超过范围创建新对象所以返回false
        System.out.println(e.equals(f)); //true
        System.out.println(c==(a+b));//true 范围没超过128
        System.out.println(c.equals(a+b));//true 同类型，值比较
        System.out.println(g==(a+b));//true 此时类型不同，a+b的结果应该是进行了自动装箱成Long类型，又没超过范围，所以才会为true
        System.out.println(g.equals(a+b));//false 类型不同，直接返回false
        
    }
}


