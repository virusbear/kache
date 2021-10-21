package io.github.virusbear.kache

import io.micrometer.core.instrument.*

class MetricsKacheDecorator<K, V>(private val kache: Kache<K, V>): Kache<K, V> by kache {
    private val tags = Tags.of("kache", kache.name)
    private val hitCount = Counter.builder("kache.hit").tags(tags).description("Number of cache hits by get/contains").register(Metrics.globalRegistry)
    private val missCount = Counter.builder("kache.miss").tags(tags).description("Number of cache misses by get/contains").register(Metrics.globalRegistry)
    private val getTime = Timer.builder("kache.get").tags(tags).description("Time spend by reading values from cache").register(Metrics.globalRegistry)
    private val putTime = Timer.builder("kache.put").tags(tags).description("Time spend by writing values from cache").register(Metrics.globalRegistry)
    private val removeTime = Timer.builder("kache.remove").tags(tags).description("Time spend removing keys from cache").register(Metrics.globalRegistry)
    private val getCount = Counter.builder("kache.get").tags(tags).description("Number of read requests").register(Metrics.globalRegistry)
    private val putCount = Counter.builder("kache.put").tags(tags).description("Number of write requests").register(Metrics.globalRegistry)

    override suspend fun get(key: K): V? {
        getCount.increment()

        val value = sample(getTime) {
            kache.get(key)
        }

        value?.let {
            hitCount.increment()
        } ?: missCount.increment()

        return value
    }

    override suspend fun put(key: K, value: V) {
        putCount.increment()

        sample(putTime) {
            kache.put(key, value)
        }
    }

    override suspend fun remove(key: K) {
        sample(removeTime) {
            kache.remove(key)
        }
    }

    private inline fun <T> sample(timer: Timer, block: () -> T): T {
        val sample = Timer.start(Metrics.globalRegistry)
        val result = block()
        sample.stop(timer)

        return result
    }
}

fun <K, V> Kache<K, V>.withMetrics(): Kache<K, V> =
    MetricsKacheDecorator(this)