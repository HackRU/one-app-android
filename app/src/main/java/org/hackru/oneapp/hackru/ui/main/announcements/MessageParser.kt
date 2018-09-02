package org.hackru.oneapp.hackru.ui.main.announcements

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.util.Linkify
import android.widget.TextView
import java.util.*
import android.graphics.Typeface
import android.text.style.StyleSpan


class MessageParser {
    companion object {

        fun stringParser(string: String?) : String {

            var isEmoji = false
            val st = StringTokenizer(string)
            val buffer = StringBuffer()
//            val specCharStr = " !#$%&'()*+,-./;<=>?@[]^_`{|}~3DXoOpPb<>"
            val specChar = "*&;-"
            val num = "1234567890"

            while (st.hasMoreTokens()) {
                val word = st.nextToken()
                val strArr = word.split("".toRegex())

                if (word.indexOf('<') != -1 && word.indexOf('>') != -1 && word.indexOf('!') != -1 || word.indexOf('@') != -1
                            || word.indexOf(':') != -1 && word.lastIndexOf(':',word.length-1) != -1){
                    isEmoji = true
                }
                /** to check if it's emoticons like */
                else{
                    for(i in 0 until word.length) {
                        isEmoji = strArr[i].contains(specChar)
                    }
                    if(!isEmoji) {
                        buffer.append("$word ")
                    }
                }
                if(word.indexOf('/') != -1 && word.indexOf('.') != -1){
                    buffer.append("$word ")
                }
                if(word.indexOf('*') != -1 && word.lastIndexOf('*',word.length-1) != -1){
                    val str = word.makePartialTextsBold()
                    buffer.append("$str ")
                }

                /** <---to check if the word is (time) */
                if(strArr[0].contains(num)) {
                    buffer.append("$word ")
                }
            }
            return buffer.toString()
        }

        private fun String.makePartialTextsBold(): SpannableStringBuilder {
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
                        setSpan(StyleSpan(Typeface.BOLD), start, length,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    copy = copy.removePrefix(next).removePrefix("*")
                }while (copy.isNotEmpty())
            }
        }

    }
}

//                val linkView = findViewById(R.id.noteview) as TextView
//                linkView.text = "www.google.com"
//                Linkify.addLinks(linkView, Linkify.ALL)