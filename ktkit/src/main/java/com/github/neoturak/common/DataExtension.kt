package com.github.neoturak.common
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import java.io.File
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

/**
 * 数据扩展类
 **/


/**
 * 时间戳转时间
 */
@SuppressLint("SimpleDateFormat")
fun Long.toTime(patten: String?): String {
    return if (this == 0L) {
        ""
    } else {
        SimpleDateFormat(patten).format(this)
    }
}

/**
 * 时间转时间戳
 */
@SuppressLint("SimpleDateFormat")
fun String?.toTime(patten: String): Long {
    return if (isNullOrEmpty()) {
        0L
    } else {
        SimpleDateFormat(patten).parse(this).time
    }

}

/**
 * 字符串时间戳转时间
 */
@SuppressLint("SimpleDateFormat")
fun String?.longToTime(pattern: String = "yyyy-MM-dd HH:mm"): String {
    if (this == null) {
        return ""
    }
    return try {
        val toLong = when (this.length) {
            10 -> "${this}000".toLong()
            13 -> this.toLong()
            else -> throw NumberFormatException()
        }
        SimpleDateFormat(pattern).format(toLong)
    } catch (e: NumberFormatException) {
        this
    }
}

/**
 * 时间转换
 */
fun String?.compareDateTime(): String {
    if (this.isNullOrEmpty()) {
        return ""
    }
    try {
        val time = this.toLong()
        return when (System.currentTimeMillis() - time) {
            in 0..60 * 1000 -> "刚刚"
            in 59 * 60 * 1000..58 * 60 * 1000 -> "59分钟前"
            in 58 * 60 * 1000..57 * 60 * 1000 -> "58钟前"
            in 57 * 60 * 1000..56 * 60 * 1000 -> "57分钟前"
            in 56 * 60 * 1000..55 * 60 * 1000 -> "56分钟前"
            in 55 * 60 * 1000..54 * 60 * 1000 -> "55分钟前"
            in 54 * 60 * 1000..53 * 60 * 1000 -> "54分钟前"
            in 53 * 60 * 1000..52 * 60 * 1000 -> "53分钟前"
            in 52 * 60 * 1000..51 * 60 * 1000 -> "52分钟前"
            in 51 * 60 * 1000..50 * 60 * 1000 -> "51分钟前"
            in 50 * 60 * 1000..49 * 60 * 1000 -> "50分钟前"
            in 49 * 60 * 1000..48 * 60 * 1000 -> "49分钟前"
            in 48 * 60 * 1000..47 * 60 * 1000 -> "48分钟前"
            in 47 * 60 * 1000..46 * 60 * 1000 -> "47分钟前"
            in 46 * 60 * 1000..45 * 60 * 1000 -> "46分钟前"
            in 45 * 60 * 1000..44 * 60 * 1000 -> "45分钟前"
            in 44 * 60 * 1000..43 * 60 * 1000 -> "44分钟前"
            in 43 * 60 * 1000..42 * 60 * 1000 -> "43分钟前"
            in 42 * 60 * 1000..41 * 60 * 1000 -> "42分钟前"
            in 41 * 60 * 1000..40 * 60 * 1000 -> "41分钟前"
            in 40 * 60 * 1000..39 * 60 * 1000 -> "40分钟前"
            in 39 * 60 * 1000..38 * 60 * 1000 -> "39分钟前"
            in 38 * 60 * 1000..37 * 60 * 1000 -> "38分钟前"
            in 37 * 60 * 1000..36 * 60 * 1000 -> "37分钟前"
            in 36 * 60 * 1000..35 * 60 * 1000 -> "36分钟前"
            in 35 * 60 * 1000..34 * 60 * 1000 -> "35分钟前"
            in 34 * 60 * 1000..33 * 60 * 1000 -> "34分钟前"
            in 33 * 60 * 1000..32 * 60 * 1000 -> "33分钟前"
            in 32 * 60 * 1000..31 * 60 * 1000 -> "32分钟前"
            in 31 * 60 * 1000..30 * 60 * 1000 -> "31分钟前"
            in 30 * 60 * 1000..29 * 60 * 1000 -> "30分钟前"
            in 29 * 60 * 1000..28 * 60 * 1000 -> "29分钟前"
            in 28 * 60 * 1000..27 * 60 * 1000 -> "28分钟前"
            in 27 * 60 * 1000..26 * 60 * 1000 -> "27分钟前"
            in 26 * 60 * 1000..25 * 60 * 1000 -> "26分钟前"
            in 25 * 60 * 1000..24 * 60 * 1000 -> "25分钟前"
            in 24 * 60 * 1000..23 * 60 * 1000 -> "24分钟前"
            in 23 * 60 * 1000..22 * 60 * 1000 -> "23分钟前"
            in 22 * 60 * 1000..21 * 60 * 1000 -> "22分钟前"
            in 21 * 60 * 1000..20 * 60 * 1000 -> "21分钟前"
            in 20 * 60 * 1000..19 * 60 * 1000 -> "20分钟前"
            in 19 * 60 * 1000..18 * 60 * 1000 -> "19分钟前"
            in 18 * 60 * 1000..17 * 60 * 1000 -> "18分钟前"
            in 17 * 60 * 1000..16 * 60 * 1000 -> "17分钟前"
            in 16 * 60 * 1000..15 * 60 * 1000 -> "16分钟前"
            in 15 * 60 * 1000..14 * 60 * 1000 -> "15分钟前"
            in 14 * 60 * 1000..13 * 60 * 1000 -> "14分钟前"
            in 13 * 60 * 1000..12 * 60 * 1000 -> "13分钟前"
            in 12 * 60 * 1000..11 * 60 * 1000 -> "12分钟前"
            in 11 * 60 * 1000..10 * 60 * 1000 -> "11分钟前"
            in 10 * 60 * 1000..9 * 60 * 1000 -> "10分钟前"
            in 9 * 60 * 1000..8 * 60 * 1000 -> "9分钟前"
            in 8 * 60 * 1000..7 * 60 * 1000 -> "8分钟前"
            in 7 * 60 * 1000..6 * 60 * 1000 -> "7分钟前"
            in 6 * 60 * 1000..5 * 60 * 1000 -> "6分钟前"
            in 5 * 60 * 1000..4 * 60 * 1000 -> "5分钟前"
            in 4 * 60 * 1000..3 * 60 * 1000 -> "4分钟前"
            in 3 * 60 * 1000..2 * 60 * 1000 -> "3分钟前"
            in 2 * 60 * 1000..1 * 60 * 1000 -> "2分钟前"
            in 1 * 60 * 1000..59 * 1000 -> "1分钟前"
            in 23 * 60 * 60 * 1000..22 * 60 * 60 * 1000 -> "23小时前"
            in 22 * 60 * 60 * 1000..21 * 60 * 60 * 1000 -> "22小时前"
            in 21 * 60 * 60 * 1000..20 * 60 * 60 * 1000 -> "21小时前"
            in 20 * 60 * 60 * 1000..19 * 60 * 60 * 1000 -> "20小时前"
            in 19 * 60 * 60 * 1000..18 * 60 * 60 * 1000 -> "19小时前"
            in 18 * 60 * 60 * 1000..17 * 60 * 60 * 1000 -> "18小时前"
            in 17 * 60 * 60 * 1000..16 * 60 * 60 * 1000 -> "17小时前"
            in 16 * 60 * 60 * 1000..15 * 60 * 60 * 1000 -> "16小时前"
            in 15 * 60 * 60 * 1000..14 * 60 * 60 * 1000 -> "15小时前"
            in 14 * 60 * 60 * 1000..13 * 60 * 60 * 1000 -> "14小时前"
            in 13 * 60 * 60 * 1000..12 * 60 * 60 * 1000 -> "13小时前"
            in 12 * 60 * 60 * 1000..11 * 60 * 60 * 1000 -> "12小时前"
            in 11 * 60 * 60 * 1000..10 * 60 * 60 * 1000 -> "11小时前"
            in 10 * 60 * 60 * 1000..9 * 60 * 60 * 1000 -> "10小时前"
            in 9 * 60 * 60 * 1000..8 * 60 * 60 * 1000 -> "9小时前"
            in 8 * 60 * 60 * 1000..7 * 60 * 60 * 1000 -> "8小时前"
            in 7 * 60 * 60 * 1000..6 * 60 * 60 * 1000 -> "7小时前"
            in 6 * 60 * 60 * 1000..5 * 60 * 60 * 1000 -> "6小时前"
            in 5 * 60 * 60 * 1000..4 * 60 * 60 * 1000 -> "5小时前"
            in 4 * 60 * 60 * 1000..3 * 60 * 60 * 1000 -> "4小时前"
            in 3 * 60 * 60 * 1000..2 * 60 * 60 * 1000 -> "3小时前"
            in 2 * 60 * 60 * 1000..1 * 60 * 60 * 1000 -> "2小时前"
            in 10 * 24 * 60 * 60 * 1000..9 * 24 * 60 * 60 * 1000 -> "10天前"
            in 9 * 24 * 60 * 60 * 1000..8 * 24 * 60 * 60 * 1000 -> "9天前"
            in 8 * 24 * 60 * 60 * 1000..7 * 24 * 60 * 60 * 1000 -> "8天前"
            in 7 * 24 * 60 * 60 * 1000..6 * 24 * 60 * 60 * 1000 -> "7天前"
            in 6 * 24 * 60 * 60 * 1000..5 * 24 * 60 * 60 * 1000 -> "6天前"
            in 5 * 24 * 60 * 60 * 1000..4 * 24 * 60 * 60 * 1000 -> "5天前"
            in 4 * 24 * 60 * 60 * 1000..3 * 24 * 60 * 60 * 1000 -> "4天前"
            in 3 * 24 * 60 * 60 * 1000..2 * 24 * 60 * 60 * 1000 -> "3天前"
            in 2 * 24 * 60 * 60 * 1000..1 * 24 * 60 * 60 * 1000 -> "2天前"
            in 1 * 24 * 60 * 60 * 1000..24 * 60 * 60 * 1000 -> "1天前"
            else -> {
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                return formatter.format(Date())
            }
        }
    } catch (e: NumberFormatException) {
        return ""
    }


}

/**
 * String  -> Int
 */
fun String?.strToInt(): Int {
    return if (this.isNullOrEmpty()) {
        0
    } else {
        this.toInt()
    }
}

/**
 * String  -> Float
 */
fun String?.strToFloat(): Float {
    return if (this.isNullOrEmpty()) {
        0.0f
    } else {
        this.toFloat()
    }
}

/**
 * String  -> Float
 */
fun String?.strToFloat2(): BigDecimal {
    return BigDecimal(this ?: "0")
        .setScale(2, BigDecimal.ROUND_DOWN)
}


/**
 * 获取非空值的内容
 */
fun String?.toStringNoNull(default: String = ""): String {
    return if ("null" == this) {
        default
    } else {
        this ?: default
    }
}

/**
 * String  -> Long
 */
fun String?.strToLong(): Long {
    return if (this.isNullOrEmpty()) {
        0
    } else {
        this.toLong()
    }
}

/**
 * String  -> Double
 */
fun String?.strToDouble(): Double {
    return if (this.isNullOrEmpty()) {
        0.0
    } else {
        this.toDouble()
    }
}

/**
 * String  -> Short
 */
fun String?.strToShort(): Short {
    return if (this.isNullOrEmpty()) {
        0
    } else {
        this.toShort()
    }
}

/**
 * String  -> Boolean
 */
fun String?.strToBoolean(): Boolean {
    return if (this.isNullOrEmpty()) {
        false
    } else {
        this.toBoolean()
    }
}

/**
 *  String -> 内容 为 null 或者 “null”
 */
fun String?.strToString(): String {
    return if (this == null || this == "null") {
        ""
    } else {
        this.toString()
    }
}

fun Long.timeParse(): String {
    var time = ""
    if (this > 1000) {
        time = timeParseMinute()
    } else {
        val minute = this / 60000
        val seconds = this % 60000
        val second = Math.round(seconds.toFloat() / 1000).toLong()
        if (minute < 10) {
            time += "0"
        }
        time += minute.toString() + ":"
        if (second < 10) {
            time += "0"
        }
        time += second
    }
    return time
}

@SuppressLint("SimpleDateFormat")
private fun Long.timeParseMinute(): String {
    return try {
        SimpleDateFormat("mm:ss").format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        "0:00"
    }
}

/**
 * 获取文件的ContentType
 */
fun File.getContentType(): String {
    var contentType: String? = null
    mapOf(
        Pair(".load", "text/html"),
        Pair(".123", "application/vnd.lotus-1-2-3"),
        Pair(".3ds", "image/x-3ds"),
        Pair(".3g2", "video/3gpp"),
        Pair(".3ga", "video/3gpp"),
        Pair(".3gp", "video/3gpp"),
        Pair(".3gpp", "video/3gpp"),
        Pair(".602", "application/x-t602"),
        Pair(".669", "audio/x-mod"),
        Pair(".7z", "application/x-7z-compressed"),
        Pair(".a", "application/x-archive"),
        Pair(".aac", "audio/mp4"),
        Pair(".abw", "application/x-abiword"),
        Pair(".abw.crashed", "application/x-abiword"),
        Pair(".abw.gz", "application/x-abiword"),
        Pair(".ac3", "audio/ac3"),
        Pair(".ace", "application/x-ace"),
        Pair(".adb", "text/x-adasrc"),
        Pair(".ads", "text/x-adasrc"),
        Pair(".afm", "application/x-font-afm"),
        Pair(".ag", "image/x-applix-graphics"),
        Pair(".ai", "application/illustrator"),
        Pair(".aif", "audio/x-aiff"),
        Pair(".aifc", "audio/x-aiff"),
        Pair(".aiff", "audio/x-aiff"),
        Pair(".al", "application/x-perl"),
        Pair(".alz", "application/x-alz"),
        Pair(".amr", "audio/amr"),
        Pair(".ani", "application/x-navi-animation"),
        Pair(".anim[1-9j]", "video/x-anim"),
        Pair(".anx", "application/annodex"),
        Pair(".ape", "audio/x-ape"),
        Pair(".arj", "application/x-arj"),
        Pair(".arw", "image/x-sony-arw"),
        Pair(".as", "application/x-applix-spreadsheet"),
        Pair(".asc", "text/plain"),
        Pair(".asf", "video/x-ms-asf"),
        Pair(".asp", "application/x-asp"),
        Pair(".ass", "text/x-ssa"),
        Pair(".asx", "audio/x-ms-asx"),
        Pair(".atom", "application/atom+xml"),
        Pair(".au", "audio/basic"),
        Pair(".avi", "video/x-msvideo"),
        Pair(".aw", "application/x-applix-word"),
        Pair(".awb", "audio/amr-wb"),
        Pair(".awk", "application/x-awk"),
        Pair(".axa", "audio/annodex"),
        Pair(".axv", "video/annodex"),
        Pair(".bak", "application/x-trash"),
        Pair(".bcpio", "application/x-bcpio"),
        Pair(".bdf", "application/x-font-bdf"),
        Pair(".bib", "text/x-bibtex"),
        Pair(".bin", "application/octet-stream"),
        Pair(".blend", "application/x-blender"),
        Pair(".blender", "application/x-blender"),
        Pair(".bmp", "image/bmp"),
        Pair(".bz", "application/x-bzip"),
        Pair(".bz2", "application/x-bzip"),
        Pair(".c", "text/x-csrc"),
        Pair(".c++", "text/x-c++src"),
        Pair(".cab", "application/vnd.ms-cab-compressed"),
        Pair(".cb7", "application/x-cb7"),
        Pair(".cbr", "application/x-cbr"),
        Pair(".cbt", "application/x-cbt"),
        Pair(".cbz", "application/x-cbz"),
        Pair(".cc", "text/x-c++src"),
        Pair(".cdf", "application/x-netcdf"),
        Pair(".cdr", "application/vnd.corel-draw"),
        Pair(".cer", "application/x-x509-ca-cert"),
        Pair(".cert", "application/x-x509-ca-cert"),
        Pair(".cgm", "image/cgm"),
        Pair(".chm", "application/x-chm"),
        Pair(".chrt", "application/x-kchart"),
        Pair(".class", "application/x-java"),
        Pair(".cls", "text/x-tex"),
        Pair(".cmake", "text/x-cmake"),
        Pair(".cpio", "application/x-cpio"),
        Pair(".cpio.gz", "application/x-cpio-compressed"),
        Pair(".cpp", "text/x-c++src"),
        Pair(".cr2", "image/x-canon-cr2"),
        Pair(".crt", "application/x-x509-ca-cert"),
        Pair(".crw", "image/x-canon-crw"),
        Pair(".cs", "text/x-csharp"),
        Pair(".csh", "application/x-csh"),
        Pair(".css", "text/css"),
        Pair(".cssl", "text/css"),
        Pair(".csv", "text/csv"),
        Pair(".cue", "application/x-cue"),
        Pair(".cur", "image/x-win-bitmap"),
        Pair(".cxx", "text/x-c++src"),
        Pair(".d", "text/x-dsrc"),
        Pair(".dar", "application/x-dar"),
        Pair(".dbf", "application/x-dbf"),
        Pair(".dc", "application/x-dc-rom"),
        Pair(".dcl", "text/x-dcl"),
        Pair(".dcm", "application/dicom"),
        Pair(".dcr", "image/x-kodak-dcr"),
        Pair(".dds", "image/x-dds"),
        Pair(".deb", "application/x-deb"),
        Pair(".der", "application/x-x509-ca-cert"),
        Pair(".desktop", "application/x-desktop"),
        Pair(".dia", "application/x-dia-diagram"),
        Pair(".diff", "text/x-patch"),
        Pair(".divx", "video/x-msvideo"),
        Pair(".djv", "image/vnd.djvu"),
        Pair(".djvu", "image/vnd.djvu"),
        Pair(".dng", "image/x-adobe-dng"),
        Pair(".doc", "application/msword"),
        Pair(".docbook", "application/docbook+xml"),
        Pair(".docm", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        Pair(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        Pair(".dot", "text/vnd.graphviz"),
        Pair(".dsl", "text/x-dsl"),
        Pair(".dtd", "application/xml-dtd"),
        Pair(".dtx", "text/x-tex"),
        Pair(".dv", "video/dv"),
        Pair(".dvi", "application/x-dvi"),
        Pair(".dvi.bz2", "application/x-bzdvi"),
        Pair(".dvi.gz", "application/x-gzdvi"),
        Pair(".dwg", "image/vnd.dwg"),
        Pair(".dxf", "image/vnd.dxf"),
        Pair(".e", "text/x-eiffel"),
        Pair(".egon", "application/x-egon"),
        Pair(".eif", "text/x-eiffel"),
        Pair(".el", "text/x-emacs-lisp"),
        Pair(".emf", "image/x-emf"),
        Pair(".emp", "application/vnd.emusic-emusic_package"),
        Pair(".ent", "application/xml-external-parsed-entity"),
        Pair(".eps", "image/x-eps"),
        Pair(".eps.bz2", "image/x-bzeps"),
        Pair(".eps.gz", "image/x-gzeps"),
        Pair(".epsf", "image/x-eps"),
        Pair(".epsf.bz2", "image/x-bzeps"),
        Pair(".epsf.gz", "image/x-gzeps"),
        Pair(".epsi", "image/x-eps"),
        Pair(".epsi.bz2", "image/x-bzeps"),
        Pair(".epsi.gz", "image/x-gzeps"),
        Pair(".epub", "application/epub+zip"),
        Pair(".erl", "text/x-erlang"),
        Pair(".es", "application/ecmascript"),
        Pair(".etheme", "application/x-e-theme"),
        Pair(".etx", "text/x-setext"),
        Pair(".exe", "application/x-ms-dos-executable"),
        Pair(".exr", "image/x-exr"),
        Pair(".ez", "application/andrew-inset"),
        Pair(".f", "text/x-fortran"),
        Pair(".f90", "text/x-fortran"),
        Pair(".f95", "text/x-fortran"),
        Pair(".fb2", "application/x-fictionbook+xml"),
        Pair(".fig", "image/x-xfig"),
        Pair(".fits", "image/fits"),
        Pair(".fl", "application/x-fluid"),
        Pair(".flac", "audio/x-flac"),
        Pair(".flc", "video/x-flic"),
        Pair(".fli", "video/x-flic"),
        Pair(".flv", "video/x-flv"),
        Pair(".flw", "application/x-kivio"),
        Pair(".fo", "text/x-xslfo"),
        Pair(".for", "text/x-fortran"),
        Pair(".g3", "image/fax-g3"),
        Pair(".gb", "application/x-gameboy-rom"),
        Pair(".gba", "application/x-gba-rom"),
        Pair(".gcrd", "text/directory"),
        Pair(".ged", "application/x-gedcom"),
        Pair(".gedcom", "application/x-gedcom"),
        Pair(".gen", "application/x-genesis-rom"),
        Pair(".gf", "application/x-tex-gf"),
        Pair(".gg", "application/x-sms-rom"),
        Pair(".gif", "image/gif"),
        Pair(".glade", "application/x-glade"),
        Pair(".gmo", "application/x-gettext-translation"),
        Pair(".gnc", "application/x-gnucash"),
        Pair(".gnd", "application/gnunet-directory"),
        Pair(".gnucash", "application/x-gnucash"),
        Pair(".gnumeric", "application/x-gnumeric"),
        Pair(".gnuplot", "application/x-gnuplot"),
        Pair(".gp", "application/x-gnuplot"),
        Pair(".gpg", "application/pgp-encrypted"),
        Pair(".gplt", "application/x-gnuplot"),
        Pair(".gra", "application/x-graphite"),
        Pair(".gsf", "application/x-font-type1"),
        Pair(".gsm", "audio/x-gsm"),
        Pair(".gtar", "application/x-tar"),
        Pair(".gv", "text/vnd.graphviz"),
        Pair(".gvp", "text/x-google-video-pointer"),
        Pair(".gz", "application/x-gzip"),
        Pair(".h", "text/x-chdr"),
        Pair(".h++", "text/x-c++hdr"),
        Pair(".hdf", "application/x-hdf"),
        Pair(".hh", "text/x-c++hdr"),
        Pair(".hp", "text/x-c++hdr"),
        Pair(".hpgl", "application/vnd.hp-hpgl"),
        Pair(".hpp", "text/x-c++hdr"),
        Pair(".hs", "text/x-haskell"),
        Pair(".htm", "text/html"),
        Pair(".html", "text/html"),
        Pair(".hwp", "application/x-hwp"),
        Pair(".hwt", "application/x-hwt"),
        Pair(".hxx", "text/x-c++hdr"),
        Pair(".ica", "application/x-ica"),
        Pair(".icb", "image/x-tga"),
        Pair(".icns", "image/x-icns"),
        Pair(".ico", "image/vnd.microsoft.icon"),
        Pair(".ics", "text/calendar"),
        Pair(".idl", "text/x-idl"),
        Pair(".ief", "image/ief"),
        Pair(".iff", "image/x-iff"),
        Pair(".ilbm", "image/x-ilbm"),
        Pair(".ime", "text/x-imelody"),
        Pair(".imy", "text/x-imelody"),
        Pair(".ins", "text/x-tex"),
        Pair(".iptables", "text/x-iptables"),
        Pair(".iso", "application/x-cd-image"),
        Pair(".iso9660", "application/x-cd-image"),
        Pair(".it", "audio/x-it"),
        Pair(".j2k", "image/jp2"),
        Pair(".jad", "text/vnd.sun.j2me.app-descriptor"),
        Pair(".jar", "application/x-java-archive"),
        Pair(".java", "text/x-java"),
        Pair(".jng", "image/x-jng"),
        Pair(".jnlp", "application/x-java-jnlp-file"),
        Pair(".jp2", "image/jp2"),
        Pair(".jpc", "image/jp2"),
        Pair(".jpe", "image/jpeg"),
        Pair(".jpeg", "image/jpeg"),
        Pair(".jpf", "image/jp2"),
        Pair(".jpg", "image/jpeg"),
        Pair(".jpr", "application/x-jbuilder-project"),
        Pair(".jpx", "image/jp2"),
        Pair(".js", "application/javascript"),
        Pair(".json", "application/json"),
        Pair(".jsonp", "application/jsonp"),
        Pair(".k25", "image/x-kodak-k25"),
        Pair(".kar", "audio/midi"),
        Pair(".karbon", "application/x-karbon"),
        Pair(".kdc", "image/x-kodak-kdc"),
        Pair(".kdelnk", "application/x-desktop"),
        Pair(".kexi", "application/x-kexiproject-sqlite3"),
        Pair(".kexic", "application/x-kexi-connectiondata"),
        Pair(".kexis", "application/x-kexiproject-shortcut"),
        Pair(".kfo", "application/x-kformula"),
        Pair(".kil", "application/x-killustrator"),
        Pair(".kino", "application/smil"),
        Pair(".kml", "application/vnd.google-earth.kml+xml"),
        Pair(".kmz", "application/vnd.google-earth.kmz"),
        Pair(".kon", "application/x-kontour"),
        Pair(".kpm", "application/x-kpovmodeler"),
        Pair(".kpr", "application/x-kpresenter"),
        Pair(".kpt", "application/x-kpresenter"),
        Pair(".kra", "application/x-krita"),
        Pair(".ksp", "application/x-kspread"),
        Pair(".kud", "application/x-kugar"),
        Pair(".kwd", "application/x-kword"),
        Pair(".kwt", "application/x-kword"),
        Pair(".la", "application/x-shared-library-la"),
        Pair(".latex", "text/x-tex"),
        Pair(".ldif", "text/x-ldif"),
        Pair(".lha", "application/x-lha"),
        Pair(".lhs", "text/x-literate-haskell"),
        Pair(".lhz", "application/x-lhz"),
        Pair(".log", "text/x-log"),
        Pair(".ltx", "text/x-tex"),
        Pair(".lua", "text/x-lua"),
        Pair(".lwo", "image/x-lwo"),
        Pair(".lwob", "image/x-lwo"),
        Pair(".lws", "image/x-lws"),
        Pair(".ly", "text/x-lilypond"),
        Pair(".lyx", "application/x-lyx"),
        Pair(".lz", "application/x-lzip"),
        Pair(".lzh", "application/x-lha"),
        Pair(".lzma", "application/x-lzma"),
        Pair(".lzo", "application/x-lzop"),
        Pair(".m", "text/x-matlab"),
        Pair(".m15", "audio/x-mod"),
        Pair(".m2t", "video/mpeg"),
        Pair(".m3u", "audio/x-mpegurl"),
        Pair(".m3u8", "audio/x-mpegurl"),
        Pair(".m4", "application/x-m4"),
        Pair(".m4a", "audio/mp4"),
        Pair(".m4b", "audio/x-m4b"),
        Pair(".m4v", "video/mp4"),
        Pair(".mab", "application/x-markaby"),
        Pair(".man", "application/x-troff-man"),
        Pair(".mbox", "application/mbox"),
        Pair(".md", "application/x-genesis-rom"),
        Pair(".mdb", "application/vnd.ms-access"),
        Pair(".mdi", "image/vnd.ms-modi"),
        Pair(".me", "text/x-troff-me"),
        Pair(".med", "audio/x-mod"),
        Pair(".metalink", "application/metalink+xml"),
        Pair(".mgp", "application/x-magicpoint"),
        Pair(".mid", "audio/midi"),
        Pair(".midi", "audio/midi"),
        Pair(".mif", "application/x-mif"),
        Pair(".minipsf", "audio/x-minipsf"),
        Pair(".mka", "audio/x-matroska"),
        Pair(".mkv", "video/x-matroska"),
        Pair(".ml", "text/x-ocaml"),
        Pair(".mli", "text/x-ocaml"),
        Pair(".mm", "text/x-troff-mm"),
        Pair(".mmf", "application/x-smaf"),
        Pair(".mml", "text/mathml"),
        Pair(".mng", "video/x-mng"),
        Pair(".mo", "application/x-gettext-translation"),
        Pair(".mo3", "audio/x-mo3"),
        Pair(".moc", "text/x-moc"),
        Pair(".mod", "audio/x-mod"),
        Pair(".mof", "text/x-mof"),
        Pair(".moov", "video/quicktime"),
        Pair(".mov", "video/quicktime"),
        Pair(".movie", "video/x-sgi-movie"),
        Pair(".mp+", "audio/x-musepack"),
        Pair(".mp2", "video/mpeg"),
        Pair(".mp3", "audio/mpeg"),
        Pair(".mp4", "video/mp4"),
        Pair(".mpc", "audio/x-musepack"),
        Pair(".mpe", "video/mpeg"),
        Pair(".mpeg", "video/mpeg"),
        Pair(".mpg", "video/mpeg"),
        Pair(".mpga", "audio/mpeg"),
        Pair(".mpp", "audio/x-musepack"),
        Pair(".mrl", "text/x-mrml"),
        Pair(".mrml", "text/x-mrml"),
        Pair(".mrw", "image/x-minolta-mrw"),
        Pair(".ms", "text/x-troff-ms"),
        Pair(".msi", "application/x-msi"),
        Pair(".msod", "image/x-msod"),
        Pair(".msx", "application/x-msx-rom"),
        Pair(".mtm", "audio/x-mod"),
        Pair(".mup", "text/x-mup"),
        Pair(".mxf", "application/mxf"),
        Pair(".n64", "application/x-n64-rom"),
        Pair(".nb", "application/mathematica"),
        Pair(".nc", "application/x-netcdf"),
        Pair(".nds", "application/x-nintendo-ds-rom"),
        Pair(".nef", "image/x-nikon-nef"),
        Pair(".nes", "application/x-nes-rom"),
        Pair(".nfo", "text/x-nfo"),
        Pair(".not", "text/x-mup"),
        Pair(".nsc", "application/x-netshow-channel"),
        Pair(".nsv", "video/x-nsv"),
        Pair(".o", "application/x-object"),
        Pair(".obj", "application/x-tgif"),
        Pair(".ocl", "text/x-ocl"),
        Pair(".oda", "application/oda"),
        Pair(".odb", "application/vnd.oasis.opendocument.database"),
        Pair(".odc", "application/vnd.oasis.opendocument.chart"),
        Pair(".odf", "application/vnd.oasis.opendocument.formula"),
        Pair(".odg", "application/vnd.oasis.opendocument.graphics"),
        Pair(".odi", "application/vnd.oasis.opendocument.image"),
        Pair(".odm", "application/vnd.oasis.opendocument.text-master"),
        Pair(".odp", "application/vnd.oasis.opendocument.presentation"),
        Pair(".ods", "application/vnd.oasis.opendocument.spreadsheet"),
        Pair(".odt", "application/vnd.oasis.opendocument.text"),
        Pair(".oga", "audio/ogg"),
        Pair(".ogg", "video/x-theora+ogg"),
        Pair(".ogm", "video/x-ogm+ogg"),
        Pair(".ogv", "video/ogg"),
        Pair(".ogx", "application/ogg"),
        Pair(".old", "application/x-trash"),
        Pair(".oleo", "application/x-oleo"),
        Pair(".opml", "text/x-opml+xml"),
        Pair(".ora", "image/openraster"),
        Pair(".orf", "image/x-olympus-orf"),
        Pair(".otc", "application/vnd.oasis.opendocument.chart-template"),
        Pair(".otf", "application/x-font-otf"),
        Pair(".otg", "application/vnd.oasis.opendocument.graphics-template"),
        Pair(".oth", "application/vnd.oasis.opendocument.text-web"),
        Pair(".otp", "application/vnd.oasis.opendocument.presentation-template"),
        Pair(".ots", "application/vnd.oasis.opendocument.spreadsheet-template"),
        Pair(".ott", "application/vnd.oasis.opendocument.text-template"),
        Pair(".owl", "application/rdf+xml"),
        Pair(".oxt", "application/vnd.openofficeorg.extension"),
        Pair(".p", "text/x-pascal"),
        Pair(".p10", "application/pkcs10"),
        Pair(".p12", "application/x-pkcs12"),
        Pair(".p7b", "application/x-pkcs7-certificates"),
        Pair(".p7s", "application/pkcs7-signature"),
        Pair(".pack", "application/x-java-pack200"),
        Pair(".pak", "application/x-pak"),
        Pair(".par2", "application/x-par2"),
        Pair(".pas", "text/x-pascal"),
        Pair(".patch", "text/x-patch"),
        Pair(".pbm", "image/x-portable-bitmap"),
        Pair(".pcd", "image/x-photo-cd"),
        Pair(".pcf", "application/x-cisco-vpn-settings"),
        Pair(".pcf.gz", "application/x-font-pcf"),
        Pair(".pcf.z", "application/x-font-pcf"),
        Pair(".pcl", "application/vnd.hp-pcl"),
        Pair(".pcx", "image/x-pcx"),
        Pair(".pdb", "chemical/x-pdb"),
        Pair(".pdc", "application/x-aportisdoc"),
        Pair(".pdf", "application/pdf"),
        Pair(".pdf.bz2", "application/x-bzpdf"),
        Pair(".pdf.gz", "application/x-gzpdf"),
        Pair(".pef", "image/x-pentax-pef"),
        Pair(".pem", "application/x-x509-ca-cert"),
        Pair(".perl", "application/x-perl"),
        Pair(".pfa", "application/x-font-type1"),
        Pair(".pfb", "application/x-font-type1"),
        Pair(".pfx", "application/x-pkcs12"),
        Pair(".pgm", "image/x-portable-graymap"),
        Pair(".pgn", "application/x-chess-pgn"),
        Pair(".pgp", "application/pgp-encrypted"),
        Pair(".php", "application/x-php"),
        Pair(".php3", "application/x-php"),
        Pair(".php4", "application/x-php"),
        Pair(".pict", "image/x-pict"),
        Pair(".pict1", "image/x-pict"),
        Pair(".pict2", "image/x-pict"),
        Pair(".pickle", "application/python-pickle"),
        Pair(".pk", "application/x-tex-pk"),
        Pair(".pkipath", "application/pkix-pkipath"),
        Pair(".pkr", "application/pgp-keys"),
        Pair(".pl", "application/x-perl"),
        Pair(".pla", "audio/x-iriver-pla"),
        Pair(".pln", "application/x-planperfect"),
        Pair(".pls", "audio/x-scpls"),
        Pair(".pm", "application/x-perl"),
        Pair(".png", "image/png"),
        Pair(".pnm", "image/x-portable-anymap"),
        Pair(".pntg", "image/x-macpaint"),
        Pair(".po", "text/x-gettext-translation"),
        Pair(".por", "application/x-spss-por"),
        Pair(".pot", "text/x-gettext-translation-template"),
        Pair(".ppm", "image/x-portable-pixmap"),
        Pair(".pps", "application/vnd.ms-powerpoint"),
        Pair(".ppt", "application/vnd.ms-powerpoint"),
        Pair(".pptm", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
        Pair(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
        Pair(".ppz", "application/vnd.ms-powerpoint"),
        Pair(".prc", "application/x-palm-database"),
        Pair(".ps", "application/postscript"),
        Pair(".ps.bz2", "application/x-bzpostscript"),
        Pair(".ps.gz", "application/x-gzpostscript"),
        Pair(".psd", "image/vnd.adobe.photoshop"),
        Pair(".psf", "audio/x-psf"),
        Pair(".psf.gz", "application/x-gz-font-linux-psf"),
        Pair(".psflib", "audio/x-psflib"),
        Pair(".psid", "audio/prs.sid"),
        Pair(".psw", "application/x-pocket-word"),
        Pair(".pw", "application/x-pw"),
        Pair(".py", "text/x-python"),
        Pair(".pyc", "application/x-python-bytecode"),
        Pair(".pyo", "application/x-python-bytecode"),
        Pair(".qif", "image/x-quicktime"),
        Pair(".qt", "video/quicktime"),
        Pair(".qtif", "image/x-quicktime"),
        Pair(".qtl", "application/x-quicktime-media-link"),
        Pair(".qtvr", "video/quicktime"),
        Pair(".ra", "audio/vnd.rn-realaudio"),
        Pair(".raf", "image/x-fuji-raf"),
        Pair(".ram", "application/ram"),
        Pair(".rar", "application/x-rar"),
        Pair(".ras", "image/x-cmu-raster"),
        Pair(".raw", "image/x-panasonic-raw"),
        Pair(".rax", "audio/vnd.rn-realaudio"),
        Pair(".rb", "application/x-ruby"),
        Pair(".rdf", "application/rdf+xml"),
        Pair(".rdfs", "application/rdf+xml"),
        Pair(".reg", "text/x-ms-regedit"),
        Pair(".rej", "application/x-reject"),
        Pair(".rgb", "image/x-rgb"),
        Pair(".rle", "image/rle"),
        Pair(".rm", "application/vnd.rn-realmedia"),
        Pair(".rmj", "application/vnd.rn-realmedia"),
        Pair(".rmm", "application/vnd.rn-realmedia"),
        Pair(".rms", "application/vnd.rn-realmedia"),
        Pair(".rmvb", "application/vnd.rn-realmedia"),
        Pair(".rmx", "application/vnd.rn-realmedia"),
        Pair(".roff", "text/troff"),
        Pair(".rp", "image/vnd.rn-realpix"),
        Pair(".rpm", "application/x-rpm"),
        Pair(".rss", "application/rss+xml"),
        Pair(".rt", "text/vnd.rn-realtext"),
        Pair(".rtf", "application/rtf"),
        Pair(".rtx", "text/richtext"),
        Pair(".rv", "video/vnd.rn-realvideo"),
        Pair(".rvx", "video/vnd.rn-realvideo"),
        Pair(".s3m", "audio/x-s3m"),
        Pair(".sam", "application/x-amipro"),
        Pair(".sami", "application/x-sami"),
        Pair(".sav", "application/x-spss-sav"),
        Pair(".scm", "text/x-scheme"),
        Pair(".sda", "application/vnd.stardivision.draw"),
        Pair(".sdc", "application/vnd.stardivision.calc"),
        Pair(".sdd", "application/vnd.stardivision.impress"),
        Pair(".sdp", "application/sdp"),
        Pair(".sds", "application/vnd.stardivision.chart"),
        Pair(".sdw", "application/vnd.stardivision.writer"),
        Pair(".sgf", "application/x-go-sgf"),
        Pair(".sgi", "image/x-sgi"),
        Pair(".sgl", "application/vnd.stardivision.writer"),
        Pair(".sgm", "text/sgml"),
        Pair(".sgml", "text/sgml"),
        Pair(".sh", "application/x-shellscript"),
        Pair(".shar", "application/x-shar"),
        Pair(".shn", "application/x-shorten"),
        Pair(".siag", "application/x-siag"),
        Pair(".sid", "audio/prs.sid"),
        Pair(".sik", "application/x-trash"),
        Pair(".sis", "application/vnd.symbian.install"),
        Pair(".sisx", "x-epoc/x-sisx-app"),
        Pair(".sit", "application/x-stuffit"),
        Pair(".siv", "application/sieve"),
        Pair(".sk", "image/x-skencil"),
        Pair(".sk1", "image/x-skencil"),
        Pair(".skr", "application/pgp-keys"),
        Pair(".slk", "text/spreadsheet"),
        Pair(".smaf", "application/x-smaf"),
        Pair(".smc", "application/x-snes-rom"),
        Pair(".smd", "application/vnd.stardivision.mail"),
        Pair(".smf", "application/vnd.stardivision.math"),
        Pair(".smi", "application/x-sami"),
        Pair(".smil", "application/smil"),
        Pair(".sml", "application/smil"),
        Pair(".sms", "application/x-sms-rom"),
        Pair(".snd", "audio/basic"),
        Pair(".so", "application/x-sharedlib"),
        Pair(".spc", "application/x-pkcs7-certificates"),
        Pair(".spd", "application/x-font-speedo"),
        Pair(".spec", "text/x-rpm-spec"),
        Pair(".spl", "application/x-shockwave-flash"),
        Pair(".spx", "audio/x-speex"),
        Pair(".sql", "text/x-sql"),
        Pair(".sr2", "image/x-sony-sr2"),
        Pair(".src", "application/x-wais-source"),
        Pair(".srf", "image/x-sony-srf"),
        Pair(".srt", "application/x-subrip"),
        Pair(".ssa", "text/x-ssa"),
        Pair(".stc", "application/vnd.sun.xml.calc.template"),
        Pair(".std", "application/vnd.sun.xml.draw.template"),
        Pair(".sti", "application/vnd.sun.xml.impress.template"),
        Pair(".stm", "audio/x-stm"),
        Pair(".stw", "application/vnd.sun.xml.writer.template"),
        Pair(".sty", "text/x-tex"),
        Pair(".sub", "text/x-subviewer"),
        Pair(".sun", "image/x-sun-raster"),
        Pair(".sv4cpio", "application/x-sv4cpio"),
        Pair(".sv4crc", "application/x-sv4crc"),
        Pair(".svg", "image/svg+xml"),
        Pair(".svgz", "image/svg+xml-compressed"),
        Pair(".swf", "application/x-shockwave-flash"),
        Pair(".sxc", "application/vnd.sun.xml.calc"),
        Pair(".sxd", "application/vnd.sun.xml.draw"),
        Pair(".sxg", "application/vnd.sun.xml.writer.global"),
        Pair(".sxi", "application/vnd.sun.xml.impress"),
        Pair(".sxm", "application/vnd.sun.xml.math"),
        Pair(".sxw", "application/vnd.sun.xml.writer"),
        Pair(".sylk", "text/spreadsheet"),
        Pair(".t", "text/troff"),
        Pair(".t2t", "text/x-txt2tags"),
        Pair(".tar", "application/x-tar"),
        Pair(".tar.bz", "application/x-bzip-compressed-tar"),
        Pair(".tar.bz2", "application/x-bzip-compressed-tar"),
        Pair(".tar.gz", "application/x-compressed-tar"),
        Pair(".tar.lzma", "application/x-lzma-compressed-tar"),
        Pair(".tar.lzo", "application/x-tzo"),
        Pair(".tar.xz", "application/x-xz-compressed-tar"),
        Pair(".tar.z", "application/x-tarz"),
        Pair(".tbz", "application/x-bzip-compressed-tar"),
        Pair(".tbz2", "application/x-bzip-compressed-tar"),
        Pair(".tcl", "text/x-tcl"),
        Pair(".tex", "text/x-tex"),
        Pair(".texi", "text/x-texinfo"),
        Pair(".texinfo", "text/x-texinfo"),
        Pair(".tga", "image/x-tga"),
        Pair(".tgz", "application/x-compressed-tar"),
        Pair(".theme", "application/x-theme"),
        Pair(".themepack", "application/x-windows-themepack"),
        Pair(".tif", "image/tiff"),
        Pair(".tiff", "image/tiff"),
        Pair(".tk", "text/x-tcl"),
        Pair(".tlz", "application/x-lzma-compressed-tar"),
        Pair(".tnef", "application/vnd.ms-tnef"),
        Pair(".tnf", "application/vnd.ms-tnef"),
        Pair(".toc", "application/x-cdrdao-toc"),
        Pair(".torrent", "application/x-bittorrent"),
        Pair(".tpic", "image/x-tga"),
        Pair(".tr", "text/troff"),
        Pair(".ts", "application/x-linguist"),
        Pair(".tsv", "text/tab-separated-values"),
        Pair(".tta", "audio/x-tta"),
        Pair(".ttc", "application/x-font-ttf"),
        Pair(".ttf", "application/x-font-ttf"),
        Pair(".ttx", "application/x-font-ttx"),
        Pair(".txt", "text/plain"),
        Pair(".txz", "application/x-xz-compressed-tar"),
        Pair(".tzo", "application/x-tzo"),
        Pair(".ufraw", "application/x-ufraw"),
        Pair(".ui", "application/x-designer"),
        Pair(".uil", "text/x-uil"),
        Pair(".ult", "audio/x-mod"),
        Pair(".uni", "audio/x-mod"),
        Pair(".uri", "text/x-uri"),
        Pair(".url", "text/x-uri"),
        Pair(".ustar", "application/x-ustar"),
        Pair(".vala", "text/x-vala"),
        Pair(".vapi", "text/x-vala"),
        Pair(".vcf", "text/directory"),
        Pair(".vcs", "text/calendar"),
        Pair(".vct", "text/directory"),
        Pair(".vda", "image/x-tga"),
        Pair(".vhd", "text/x-vhdl"),
        Pair(".vhdl", "text/x-vhdl"),
        Pair(".viv", "video/vivo"),
        Pair(".vivo", "video/vivo"),
        Pair(".vlc", "audio/x-mpegurl"),
        Pair(".vob", "video/mpeg"),
        Pair(".voc", "audio/x-voc"),
        Pair(".vor", "application/vnd.stardivision.writer"),
        Pair(".vst", "image/x-tga"),
        Pair(".wav", "audio/x-wav"),
        Pair(".wax", "audio/x-ms-asx"),
        Pair(".wb1", "application/x-quattropro"),
        Pair(".wb2", "application/x-quattropro"),
        Pair(".wb3", "application/x-quattropro"),
        Pair(".wbmp", "image/vnd.wap.wbmp"),
        Pair(".wcm", "application/vnd.ms-works"),
        Pair(".wdb", "application/vnd.ms-works"),
        Pair(".webm", "video/webm"),
        Pair(".wk1", "application/vnd.lotus-1-2-3"),
        Pair(".wk3", "application/vnd.lotus-1-2-3"),
        Pair(".wk4", "application/vnd.lotus-1-2-3"),
        Pair(".wks", "application/vnd.ms-works"),
        Pair(".wma", "audio/x-ms-wma"),
        Pair(".wmf", "image/x-wmf"),
        Pair(".wml", "text/vnd.wap.wml"),
        Pair(".wmls", "text/vnd.wap.wmlscript"),
        Pair(".wmv", "video/x-ms-wmv"),
        Pair(".wmx", "audio/x-ms-asx"),
        Pair(".wp", "application/vnd.wordperfect"),
        Pair(".wp4", "application/vnd.wordperfect"),
        Pair(".wp5", "application/vnd.wordperfect"),
        Pair(".wp6", "application/vnd.wordperfect"),
        Pair(".wpd", "application/vnd.wordperfect"),
        Pair(".wpg", "application/x-wpg"),
        Pair(".wpl", "application/vnd.ms-wpl"),
        Pair(".wpp", "application/vnd.wordperfect"),
        Pair(".wps", "application/vnd.ms-works"),
        Pair(".wri", "application/x-mswrite"),
        Pair(".wrl", "model/vrml"),
        Pair(".wv", "audio/x-wavpack"),
        Pair(".wvc", "audio/x-wavpack-correction"),
        Pair(".wvp", "audio/x-wavpack"),
        Pair(".wvx", "audio/x-ms-asx"),
        Pair(".x3f", "image/x-sigma-x3f"),
        Pair(".xac", "application/x-gnucash"),
        Pair(".xbel", "application/x-xbel"),
        Pair(".xbl", "application/xml"),
        Pair(".xbm", "image/x-xbitmap"),
        Pair(".xcf", "image/x-xcf"),
        Pair(".xcf.bz2", "image/x-compressed-xcf"),
        Pair(".xcf.gz", "image/x-compressed-xcf"),
        Pair(".xhtml", "application/xhtml+xml"),
        Pair(".xi", "audio/x-xi"),
        Pair(".xla", "application/vnd.ms-excel"),
        Pair(".xlc", "application/vnd.ms-excel"),
        Pair(".xld", "application/vnd.ms-excel"),
        Pair(".xlf", "application/x-xliff"),
        Pair(".xliff", "application/x-xliff"),
        Pair(".xll", "application/vnd.ms-excel"),
        Pair(".xlm", "application/vnd.ms-excel"),
        Pair(".xls", "application/vnd.ms-excel"),
        Pair(".xlsm", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        Pair(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        Pair(".xlt", "application/vnd.ms-excel"),
        Pair(".xlw", "application/vnd.ms-excel"),
        Pair(".xm", "audio/x-xm"),
        Pair(".xmf", "audio/x-xmf"),
        Pair(".xmi", "text/x-xmi"),
        Pair(".xml", "application/xml"),
        Pair(".xpm", "image/x-xpixmap"),
        Pair(".xps", "application/vnd.ms-xpsdocument"),
        Pair(".xsl", "application/xml"),
        Pair(".xslfo", "text/x-xslfo"),
        Pair(".xslt", "application/xml"),
        Pair(".xspf", "application/xspf+xml"),
        Pair(".xul", "application/vnd.mozilla.xul+xml"),
        Pair(".xwd", "image/x-xwindowdump"),
        Pair(".xyz", "chemical/x-pdb"),
        Pair(".xz", "application/x-xz"),
        Pair(".w2p", "application/w2p"),
        Pair(".z", "application/x-compress"),
        Pair(".zabw", "application/x-abiword"),
        Pair(".zip", "application/zip"),
        Pair(".zoo", "application/x-zoo")
    ).forEach { entry ->
        if (this.name.endsWith(entry.key)) {
            contentType = entry.value
            return@forEach
        }
    }
    return contentType ?: "application/octet-stream"
}


/**
 * 获取网络连接中的文件名称
 */
fun String.getUrlFileName(): String {
    val start = lastIndexOf("/")
    return if (start != -1)
        substring(start + 1)
    else
        ""
}


/**
 * 显示条形的内容
 * @param bCBitmap 已生成的条形码的位图
 * @param content  条形码包含的内容
 * @return 返回生成的新位图,它是 方法[.createQRCode]返回的位图与新绘制文本content的组合
 */
private fun showContent(bCBitmap: Bitmap?, content: String): Bitmap? {
    if (TextUtils.isEmpty(content) || null == bCBitmap) {
        return null
    }
    val paint = Paint()
    paint.color = Color.BLACK
    paint.isAntiAlias = true
    paint.style = Paint.Style.FILL//设置填充样式
    paint.textSize = 20f
    //        paint.setTextAlign(Paint.Align.CENTER);
    //测量字符串的宽度
    val textWidth = paint.measureText(content).toInt()
    val fm = paint.fontMetrics
    //绘制字符串矩形区域的高度
    val textHeight = (fm.bottom - fm.top).toInt()
    // x 轴的缩放比率
    val scaleRateX = (bCBitmap.width / textWidth).toFloat()
    paint.textScaleX = scaleRateX
    //绘制文本的基线
 //   val baseLine = bCBitmap.height + textHeight
    //创建一个图层，然后在这个图层上绘制bCBitmap、content
    val bitmap = Bitmap.createBitmap(bCBitmap.width, bCBitmap.height + 2 * textHeight, Bitmap.Config.ARGB_4444)
    val canvas = Canvas()
    canvas.drawColor(Color.WHITE)
    canvas.setBitmap(bitmap)
    canvas.drawBitmap(bCBitmap, 0f, 0f, null)
//    canvas.drawText(content, (bCBitmap.width / 10).toFloat(), baseLine.toFloat(), paint)
    canvas.save()
    canvas.restore()
    return bitmap
}


fun String?.strIsEmpty(): Boolean {
    return this.isNullOrEmpty() || "null" == this
}

fun String?.mStr(): String {
    return if (this.isNullOrEmpty()) "" else this
}

/**
 * 从Manifest中获取meta-data值
 * @param context
 * @param key
 * @return
 */
fun getMetaData(context: Context, i: Int, key: String): String {
    var value: String? = null
    try {
        val appInfo = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )
        value = appInfo.metaData.getString(key)
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    if (!TextUtils.isEmpty(value)) {
        if (i == 1) {
            return when (value) {
                "JCT001" -> "360"
                "JCT002" -> "百度应"
                "JCT003" -> "阿里系"
                "JCT004" -> "应用宝"
                "JCT005" -> "应用汇"
                "JCT006" -> "小米"
                "JCT007" -> "华为"
                "JCT008" -> "邮箱"
                "JCT009" -> "OPPO"
                "JCT010" -> "三星"
                "JCT012" -> "vivo"
                else -> "魅族"
            }
        } else {
            return value!!
        }
    } else {
        return ""
    }
}