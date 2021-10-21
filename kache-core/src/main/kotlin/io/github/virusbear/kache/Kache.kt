package io.github.virusbear.kache

interface Kache<K, V> {
    val name: String
    suspend fun get(key: K): V?
    suspend fun put(key: K, value: V)
    suspend fun remove(key: K)
    suspend fun contains(key: K): Boolean
    suspend fun clear()
}

/*
kache<String, BufferedImage> {
    memory("l1") {
        eviction(TLRU) {
            threshold = 100 (keys)
            ttl = TimeUnit.seconds(10)
        }
    }
    redis("l2") {
        eviction(EXPIRE) {
            ttl = TimeUnit.minutes(10)
        }
        value {
            type = Custom<BufferedImage, PrimitiveType.ByteArray> {
                reader {
                    BufferedImage.read(it)
                }

                writer {
                    it.toByteArray()
                }
            }
        }
        key {
            type = PrimitiveType.String
        }
    }
}
 */