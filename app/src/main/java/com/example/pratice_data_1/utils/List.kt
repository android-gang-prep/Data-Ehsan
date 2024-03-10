package com.example.pratice_data_1.utils

fun <T>List<T>.next(current:T):T{
    val indexOfCurrent = indexOf(current)
    var newIndex =indexOfCurrent+1
    if (newIndex > lastIndex) newIndex = 0
    return get(newIndex)
}