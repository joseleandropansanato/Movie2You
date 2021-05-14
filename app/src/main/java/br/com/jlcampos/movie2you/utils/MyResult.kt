package br.com.jlcampos.movie2you.utils

class MyResult<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): MyResult<T> = MyResult(status = Status.SUCCESS, data = data, message = null)
        fun <T> error(data: T?, message: String): MyResult<T> = MyResult(status = Status.ERROR, data = data, message = message)
        fun <T> load (data: T?): MyResult<T> = MyResult(status = Status.LOADING, data = data, message = null)
    }
}