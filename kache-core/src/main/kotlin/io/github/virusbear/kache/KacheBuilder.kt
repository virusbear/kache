package io.github.virusbear.kache

class KacheBuilder<K, V>(val name: String) {
    private val levels = mutableListOf<Kache<K, V>>()

    var propagateDownstream: Boolean = true

    operator fun Kache<K, V>.unaryPlus() {
        levels += this
    }

    fun build(): Kache<K, V> =
        MultiLevelKache(
            name,
            levels.mapIndexed(::KacheLevel),
            propagateDownstream
        )
}

fun <K, V> kache(name: String, block: KacheBuilder<K, V>.() -> Unit): Kache<K, V> =
    KacheBuilder<K, V>(name).apply(block).build()