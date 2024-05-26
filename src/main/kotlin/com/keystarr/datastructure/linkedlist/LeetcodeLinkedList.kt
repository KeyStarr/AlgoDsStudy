package com.keystarr.datastructure.linkedlist

/**
 * Specification based on https://leetcode.com/problems/design-linked-list/
 */
interface LeetcodeLinkedList {

    fun get(index: Int): Int

    fun addAtHead(value: Int)

    fun addAtTail(value: Int)

    fun addAtIndex(index: Int, value: Int)

    fun deleteAtIndex(index: Int)
}