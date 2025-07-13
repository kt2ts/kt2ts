package io.github.kt2tssample.subpackage

import kt2ts.annotation.GenerateTypescript

data class UnusedClass(val someValue: String)

data class BaseDataClass(val someValue: String)

@GenerateTypescript data class ClassInSubPackage(val string: String)
