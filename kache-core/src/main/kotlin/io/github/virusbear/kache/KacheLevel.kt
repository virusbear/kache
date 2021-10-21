package io.github.virusbear.kache

class KacheLevel<K, V>(
    val level: Int,
    kache: Kache<K, V>
): Kache<K, V> by kache