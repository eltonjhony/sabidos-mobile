package com.sabidos.data.mapper

abstract class DataMapper<T, V> {
    abstract fun transform(entity: T): V
    abstract fun transform(entities: List<T>): List<V>
}