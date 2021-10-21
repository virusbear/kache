package io.github.virusbear.kache

import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext

class MultiLevelKache<K, V>(
    override val name: String,
    private val levels: List<KacheLevel<K, V>>,
    private val populateCacheFromLowerLevels: Boolean
): Kache<K, V> {
    private val coroutineScope = CoroutineScope(EmptyCoroutineContext)

    override suspend fun get(key: K): V? =
        getInternal(key, levels, 0)

    private suspend fun getInternal(key: K, levels: List<KacheLevel<K, V>>, level: Int): V? {
        if(level !in levels.indices) {
            return null
        }

        val kache = levels.sortedBy { it.level }[level]
        var value = kache.get(key)

        if(value == null) {
            val slowValue = getInternal(key, levels, level + 1)

            if(slowValue != null) {
                if(populateCacheFromLowerLevels) {
                    //Eventually consistent
                    coroutineScope.launch {
                        kache.put(key, slowValue)
                    }
                }

                value = slowValue
            }
        }

        return value
    }

    //Eventually consistent
    override suspend fun put(key: K, value: V) {
        coroutineScope.launch {
            levels.map {
                async {
                    it.put(key, value)
                }
            }.awaitAll()
        }
    }

    //Eventually consistent
    override suspend fun remove(key: K) {
        coroutineScope.launch {
            levels.map {
                async {
                    it.remove(key)
                }
            }.awaitAll()
        }
    }

    override suspend fun contains(key: K): Boolean =
        levels.firstOrNull {
            it.contains(key)
        }?.let { true } ?: false

    //Eventually consistent
    override suspend fun clear() {
        coroutineScope.launch {
            levels.map {
                async {
                    it.clear()
                }
            }
        }
    }
}