package io.github.kt2tssample

import io.github.kt2tssample.subpackage.BaseDataClass
import java.time.LocalDate
import kt2ts.annotation.GenerateTypescript

@GenerateTypescript
data class MyDataClass(
    // "scalar" types
    val boolean: Boolean,
    val nullableBoolean: Boolean?,
    val double: Double,
    val nullableDouble: Double?,
    val int: Int,
    val nullableInt: Int?,
    val long: Long,
    val nullableLong: Long?,
    val string: String,
    val nullableString: String?,
    val enum: MyEnum,
    val nullableEnum: MyEnum?,
    // collections
    val intList: List<Int>,
    val nullableIntList: List<Int?>,
    val stringList: List<String>,
    val nullableStringList: List<String?>,
    val enumList: List<MyEnum>,
    val nullableEnumList: List<MyEnum?>,
    val classList: List<BaseDataClass>,
    val nullableClassList: List<BaseDataClass?>,
    val nullableClassNullableList: List<BaseDataClass?>?,
    val nullableList: List<BaseDataClass>?,
    val set: Set<BaseDataClass>,
    val nullableClassSet: Set<BaseDataClass?>,
    val nullableClassNullableSet: Set<BaseDataClass?>?,
    val nullableSet: Set<BaseDataClass>?,
    val mapEnumKey: Map<MyEnum, BaseDataClass>,
    val mapIdKey: Map<MySampleId, BaseDataClass>,
    val map: Map<String, BaseDataClass>,
    val nullableClassMap: Map<String, BaseDataClass?>,
    val nullableClassNullableMap: Map<String, BaseDataClass?>?,
    val nullableMap: Map<String, BaseDataClass>?,
    // mapping
    val date: LocalDate,
    val nullableDate: LocalDate?,
    // data class
    val item: BaseDataClass,
    val nullableItem: BaseDataClass?,
    val id: MySampleId,
    val nullableId: MySampleId?,
    // kotlin Pair
    val intPair: Pair<Int, Int>,
    val nullableIntPair: Pair<Int?, Int?>,
    val stringPair: Pair<String, String>,
    val nullableStringPair: Pair<String?, String?>,
    val classPair: Pair<BaseDataClass, BaseDataClass>,
    val nullableClassPair: Pair<BaseDataClass?, BaseDataClass?>,
    val nullableClassNullablePair: Pair<BaseDataClass?, BaseDataClass?>?,
    val nullablePair: Pair<BaseDataClass, BaseDataClass>?,
    // kotlin Triple
    val classTriple: Triple<BaseDataClass, BaseDataClass, BaseDataClass>,
    val any: Any,
    val nullableAny: Any?,
)

@GenerateTypescript
data class ComplexStuff(
    val value:
        List<List<Pair<Map<MySampleId, List<Map<MySampleId, BaseDataClass>>>, BaseDataClass>>>
)
