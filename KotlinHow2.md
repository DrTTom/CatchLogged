# How I created my first Kotlin project

This is not a tutorial, its just a list of questions I came across and how it can be done.
That is not necessarily how it should be done!

## How do I build?

Kotlin can be built by a Gradle project. The plugin `org.jetbrains.kotlin:kotlin-gradle-plugin` did the trick.
It seems compatible enough with the application plugin. Remaining Question: Can the plugins-block be used?

## Hellow world 

```kotlin 
fun main(args: Array<String>): Unit {
  val name: String = "TAU"
  println(name)
} 
```
Note that "static" stuff must be done outside class.

## Where to find the language definition

https://kotlinlang.org/docs/reference/ is comprehensive and seems to cover enough of the language.


## How do I get IDE support?

Got an Eclipse plugin from https://kotlinlang.org/docs/tutorials/getting-started-eclipse.html which 
provides some syntax highlighting and breaks coding conventions with formatting. So what, its only the format.
Some Errors/Warnings and code completion, but not always. There is a new entry 'convert Java to Kotlin' in the
context menu but it seems to work only for short classes. Want other IDE.

Found online Java to Kotlin conversion at
https://try.kotlinlang.org/#/Kotlin%20Koans/Introduction/Java%20to%20Kotlin%20conversion/Task.kt
which works as expected, i.e. the generated code cannot be compiled. However, it was useful because generated stuff
was very easily turned into correct class.

## Is there something like JavaDoc for Kotlin

Yes and no. The tool is named Dokka but the repository link claimed in https://github.com/Kotlin/dokka/blob/master/README.md
does not exist. So following the instructions will lead to an error.

## How do Kotlin classes integrate into Java Projects? 

**Calling Java from Kotlin**
No problems so far, just import and call as expected. Consider that java return values may be null, be prepared to
do casts like (String?)! to String.

**Calling Kotlin from Java**
- Note these special Kt - "classes" which seem to contain static stuff.
- Requires special jars at runtime.

## How does programming feel like?
Lots of stuff can be left out because it is assumed implicitly by the compiler. Open source tooling is inacceptable.

- implicit non null check is good
- primary constructors are great in trivial cases
- ranges in for loops look good
- no ';' at line ending feels good

Kotlin code looks better than Java in case of default data classes and classes with lots of overloading. In complex classes,
the quantity of implicit assumptions might be a disadvantage.
Finding further real advantages of the language seems to require some more experience, not just translation of Java ideas into Kotlin.

However, the programming task of current project seems not to be an ideal candidate to apply Kotlin.