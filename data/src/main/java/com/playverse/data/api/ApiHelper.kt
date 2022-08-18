package com.playverse.data.api

import android.content.Context
import com.playverse.data.models.*
import com.playverse.data.models.AvatarResponse
import com.playverse.data.models.GameBannerAndInfoResponse
import com.playverse.data.models.TournamentListingResponse
import com.playverse.data.models.MainOtpResponse
import com.playverse.data.models.ProfileMainResponse
import com.playverse.data.util.Result
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

/**
 * Created by Subham on 7/11/2022.
 */
interface ApiHelper {

    suspend fun getBannerInfo(
        context: Context,
        gameId: String
    ): Flow<Result<GameBannerAndInfoResponse>>?

    suspend fun getDepositList(
        context: Context,
        status: String,
        size: String
    ): Flow<Result<DepositListingResponse>>?

    suspend fun getTournamentListInfo(
        context: Context,
        page : Int,
        size : Int,
        status: Int,
        sortOrder: String
    ): Flow<Result<TournamentListingResponse>>?

    suspend fun getRecentGames(
        context: Context,
        gameId: String,
        userId: String
    ): Flow<Result<RecentGameResponse>>?


    suspend fun getRewardsAndLeader(
        context: Context,
        gameId: String,
        user_id : String
    ): Flow<Result<LeaderAndRewardsResponse>>?

    suspend fun setUpAvatarUserName(
        context: Context,
        image: MultipartBody.Part,
        userId: Int,
        username: String
    ): Flow<Result<AvatarResponse.MainResponse>>

    suspend fun updateProfileName(
        context: Context,
        userId: Int,
        username: String
    ): Flow<Result<AvatarResponse.MainResponse>>

    suspend fun updateProfilePic(
        context: Context,
        image: MultipartBody.Part,
        userId: Int
    ): Flow<Result<AvatarResponse.MainResponse>>

    suspend fun verifyOtp(
        context: Context,
        number: String,
        otp: String,
        type: String
    ): Flow<Result<MainOtpResponse>>

    suspend fun login(context: Context, number: String): Flow<Result<MainOtpResponse>>

    suspend fun signUp(context: Context, number: String): Flow<Result<MainOtpResponse>>

    suspend fun fetchUserDetails(context: Context, userId: Int): Flow<Result<ProfileMainResponse>>

    suspend fun fetchProfileTournaments(
        context: Context,
        userId: Int
    ): Flow<Result<BaseApiResponse<List<ProfileTournamentResponse>>>>

    suspend fun getFreePlayListInfo(context: Context, page : Int, size : Int): Flow<Result<FreePlayListingResponse>>?

    suspend fun fetchSessionId(
        context: Context,
        sessionRequestBody: SessionRequestBody
    ): Flow<Result<SessionIdResponse>>?

    suspend fun tournamentBuyIn(
        context: Context,
        buyInResponse: BuyinPopUpResponse
    ): Flow<Result<TournamentBuyInResponse>>?

    suspend fun tournamentRegisteration(
        context: Context,
        gameId: Int,
        tournamentId: Int,
        userId: Int
    ): Flow<Result<TournamentRegistered>>?

    suspend fun fetchCashBalance(
        context: Context,
        userId: Int
    ): Flow<Result<BaseApiResponse<MyFundsResponse>>>

    suspend fun addCashBalance(
        context: Context,
        addCashRequestBody: AddCashRequestBody
    ): Flow<Result<AddCashResponse>>?

    suspend fun withdrawCash(
        context: Context,
        withdrawCashRequestBody: WithdrawCashRequestBody
    ): Flow<Result<WithdrawCashResponse>>?

    suspend fun logout(
        context: Context,
        logoutRequestBody: LogoutRequestBody
    ): Flow<Result<LogoutResponse>>?
}