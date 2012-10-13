Where-Clause
============

Goal is to provide a simple DSL to users to ease data query in UI :

* UI would show text filter for each data property
* If valid, filter will fill a SQL query (join and where clause)
* Filter can apply to `Boolean`, `Number`, `Date`, `Enum` and `String` types 

See this [unit test](https://github.com/MartyHub/where-clause/blob/master/src/test/java/com/bnpparibas/eqd/prs/where/test/SearchTest.java) for an example 

Grammar
-------

Is available through this [class](https://github.com/MartyHub/where-clause/blob/master/src/main/java/com/bnpparibas/eqd/prs/where/parser/OperationsParser.java)

### Supported features

* Basic expressions : `is null, is not null, is true, is false, is empty, is not empty`
* Basic operators : `=, !=, >, >=, <, <=`
* List operators : `in, not in`
* String operators : `like, not like`
* Binary operators : `and, or`

### Samples

* `is true`
* `(< 10 or >= 123.456 or in (12, 13.14)) and not in (5.5, 555)`
* `like youpi% and != youpi0 or is empty`
* `in (USER, GROUP) or is null`
* `>= 2012-09-01 and < 2012-10-01`

Main dependencies
-----------------

* [parboiled](http://www.parboiled.org) : parsing Java library
* [Querydsl](http://www.querydsl.com/) : SQL construction framework in Java

Todo
----

* Currently only `JPAQuery` is supported
* Find a way to provide auto-completion to user
* Not sure `Enum` data type is really working, should `String` be converted to `Enum` in JPA parameters ?