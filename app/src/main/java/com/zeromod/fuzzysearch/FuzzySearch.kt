package com.zeromod.fuzzysearch

/**
 * algorithm : http://blog.amjith.com/fuzzyfinder-in-10-lines-of-python
 * */
inline fun <reified T : Any> List<T>.fuzzySearch(
    query: String?,
    block: List<T>.() -> List<String>
): List<T> {
    if (query == null) return this

    val filteredResults: MutableList<T> = emptyList<T>().toMutableList()

    val regexToken: String = if (query.isNotEmpty()) {
        query.map { "$it.*?" }.joinToString("")
    } else return this

    val regex = Regex(regexToken, setOf(RegexOption.IGNORE_CASE))
    val filtered: MutableList<FuzzyData> = emptyList<FuzzyData>().toMutableList()

    block(this).forEachIndexed { index, field ->
        regex.find(field)?.apply {
            filtered.add(FuzzyData(value.length, range.first, index))
        }
    }

    filtered.sortWith(compareBy(
        { it.matchLength },
        { it.rangeFirst }
    ))

    filtered.forEach {
        filteredResults.add(this[it.originIndex])
    }
    return filteredResults
}