package com.github.neoturak.db

import java.lang.reflect.Constructor

internal object JavaSqliteUtils {
    private fun <T> wrap(c: Class<T>): Class<T> {
        return if (c.isPrimitive) PRIMITIVES_TO_WRAPPERS[c] as Class<T> else c
    }

    val PRIMITIVES_TO_WRAPPERS: MutableMap<Class<*>?, Class<*>> =
        HashMap()

    init {
        PRIMITIVES_TO_WRAPPERS[Boolean::class.javaPrimitiveType] = Boolean::class.java
        PRIMITIVES_TO_WRAPPERS[Byte::class.javaPrimitiveType] = Byte::class.java
        PRIMITIVES_TO_WRAPPERS[Char::class.javaPrimitiveType] = Char::class.java
        PRIMITIVES_TO_WRAPPERS[Double::class.javaPrimitiveType] = Double::class.java
        PRIMITIVES_TO_WRAPPERS[Float::class.javaPrimitiveType] = Float::class.java
        PRIMITIVES_TO_WRAPPERS[Int::class.javaPrimitiveType] = Int::class.java
        PRIMITIVES_TO_WRAPPERS[Long::class.javaPrimitiveType] = Long::class.java
        PRIMITIVES_TO_WRAPPERS[Short::class.javaPrimitiveType] = Short::class.java
        PRIMITIVES_TO_WRAPPERS[Void.TYPE] = Void::class.java
    }

    @Throws(Exception::class)
    fun <T> newInstance(constructor: Constructor<T>, arguments: Array<Any?>): T {
        return constructor.newInstance(*arguments)
    }
}
