package com.xlebnick.kitties.data.model


/**
 * A helper class to avoid boilerplate. It is designed to represent the state of API requests
 * and can be used to pass to UI model for correct state displaying
 */
sealed class RequestStatus<out Result, out Error> {
    object Idle : RequestStatus<Nothing, Nothing>()
    object Loading : RequestStatus<Nothing, Nothing>()
    class Success<Result>(val result: Result) : RequestStatus<Result, Nothing>()
    class Error<Error>(val errorReason: Error) : RequestStatus<Nothing, Error>()
}