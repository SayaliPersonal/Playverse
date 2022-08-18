package com.playverse.data.api

import com.playverse.data.models.*
import com.playverse.data.other.Constants
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UserBaseService {
    @GET(Constants.Login.LOGIN_PATH)
    suspend fun login(
        @Query("number") number: String
    ): Response<MainOtpResponse>

    @POST(Constants.Login.SIGNUP_PATH)
    suspend fun signUp(
        @Query("number") number: String
    ): Response<MainOtpResponse>

    @GET(Constants.Login.VERIFY_OTP)
    suspend fun verifyOtp(
        @Query("number") number: String,
        @Query("otp") otp: String,
        @Query("type") type : String
    ) : Response<MainOtpResponse>

    @Multipart
    @POST(Constants.Login.SETUP_AVATAR_USERNAME)
    suspend fun setUpAvatarUsername(
        @Part image: MultipartBody.Part,
        @Query("userId") userId: Int,
        @Query("username") username: String
    ) : Response<AvatarResponse.MainResponse>


    @PUT(Constants.Login.SETUP_AVATAR_USERNAME)
    suspend fun updateProfileName(
        @Query("userId") userId: Int,
        @Query("username") username: String
    ) : Response<AvatarResponse.MainResponse>

    @Multipart
    @PUT(Constants.Login.SETUP_AVATAR_USERNAME)
    suspend fun updateProfilePic(
        @Part image: MultipartBody.Part,
        @Query("userId") userId: Int,
    ) : Response<AvatarResponse.MainResponse>

    @GET(Constants.Login.FETCH_USER_DETAILS)
    suspend fun fetchUserDetails(
        @Query("userId") userId: Int,
        ) : Response<ProfileMainResponse>




    companion object {
        fun create(): UserBaseService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_LOGIN)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserBaseService::class.java)
        }
    }
}