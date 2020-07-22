package com.example.admin.myapplication.str;

import java.util.HashSet;
import java.util.Set;

/**
 * create by yx
 * on 2020/7/22
 */
public class Solution {
    /**
     * 无重复字符的最长子串
     * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
     * 通过滑动窗口的方式解决问题，需要理解滑动窗口，暂时比较模糊。判断是否重复子串通过map计数实现效果，或者hash表
     */
    public int lengthOfLongestSubstring(String s) {
        if (s == null) {
            return 0;
        }

        int right = 0;
        Set<Character> set = new HashSet<>();
        int tmp, max = 0;
        for (int i = 0, count = s.length(); i < count; i++) {
            while (right < count && !set.contains(s.charAt(right))) {
                set.add(s.charAt(right));
                right++;
            }

            set.remove(s.charAt(i));
            tmp = right - i;
            if (max < tmp) {
                max = tmp;
            }
        }
        set.clear();
        set = null;
        return max;
    }

}
