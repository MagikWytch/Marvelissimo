package io.magikwytch.marvel.network

import com.google.gson.GsonBuilder
import io.magikwytch.marvel.BuildConfig
import io.magikwytch.marvel.network.dto.character.CharacterDataWrapper
import io.magikwytch.marvel.network.dto.comic.ComicDataWrapper
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface MarvelApi {

    @GET("characters")
    fun getAllCharacters(@Query("offset") offset: Int = 0): Single<CharacterDataWrapper>

    @GET("characters/{characterId}")
    fun getCharacterById(@Path("characterId") characterId: Int): Single<CharacterDataWrapper>

    @GET("characters")
    fun getCharacterByNameStartsWith(@Query("nameStartsWith") nameStartsWith: String): Single<CharacterDataWrapper>

    @GET("comics")
    fun getAllComics(@Query("offset") offset: Int = 0): Single<ComicDataWrapper>

    @GET("comics/{comicId}")
    fun getComicById(@Path("comicId") comicId: Int): Single<ComicDataWrapper>

    @GET("comics")
    fun getComicByTitleStartsWith(@Query("titleStartsWith") nameStartsWith: String): Single<ComicDataWrapper>

    companion object {

        fun getService(): MarvelApi {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val publicApiKey: String = BuildConfig.PublicApiKey
            val privateApiKey: String = BuildConfig.PrivateApiKey

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url()
                val ts = (Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis / 1000L).toString()
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("apikey", publicApiKey)
                    .addQueryParameter("ts", ts)
                    .addQueryParameter("hash", "$ts$privateApiKey$publicApiKey".md5())
                    .build()

                chain.proceed(original.newBuilder().url(url).build())
            }

            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                .baseUrl("http://gateway.marvel.com/v1/public/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build()

            return retrofit.create<MarvelApi>(MarvelApi::class.java)
        }
    }
}