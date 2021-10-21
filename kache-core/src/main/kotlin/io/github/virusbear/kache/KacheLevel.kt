package io.github.virusbear.kache

class KacheLevel<K, V>(kache: Kache<K, V>, val level: Int): Kache<K, V> by kache