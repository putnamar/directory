package com.putnamnation.directory.ui.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.putnamnation.directory.api.DirectoryApi
import com.putnamnation.directory.model.Location
import com.putnamnation.directory.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class UserViewModel : ViewModel() {
    private var isLoaded = false
    private val gson: Gson = getGson(true)
    private val restApi = createRestApi("https://jsonplaceholder.typicode.com/")
    var location: MutableLiveData<LatLng> = MutableLiveData(LatLng(0.0, 0.0))
        private set

    fun updateLocation(loc: Location) {
        location.postValue(LatLng(loc.lat ?: 0.0, loc.lng ?: 0.0))
    }

    val userList: MutableLiveData<List<User>> = MutableLiveData(emptyList())
        get() {
            if (!isLoaded) {
                loadData()
            }
            return field
        }

    private fun getGson(prettyPrint: Boolean): Gson {
        val builder = GsonBuilder()

        if (prettyPrint) {
            builder.setPrettyPrinting()
        }
        return builder.create()
    }

    private fun createRestApi(baseUrl: String): DirectoryApi? {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(DirectoryApi::class.java)
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            restApi?.getUsers()?.enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    userList.value = response.body() ?: emptyList()
                    isLoaded = true
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {

                }
            })
        }
    }
}

