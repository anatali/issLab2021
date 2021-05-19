class App {
    val greeting: String
        get() = "Hello World!"

    companion object {
        @kotlin.jvm.JvmStatic
        fun main(args: Array<String>) {
            println(App().greeting)
        }
    }
}