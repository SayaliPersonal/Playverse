package com.playverse.data.api

import com.playverse.data.models.*
import com.playverse.data.other.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RecentGameBaseService {

    @GET("transaction/game/continuePlaying?")
    suspend fun getRecentGame(
        @Query("gameId") gameId: String,
        @Query("userId") userId: String
    ): Response<RecentGameResponse>

    @GET(Constants.ProfileTournaments.TOURNAMENTS)
    suspend fun fetchProfileTournaments(
        @Query("userId") userId: Int
    ): Response<BaseApiResponse<List<ProfileTournamentResponse>>>

    @POST(Constants.TournamentBuyIn.TOURNAMENT_BUYIN)
    suspend fun tournamentBuyIn(@Body request: BuyinPopUpResponse): Response<TournamentBuyInResponse>



    @GET(Constants.TournamentRegistered.TOURNAMENT_REGISTERED)
    suspend fun tournamentRegistered(
        @Query("gameId") gameId: Int,
        @Query("tournamentId") tournamentId: Int,
        @Query("userId") userId: Int): Response<TournamentRegistered>

    @POST("transaction/game/gamePlayTransaction")
    suspend fun fetchSessionId(
        @Body sessionBody: SessionRequestBody
    ): Response<SessionIdResponse>

    @GET(Constants.MyFunds.CASH_BALANCE)
    suspend fun fetchCashBalance(
        @Query("userId") userId: Int
    ): Response<BaseApiResponse<MyFundsResponse>>

    @POST(Constants.AddCash.ADD_CASH)
    suspend fun addCash(
        @Body addCashRequest: AddCashRequestBody
    ): Response<AddCashResponse>

    @POST(Constants.WithdrawCash.WITHDRAW_CASH)
    suspend fun withdrawCash(
        @Body withdrawCashRequestBody: WithdrawCashRequestBody
    ): Response<WithdrawCashResponse>

    companion object {
        fun create(): RecentGameBaseService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TRANSCATION)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RecentGameBaseService::class.java)
        }
    }
}