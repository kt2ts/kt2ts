package io.github.kt2tssample

import com.fasterxml.jackson.annotation.JsonTypeInfo
import kt2ts.annotation.GenerateTypescript

// TODO pr object type en constante ailleurs
@GenerateTypescript
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "objectType",
)
sealed class SomeSealedClass

data class Nullable(val value: String?) : SomeSealedClass()

data class WithList(val list: List<String>) : SomeSealedClass()

data class ComplexGenerics(val list: List<Pair<String, String>>) : SomeSealedClass()

data class SomeClassImpl(val someValue: String) : SomeSealedClass()

data class AnotherClassImpl(val anotherValue: Int) : SomeSealedClass()
