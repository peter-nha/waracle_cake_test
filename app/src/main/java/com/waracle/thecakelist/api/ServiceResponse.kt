package com.waracle.thecakelist.api

sealed class ServiceResponse<T> {
    class Success<T>(val data: T) : ServiceResponse<T>()
    class Failure<T>(val message: String): ServiceResponse<T>()
}