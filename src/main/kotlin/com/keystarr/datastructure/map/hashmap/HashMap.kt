package com.keystarr.datastructure.map.hashmap

interface HashMap {

    fun put(key: String, value: Any)

    fun get(key: String): Any?

    fun remove(key: String)

    fun containsKey(key: String): Boolean

    fun size(): Int
}
