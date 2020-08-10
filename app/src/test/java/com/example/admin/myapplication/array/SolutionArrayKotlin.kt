package com.example.admin.myapplication.array

import kotlin.math.log10

/**
 *create by yx
 *on 2020/6/22
 */

class SolutionArrayKotlin {

    /**
     * 565. 数组嵌套
     * 索引从0开始长度为N的数组A，包含0到N - 1的所有整数。找到最大的集合S并返回其大小，其中 S[i] = {A[i], A[A[i]], A[A[A[i]]], ... }且遵守以下的规则。
     * 假设选择索引为i的元素A[i]为S的第一个元素，S的下一个元素应该是A[A[i]]，之后是A[A[A[i]]]... 以此类推，
     * 不断添加直到S出现重复的元素。
     */
    fun arrayNesting(nums: IntArray): Int {
        val length = nums.size
        if (length == 0) {
            return 0
        }

        var target = 0
        for (i in 0 until length) {
            if (nums[i] != -1) {
                var start = nums[i]
                var count = 0
                var tmp = -1
                while (nums[i] != -1) {
                    tmp = start
                    start = nums[start]
                    count++
                    nums[tmp] = -1
                }
                target = Math.max(target, count)
            }
        }

        return target
    }

    /**
     * 1295. 统计位数为偶数的数字
     * 给你一个整数数组 nums，请你返回其中位数为 偶数 的数字的个数。
     */
    fun findNumbers(nums: IntArray): Int {
        if (nums.size == 0) {
            return 0;
        }

        var result = 0
        for (i in nums) {
            if ((log10(i.toFloat()) + 1).toInt() % 2 == 0) result++
        }
        return result
    }

    /**
     * 1343. 大小为 K 且平均值大于等于阈值的子数组数目
     * 给你一个整数数组 arr 和两个整数 k 和 threshold 。
     * 请你返回长度为 k 且平均值大于等于 threshold 的子数组数目。
     */
    fun numOfSubarrays(arr: IntArray, k: Int, threshold: Int): Int {
        if (arr?.size == 0 || k < 0) {
            return 0
        }

        val sumTarget = k * threshold
        var sum = 0
        var result = 0
        for (i in 0 until k) {
            sum += arr[i]
        }

        if (sum >= sumTarget) {
            result++
        }
        for (pos in k until arr.size) {
            sum += arr[pos] - arr[pos - k]
            if (sum >= sumTarget) {
                result++
            }
        }

        return result
    }


    /**
     * 1122. 数组的相对排序
     *
     * 给你两个数组，arr1 和 arr2，
     * arr2 中的元素各不相同
     * arr2 中的每个元素都出现在 arr1 中
     * 对 arr1 中的元素进行排序，使 arr1 中项的相对顺序和 arr2 中的相对顺序相同。未在 arr2 中出现过的元素需要按照升序放在 arr1 的末尾。
     */
    fun relativeSortArray(arr1: IntArray, arr2: IntArray): IntArray {
        if (arr1 == null) {
            return arr1
        }

        val count = IntArray(1001)

        for (value in arr1) {
            count[value]++
        }
        var index = 0
        for (value in arr2) {
            while (count[value] > 0) {
                arr1[index] = value
                index++
                count[value]--
            }
        }

        for (i in count.indices) {
            while (count[i] > 0) {
                arr1[index] = i
                index++
                count[i]--
            }
        }
        return arr1
    }

    /**
     * 283. 移动零
     * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
     */
    fun moveZeroes(nums: IntArray): Unit {
        var tmp: Int
        var tail: Int = 0
        for (i in nums.indices) {
            if (nums[i] != 0) {
                tmp = nums[i]
                nums[i] = nums[tail]
                nums[tail++] = tmp
            }
        }
    }

    /**
     * 405. 数字转换为十六进制数
     * 给定一个整数，编写一个算法将这个数转换为十六进制数。对于负整数，我们通常使用 补码运算 方法。
     */
    fun toHex(num: Int): String {
        var numTmp = num
        val map = "0123456789abcdef".toCharArray()
        var tmp: Int
        val sb = StringBuilder()
        while (numTmp != 0) {
            tmp = numTmp and 0xf
            sb.append(map[tmp])
            numTmp = numTmp ushr 4
        }

        if (sb.isEmpty()) {
            sb.append('0')
        }

        return sb.reverse().toString()
    }

//    fun toHex(num: Int): String {
//        if (num == 0) {
//            return "0"
//        }
//
//        var tmp = num
//        val map = "0123456789abcdef".toCharArray()
//        var str:String = ""
//        while (tmp != 0) {
//            str =  map[tmp and 0xf] + str
//            tmp = tmp ushr 4
//        }
//        return str
//    }

    /**
     * 349. 两个数组的交集
     * 给定两个数组，编写一个函数来计算它们的交集。
     */
    fun intersection(nums1: IntArray, nums2: IntArray): IntArray {
        val set1 = HashSet<Int>()
        val set2 = HashSet<Int>()

        for (i in nums1) {
            set1.add(i)
        }
        for (i in nums2) {
            set2.add(i)
        }
        var list = mutableListOf<Int>()
        for (i in set1) {
            if (set2.contains(i)) list.add(i)
        }

        return list.toIntArray()
    }

    /**
     * 面试题 05.01. 插入
     * 插入。给定两个32位的整数N与M，以及表示比特位置的i与j。
     * 编写一种方法，将M插入N，使得M从N的第j位开始，到第i位结束。
     * 假定从j位到i位足以容纳M，也即若M = 10 011，那么j和i之间至少可容纳5个位。
     * 例如，不可能出现j = 3和i = 2的情况，因为第3位和第2位之间放不下M
     */
    fun insertBits(N: Int, M: Int, i: Int, j: Int): Int {
        var n = N
        var m = M
        for (k in i..j) {
            n = n.and((1 shl k).inv())
        }

        m = m.shl(i)
        return n or m
    }


}