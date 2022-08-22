package com.playverse.data.api

import com.playverse.data.models.*
import com.playverse.data.other.Constants
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Subham on 7/11/2022.
 */
interface ApiService {

    @Headers("Client-Type: APP")
    @GET("admin/game/{gameid}")
    suspend fun getBannerInfo(@Path("gameid") gameid: String): Response<GameBannerAndInfoResponse>

    @GET(Constants.Login.VERSION_DETAIL)
    suspend fun versionDetails() : Response<VersionUpdate>


    @Headers("Client-Type: APP")
    @GET("admin/game")
    suspend fun getGameListing(
        @Query("status") status: String,
        @Query("size") size: String
    ): Response<DepositListingResponse>

    @Headers("Client-Type: APP")
    @GET("admin/tournament")
    suspend fun getTournamentListInfo(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("status") status: Int,
        @Query("sortOrder") sortOrder: String
    ): Response<TournamentListingResponse>


    @Headers("Client-Type: APP")
    @GET("admin/tournament/{tournamentId}/getRewardsAndLeaderboard")
    suspend fun getRewardsAndLeader(
        @Path("tournamentId") gameid: String,
        @Query("userId") user_id: String
    ): Response<LeaderAndRewardsResponse>

    /*@Headers("Client-Type: APP")
    @GET("admin/game/{gameid}/continuePlaying?")
    suspend fun getRecentGames(
        @Path("gameid") gameid: String,
        @Query("userId") userId: String
    ): Response<RecentGameResponse>*/

    @Headers("Client-Type: APP")
    @GET("admin/game/gameplay")
    suspend fun getFreePlayListInfo(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<FreePlayListingResponse>

    @POST("user/logout")
    suspend fun logout( @Body requestBody: LogoutRequestBody
    ): Response<LogoutResponse>
}