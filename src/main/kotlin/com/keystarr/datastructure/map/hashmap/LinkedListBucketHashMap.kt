package com.keystarr.datastructure.map.hashmap

/**
 * TODO:
 *  - efficient memory usage: expand/shrink and rehash based on load factor;
 *  - correct collision handling: if hashes collide for different objects => store them in the linked list;
 *  - put/remove worst/average for amortized O(1) (?) and get/containsKey/size for always O(1).
 *
 *  That is, solve most of the challenges the real production HashMap implementation faces, but maybe just in suboptimal way
 *  (no sense now to go for a full on java.util.HashMap idea by idea).
 */
class LinkedListBucketHashMap : HashMap {

    override fun put(key: String, value: Any) {
        TODO("Not yet implemented")
    }

    override fun get(key: String): Any? {
        TODO("Not yet implemented")
    }

    override fun remove(key: String) {
        TODO("Not yet implemented")
    }

    override fun containsKey(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun size(): Int {
        TODO("Not yet implemented")
    }
}
