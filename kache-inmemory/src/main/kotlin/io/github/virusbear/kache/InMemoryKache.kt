package io.github.virusbear.kache

import io.github.reactivecircus.cache4k.Cache

class InMemoryKache<K: Any, V: Any>(override val name: String, val cache: Cache<K, V>): Kache<K, V> {
    override suspend fun get(key: K): V? =
        cache.get(key)

    override suspend fun put(key: K, value: V) {
        cache.put(key, value)
    }

    override suspend fun remove(key: K) {
        cache.invalidate(key)
    }

    override suspend fun contains(key: K): Boolean =
        cache.get(key) != null

    override suspend fun clear() {
        cache.invalidateAll()
    }
}