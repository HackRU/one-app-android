package org.hackru.oneapp.hackru.api

class Resource<T> private constructor(val state: Int, val data: T?, val msg: String?){

    companion object {
        val LOADING = 0
        val SUCCESS = 1
        val FAILURE = 2

        fun <T> loading(data: T? = null) = Resource<T>(LOADING, data, null)
        // TODO: Figure out how I can avoid doing null tests on successful resource data
        fun <T> success(data: T) = Resource<T>(SUCCESS, data, null)
        fun <T> failure(msg: String, data: T? = null) = Resource<T>(FAILURE, data, msg)
    }

}