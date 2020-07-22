package com.example.admin.myapplication.str

/**
 *create by yx
 *on 2020/7/22
 */
class SolutionKotlin {
    /**
     * 无重复字符的最长子串
     * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
     * 通过滑动窗口的方式解决问题，需要理解滑动窗口，暂时比较模糊。判断是否重复子串通过map计数实现效果，或者hash表
     */
    fun lengthOfLongestSubstring(s: String): Int {
        var right = 0
        val set:HashSet<Char> = HashSet()
        var tmp = 0
        var max = 0
        val size = s.length
        for(i in s.indices) {
            while (right < size && !set.contains(s[right])) {
                set.add(s[right])
                right++
            }
            set.remove(s[i])
            tmp = right - i
            if (max <tmp) max = tmp
        }

        return max
    }
}