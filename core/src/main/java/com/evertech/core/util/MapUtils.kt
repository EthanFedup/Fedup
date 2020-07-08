package com.evertech.core.util

import com.blankj.utilcode.util.ObjectUtils

import java.util.ArrayList
import java.util.Objects

object MapUtils {

    /**
     * @param map
     * @return
     */
    fun <K, V> getKeys(map: Map<K, V>): List<K>? {
        if (ObjectUtils.isEmpty(map)) {
            return null
        }

        val list = ArrayList<K>()
        val iterator = getIterator(map)

        while (iterator!!.hasNext()) {
            val entry = iterator.next() as Map.Entry<*, *>
            list.add(entry.key as K)
        }

        return list
    }

    /**
     * @param map
     * @return
     */
    fun <K, V> getValues(map: Map<K, V>): List<V>? {
        if (ObjectUtils.isEmpty(map)) {
            return null
        }

        val list = ArrayList<V>()
        val iterator = getIterator(map)

        while (iterator!!.hasNext()) {
            val entry = iterator.next() as Map.Entry<*, *>
            list.add(entry.value as V)
        }

        return list
    }

    private fun getIterator(map: Map<*, *>?): Iterator<*>? {
        return map?.entries?.iterator()
    }

    /**
     * is null or its size is 0
     *
     *
     *
     *
     * <pre>
     * isEmpty(null)   =   true;
     * isEmpty({})     =   true;
     * isEmpty({1})    =   false;
    </pre> *
     *
     * @param <V>
     * @param sourceList
     * @return if list is null or its size is 0, return true, else return false.
    </V> */
    fun <V> isEmpty(sourceList: List<V>?): Boolean {
        return sourceList == null || sourceList.size == 0
    }

    fun <T, E> getKeyByValue(map: Map<T, E>, value: E): T? {
        for ((key, value1) in map) {
            if (value == value1) {
                return key
            }
        }
        return null
    }


}
