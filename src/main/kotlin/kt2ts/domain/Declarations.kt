package kt2ts.domain

import kotlin.reflect.KClass

@JvmInline
value class ClassQualifiedName(val name: String) {
    companion object {
        fun of(clazz: KClass<*>) = ClassQualifiedName(clazz.java.name)
    }
}

@JvmInline value class PackageName(val name: String)
