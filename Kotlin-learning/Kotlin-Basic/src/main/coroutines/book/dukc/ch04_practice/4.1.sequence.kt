package book.dukc.ch04_practice

/**
 * 《深入理解Kotlin协程》chapter 4.1：序列生成器
 *
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 2020/9/25 10:08
 */
fun main() {

    val fibonacci = sequence {
        yield(1)
        var current = 1
        var next = 1
        while (true) {
            yield(next)
            next += current
            current = next - current
        }
    }

    for (i in fibonacci.take(10)) {
        println(i)
    }
}