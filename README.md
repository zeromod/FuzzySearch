# Fuzzy-Search

Kotlin Implimentation of algorithm : http://blog.amjith.com/fuzzyfinder-in-10-lines-of-python

[![](https://jitpack.io/v/zeromod/FuzzySearch.svg)](https://jitpack.io/#zeromod/FuzzySearch)

# Gradle

Add it in your root build.gradle at the end of repositories:

    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

 Add the dependency:
 
     dependencies {
	        implementation 'com.github.zeromod:FuzzySearch:1.0'
	}

# Usage


*  call `.fuzzySearch` from any list object.


*  Also supports custom datatype like POJO/Data class members with help of generics.

##### Example

    data class Book (
        val id: Int,
        val name: String
    )
    val books: List<Book>

specify member name as `books.fuzzySearch(searchText, "name")`

## params

| Param | Type |
| ------ | ------ |
| query | String |
| fieldName | String |
