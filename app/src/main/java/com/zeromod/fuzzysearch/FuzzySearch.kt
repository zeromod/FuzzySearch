package com.zeromod.fuzzysearch

import android.util.Log

/**
 * algorithm : http://blog.amjith.com/fuzzyfinder-in-10-lines-of-python
 * */
inline fun <reified T : Any> List<T>.fuzzySearch(query: String?, fieldName: String? = null): List<T> {
    query?.let {
        val regexToken: String = if (query.isNotEmpty()) {
            query.map { "$it.*?" }.joinToString("")
        } else return this

        val regex = Regex(regexToken, setOf(RegexOption.IGNORE_CASE))
        val filtered: MutableList<Pair<Int, Pair<Int, T>>> = emptyList<Pair<Int, Pair<Int, T>>>().toMutableList()
        var matchResult: MatchResult?

        this.forEach {
            when {
                (fieldName != null) -> {
                    try {
                        it::class.java.getDeclaredField(fieldName).let { field ->
                            field.isAccessible = true
                            val value = field.get(it)
                            if (value != null && value is String) {
                                matchResult = regex.find(value)
                            } else {
                                Log.e("Fuzzy Search", "Field: '$fieldName', is either null or not a String")
                                return this
                            }
                        }
                    } catch (e: NoSuchFieldException) {
                        Log.e("Fuzzy Search", "Field: '$fieldName', not found in the class ${it::class}")
                        return this
                    }
                }

                (T::class == String::class) -> matchResult = regex.find(it as String)

                else -> {
                    Log.e("Fuzzy Search", "Unsupported operation , required field type String")
                    return this
                }
            }
            matchResult?.let { result ->
                filtered.add(result.value.length to (result.range.first to it))
            }
        }

        filtered.sortWith(compareBy(
            { it.first },
            { it.second.first }
        ))

        val filteredResults: MutableList<T> = emptyList<T>().toMutableList()
        filtered.forEach {
            filteredResults.add(it.second.second)
        }
        return filteredResults
    }
    return this
}