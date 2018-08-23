package org.hackru.oneapp.hackru.ui.main.announcements

import java.util.*

class MessageParser {
    companion object {

        fun stringParser(string: String?) : String {

            var isEmoji = false
            val st = StringTokenizer(string)
            val buffer = StringBuffer()
            val specChar = " !#$%&'()*+,-./;<=>?@[]^_`{|}~3DXoOpPb<>"
            val num = "1234567890"

            while (st.hasMoreTokens()) {
                val word = st.nextToken()
                val strArr = word.split("".toRegex())

                if (word.indexOf('<') != -1 && word.indexOf('>') != -1 ||
                        word.indexOf(':') != -1 && word.lastIndexOf(':',
                                startIndex = word.length - 1) != -1) {
                            isEmoji = true
                }

                /** to check if it's emoticons */
                else {
                    for(i in 0 until word.length) {
                        if (strArr[i].contains(specChar)) {
                            isEmoji = true
                        } else {
                            isEmoji = false
                            break
                        }
                    }
                    if(!isEmoji) {
                        buffer.append("$word ")
                    }
                }
                if(strArr[0].contains(num)) {   /** <---to check if the word is (time) */
                    buffer.append("$word ")
                }
            }

            return buffer.toString()
        }
    }
}