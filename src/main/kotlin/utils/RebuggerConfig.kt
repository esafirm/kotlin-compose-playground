package utils

object RebuggerConfig {

    /*Default values*/
    private val defaultLogger: (tag: String, message: String) -> Unit = { tag, message ->
        println("$tag -- $message")
    }
    private const val DEFAULT_TAG = "Rebugger"

    /**/
    internal var tag = DEFAULT_TAG
    internal var logger: (tag: String, message: String) -> Unit = defaultLogger


    /**
     * To override Rebugger's default variables
     */
    fun init(
        tag: String = DEFAULT_TAG,
        logger: (tag: String, message: String) -> Unit = defaultLogger
    ) {
        this.tag = tag
        this.logger = logger
    }

}
