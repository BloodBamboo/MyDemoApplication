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


    /**
     * 567. 字符串的排列
     * 给定两个字符串 s1 和 s2，写一个函数来判断 s2 是否包含 s1 的排列。
     * 换句话说，第一个字符串的排列之一是第二个字符串的子串。
     */
    public boolean checkInclusion(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() > s2.length()) {
            return false;
        }

        int map1[] = new int[26];
        int map2[] = new int[26];
        int size1 = s1.length();
        for (int i = 0; i < size1; i++) {
            map1[s1.charAt(i) - 'a']++;
            map2[s2.charAt(i) - 'a']++;
        }
        map2[s2.charAt(size1 - 1) - 'a']--;
        for (int i = size1 - 1, count = s2.length(); i < count; i++) {
            map2[s2.charAt(i) - 'a']++;
            if (compare(map1, map2)) {
                return true;
            }
            map2[s2.charAt(i - size1 + 1) - 'a']--;
        }

        return false;
    }

    private boolean compare(int[] map1, int[] map2) {
        for (int i = 0, count = map1.length; i < count; i++) {
            if (map1[i] != map2[i]) {
                return false;
            }
        }
        return true;
    }
}
