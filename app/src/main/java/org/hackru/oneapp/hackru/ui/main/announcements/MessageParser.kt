package org.hackru.oneapp.hackru.ui.main.announcements

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.util.Linkify
import java.util.*
import android.graphics.Typeface
import android.text.style.StyleSpan
import android.text.SpannableString
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.rv_item_announcement.view.*
import org.hackru.oneapp.hackru.R


class MessageParser {
    companion object {

        fun stringParser(string: String?) : String {

            var isEmoji = false
            val st = StringTokenizer(string)
            val buffer = StringBuffer()
            val specChar = "&;"
            val num = "1234567890"

            while (st.hasMoreTokens()) {
                var word = st.nextToken()
                val strArr = word.split("".toRegex())

                if (word.indexOf('<') != -1 && word.indexOf('>') != -1 && word.indexOf('!') != -1 || word.indexOf('@') != -1
                            || word.indexOf(':') != -1 && word.lastIndexOf(':',word.length-1) != -1
                            || word.indexOf('*') != -1 && word.lastIndexOf('*',word.length-1) != -1
                            || word.indexOf('_') != -1 && word.lastIndexOf('_',word.length-1) != -1
                            || word.indexOf('~') != -1 && word.lastIndexOf('~',word.length-1) != -1
                            || word.indexOf('`') != -1 && word.lastIndexOf('`',word.length-1) != -1
                            || word.indexOf('&') != -1 && word.lastIndexOf(';',word.length-1) != -1){
                    isEmoji = true
                }

                else{
                    for(i in 0 until word.length) {
                        isEmoji = strArr[i].contains(specChar)
                    }
                    if(!isEmoji) {
                        buffer.append("$word ")
                    }
                }
                if(word.indexOf('*') != -1 && word.lastIndexOf('*',word.length-1) != -1){
                    val str = word.boldTexts()
                    buffer.append("$str ")
                }
                if(word.indexOf('_') != -1 && word.lastIndexOf('_',word.length-1) != -1){
                    val str = word.italicTexts()
                    buffer.append("$str ")
                }
                if(word.indexOf('`') != -1 && word.lastIndexOf('`',word.length-1) != -1){
                    val str = word.codeTexts()
                    buffer.append("$str ")
                }
                if(word.indexOf('~') != -1 && word.lastIndexOf('~',word.length-1) != -1){
                    val str = word.removeTexts()
                    buffer.append("$str ")
                }
                if(word.contains(specChar)){
                    word = word.replace(Regex("""[&;]"""), "")
                    buffer.append("$word ")
                }
                if(word.indexOf('/') != -1 && word.indexOf('.') != -1 &&
                        word.indexOf('<') != -1 && word.indexOf('>') != -1){
                    word = word.replace(Regex("""[<>#]"""), "")
                    buffer.append("$word ")
                }

                /** <---to check if the word is (time) */
                if(word.contains(Regex("""[0123456789]""")) && word.contains(':')) {
                    buffer.append("$word ")
                }
            }
            return buffer.toString()
        }

        private fun String.boldTexts(): SpannableStringBuilder {
            var copy = this
            return SpannableStringBuilder().apply {
                var setSpan = true
                var next: String
                do{
                    setSpan = !setSpan
                    next = if (length == 0) copy.substringBefore("*", "")
                    else copy.substringBefore("*")
                    val start = length
                    append(next)
                    if (setSpan) {
                        setSpan(StyleSpan(Typeface.BOLD), start, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    copy = copy.removePrefix(next).removePrefix("*")
                }while (copy.isNotEmpty())
            }
        }

        private fun String.italicTexts(): SpannableStringBuilder {
            var copy = this
            return SpannableStringBuilder().apply {
                var setSpan = true
                var next: String
                do{
                    setSpan = !setSpan
                    next = if (length == 0) copy.substringBefore("_", "")
                    else copy.substringBefore("_")
                    val start = length
                    append(next)
                    if (setSpan) {
                        setSpan(StyleSpan(Typeface.ITALIC), start, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    copy = copy.removePrefix(next).removePrefix("_")
                }while (copy.isNotEmpty())
            }
        }

        private fun String.codeTexts(): SpannableStringBuilder {
            var copy = this
            return SpannableStringBuilder().apply {
                var setSpan = true
                var next: String
                do{
                    setSpan = !setSpan
                    next = if (length == 0) copy.substringBefore("`", "")
                    else copy.substringBefore("`")
                    val start = length
                    append(next)
                    if (setSpan) {
                        setSpan(StyleSpan(Typeface.NORMAL), start, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    copy = copy.removePrefix(next).removePrefix("`")
                }while (copy.isNotEmpty())
            }
        }

        private fun String.removeTexts(): SpannableStringBuilder {
            var copy = this
            return SpannableStringBuilder().apply {
                var setSpan = true
                var next: String
                do{
                    setSpan = !setSpan
                    next = if (length == 0) copy.substringBefore("~", "")
                    else copy.substringBefore("~")
                    val start = length
                    append(next)
                    if (setSpan) {
                        setSpan(StyleSpan(Typeface.NORMAL), start, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    copy = copy.removePrefix(next).removePrefix("~")
                }while (copy.isNotEmpty())
            }
        }

    }

}