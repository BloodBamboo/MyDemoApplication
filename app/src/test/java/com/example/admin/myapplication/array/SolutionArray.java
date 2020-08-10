package com.example.admin.myapplication.array;

/**
 * create by yx
 * on 2020/6/22
 */
public class SolutionArray {
    /**
     * 565. 数组嵌套
     * 索引从0开始长度为N的数组A，包含0到N - 1的所有整数。找到最大的集合S并返回其大小，其中 S[i] = {A[i], A[A[i]], A[A[A[i]]], ... }且遵守以下的规则。
     * 假设选择索引为i的元素A[i]为S的第一个元素，S的下一个元素应该是A[A[i]]，之后是A[A[A[i]]]... 以此类推，
     * 不断添加直到S出现重复的元素。
     */
    public int arrayNesting(int[] nums) {
        int length = nums.length;
        if (length == 0) {
            return 0;
        }

        byte[] bitMap = new byte[length];

        int target = 0;
        for (int i = 0; i < length; i++) {
            //重复出现意味着产生环，重点理解这点
            if (bitMap[nums[i]] == 0) {
                int start = nums[i], count = 0;
                do {
                    bitMap[nums[start]] = 1;
                    start = nums[start];
                    count++;
                } while (start != nums[i]);//相等意味着产生环
                target = Math.max(target, count);
            }
        }
        return target;
    }

    /**
     * 1295. 统计位数为偶数的数字
     * 给你一个整数数组 nums，请你返回其中位数为 偶数 的数字的个数。
     */
    public int findNumbers(int[] nums) {
        int count = nums.length;
        if (count == 0) {
            return 0;
        }

        int result = 0;
        int tmp = 0, bit = 0;
        for (int i = 0; i < count; i++) {
//            if (String.valueOf(nums[i]).length() % 2 == 0) {
//                result++;
//            }
            tmp = nums[i];
            bit = 0;
            while (tmp != 0) {
                tmp = tmp / 10;
                bit++;
            }
            if (bit % 2 == 0) {
                result++;
            }
        }

        return result;
    }


    /**
     * 1343. 大小为 K 且平均值大于等于阈值的子数组数目
     * 给你一个整数数组 arr 和两个整数 k 和 threshold 。
     * 请你返回长度为 k 且平均值大于等于 threshold 的子数组数目。
     */
    public int numOfSubarrays(int[] arr, int k, int threshold) {
        if (arr.length == 0) {
            return 0;
        }
        int sunTarget = k * threshold;
        int sum = 0;
        int result = 0;

        for (int i = 0; i < k; i++) {
            sum += arr[i];
        }

        int adder = sum - sunTarget;
        if (adder >= 0) {
            result++;
        }
        for (int i = 0, pos = k, count = arr.length - k; i < count; i++, pos++) {
            adder += arr[pos] - arr[i];
            if (adder >= 0) {
                result++;
            }
        }

        return result;
    }


    /**
     * 123. 买卖股票的最佳时机 III
     * 给定一个数组，它的第 i 个元素是一支给定的股票在第 i 天的价格。
     * 设计一个算法来计算你所能获取的最大利润。你最多可以完成 两笔 交易。
     * 注意: 你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
     * <p>
     * 参考网上解题思路，不是非常理解，需要多次练习
     */
    public int maxProfit(int[] prices) {
        if (prices.length < 2) {
            return 0;
        }

        int minPrice1 = Integer.MAX_VALUE;
        int maxProfit1 = 0;
        int maxProfitAfterBuy = Integer.MIN_VALUE;
        int maxProfit2 = 0;
        for (int price : prices) {
            // 1.第一次最小购买价格
            minPrice1 = Math.min(minPrice1, price);

            // 2.第一次卖出的最大利润
            maxProfit1 = Math.max(maxProfit1, price - minPrice1);

            // 3.第二次购买后的剩余净利润
            maxProfitAfterBuy = Math.max(maxProfitAfterBuy, maxProfit1 - price);

            // 4.第二次卖出后，总共获得的最大利润（第3步的净利润 + 第4步卖出的股票钱）
            maxProfit2 = Math.max(maxProfit2, price + maxProfitAfterBuy);
        }
        return maxProfit2;
    }

    /**
     * 1122. 数组的相对排序
     * <p>
     * 给你两个数组，arr1 和 arr2，
     * arr2 中的元素各不相同
     * arr2 中的每个元素都出现在 arr1 中
     * 对 arr1 中的元素进行排序，使 arr1 中项的相对顺序和 arr2 中的相对顺序相同。未在 arr2 中出现过的元素需要按照升序放在 arr1 的末尾。
     */
    public int[] relativeSortArray(int[] arr1, int[] arr2) {
        int count[] = new int[1001];
        for (int val : arr1) {
            count[val]++;
        }
        int index = 0;
        for (int val : arr2) {
            while (count[val] > 0) {
                arr1[index] = val;
                index++;
                count[val]--;
            }
        }

        for (int i = 0; i < count.length; i++) {
            while (count[i] > 0) {
                arr1[index] = i;
                index++;
                count[i]--;
            }
        }

        return arr1;
    }


    /**
     * 283. 移动零
     * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
     */
    public void moveZeroes(int[] nums) {
        if (nums == null) {
            return;
        }
        int tmp;
        for (int i = 0, tail = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                tmp = nums[i];
                nums[i] = nums[tail];
                nums[tail++] = tmp;
            }
        }
    }

    /**
     * 405. 数字转换为十六进制数
     * 给定一个整数，编写一个算法将这个数转换为十六进制数。对于负整数，我们通常使用 补码运算 方法。
     */
    public String toHex(int num) {
        char map[] = "0123456789abcdef".toCharArray();
        StringBuilder sb = new StringBuilder();
        int tmp;
        while (num != 0) {
            tmp = num & 0xf;
            sb.append(map[tmp]);
            num >>>= 4;
        }

        if (sb.length() == 0) {
            sb.append('0');
        }
        return sb.reverse().toString();
    }

    /**
     * 349. 两个数组的交集
     * 给定两个数组，编写一个函数来计算它们的交集。
     */
    public int[] intersection(int[] nums1, int[] nums2) {
        if (nums1.length == 0) {
            return nums1;
        }

        if (nums2.length == 0) {
            return nums2;
        }

        insertSort(nums1);
        insertSort(nums2);
        int[] array = new int[Math.min(nums1.length, nums2.length)];
        int count = 0;
        int index = 0;
        if (nums1.length < nums2.length) {
            for (int i = 0; i < nums1.length; i++) {
                for (int j = index; j < nums2.length; j++) {
                    if (nums1[i] == nums2[j]) {
                        if (count == 0 || nums1[i] != array[count - 1]) {
                            array[count++] = nums1[i];
                        }
                        index = j;
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < nums2.length; i++) {
                for (int j = index; j < nums1.length; j++) {
                    if (nums2[i] == nums1[j]) {
                        if (count == 0 || nums2[i] != array[count - 1]) {
                            array[count++] = nums2[i];
                        }
                        index = j;
                        break;
                    }
                }
            }
        }
        int[] r = new int[count];
        for (int i = 0; i < count; i++) {
            r[i] = array[i];
        }
        return r;
    }

    //插入排序
    public void insertSort(int[] nums) {
        int current = 0;
        int tmp;
        for (int i = 0; i < nums.length; i++) {
            current = i;
            while (current > 0 && nums[current] < nums[current - 1]) {
                tmp = nums[current - 1];
                nums[current - 1] = nums[current];
                nums[current] = tmp;
                current--;
            }
        }
    }

    /**
     * 面试题 05.01. 插入
     * 插入。给定两个32位的整数N与M，以及表示比特位置的i与j。
     * 编写一种方法，将M插入N，使得M从N的第j位开始，到第i位结束。
     * 假定从j位到i位足以容纳M，也即若M = 10 011，那么j和i之间至少可容纳5个位。
     * 例如，不可能出现j = 3和i = 2的情况，因为第3位和第2位之间放不下M
     */
    public int insertBits(int N, int M, int i, int j) {
        int tmp;
        for (int k = i; k <= j; k++) {
            tmp = 1 << k;
            if ((N & tmp) > 0) {
                N -= tmp;
            }
        }
        M = M << i;
        return N | M;
    }

}
