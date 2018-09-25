package org.hackru.oneapp.hackru.ui.main.announcements

import java.util.*

class MessageParser {
    companion object {

        fun stringParser(string: String?) : String {

            var isEmoji = false
            val st = StringTokenizer(string)
            val buffer = StringBuffer()
            val specChar = "&;"

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
                    word = word.replace(Regex("""[*]"""), "")
                    buffer.append("$word ")
                }
                if(word.indexOf('_') != -1 && word.lastIndexOf('_',word.length-1) != -1){
                    word = word.replace(Regex("""[_]"""), "")
                    buffer.append("$word ")
                }
                if(word.indexOf('`') != -1 && word.lastIndexOf('`',word.length-1) != -1 ||
                        word.indexOf('~') != -1 && word.lastIndexOf('~',word.length-1) != -1){
                    word = word.replace(Regex("""[`~]"""), "")
                    buffer.append("$word ")
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

    }

}