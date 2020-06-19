package com.example.admin.myapplication.stack;

import org.junit.Test;

import java.util.LinkedList;

/**
 * 根据自己理解双链表实现
 */
public class MyStackjava {

    private LinkedList<Integer> deque;
    private LinkedList<Integer> deque2;

    /**
     * Initialize your data structure here.
     */
    public MyStackjava() {
        deque = new LinkedList();
        deque2 = new LinkedList();
    }

    /**
     * Push element x onto stack.
     */
    public void push(int x) {
        if (deque2.isEmpty()) {
            deque.add(x);
        } else {
            deque2.add(x);
        }

    }

    /**
     * Removes the element on top of the stack and returns that element.
     */
    public int pop() {
        if (deque2.isEmpty()) {
            while (deque.size() > 1) {
                deque2.add(deque.pop());
            }
            return deque.pop();
        } else {
            while (deque2.size() > 1) {
                deque.add(deque2.pop());
            }
            return deque2.pop();
        }
    }

    /**
     * Get the top element.
     */
    public int top() {
        if (deque2.isEmpty()) {
            while (deque.size() > 1) {
                deque2.add(deque.pop());
            }
            int top = deque.pop();
            deque2.add(top);
            return top;
        } else {
            while (deque2.size() > 1) {
                deque.add(deque2.pop());
            }
            int top = deque2.pop();
            deque.add(top);
            return top;
        }
    }

    /**
     * Returns whether the stack is empty.
     */
    public boolean empty() {
        if (deque.isEmpty() && deque2.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }


    @Test
    public void t() {
        String s = "abbaca";
        SolutionDeleteRepetition solution = new SolutionDeleteRepetition();
        println(solution.removeDuplicates(s));
    }


    private void println(String s) {
        System.out.println(s);
    }

    private void println(int s) {
        System.out.println(s + "");
    }
}
