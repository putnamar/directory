package com.putnamnation.directory.api

import com.putnamnation.directory.model.User
import retrofit2.Call
import retrofit2.http.GET

//https://jsonplaceholder.typicode.com/users
interface DirectoryApi {
    @GET("/users")
    fun getUsers(): Call<List<User>>
}