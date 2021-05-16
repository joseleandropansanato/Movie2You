package br.com.jlcampos.movie2you.utils

import android.os.Build
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MiscellaneousUtils {

    fun getYear(date: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val format = DateTimeFormatter.ofPattern("yyyy")

            current.format(format)
        } else {
            date
        }
    }
}