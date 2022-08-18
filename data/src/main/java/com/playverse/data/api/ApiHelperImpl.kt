package com.playverse.data.api

import android.content.Context
import com.playverse.data.models.AvatarResponse
import com.playverse.data.models.GameBannerAndInfoResponse
import com.playverse.data.models.TournamentListingResponse
import com.playverse.data.models.MainOtpResponse
import com.playverse.data.models.ProfileMainResponse
import com.playverse.data.models.RecentGameResponse
import com.playverse.data.models.*
import com.playverse.data.util.Result
import com.playverse.data.util.getDataFromNetwork
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import javax.inject.Inject

/**
 * Created by Subham on 7/11/2022.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService,
    private val userBaseService: UserBaseService,
    private val recentGameBaseService: RecentGameBaseService
) : ApiHelper {

    override suspend fun getBannerInfo(
        context: Context,
        gameId: String
    ): Flow<Result<GameBannerAndInfoResponse>>? {
        return getDataFromNetwork({
            apiService.getBannerInfo(gameid = gameId)
        }, context)
    }

    override suspend fun getDepositList(
        context: Context,
        status: String,
        size: String
    ): Flow<Result<DepositListingResponse>>? {
        return getDataFromNetwork({
            apiService.getGameListing(status = status, size = size)
        }, context)
    }

    override suspend fun getTournamentListInfo(
        context: Context,
        page: Int,
        size: Int,
        status: Int,
        sortOrder: String
    ): Flow<Result<TournamentListingResponse>>? {
        return getDataFromNetwork({
            apiService.getTournamentListInfo(page, size, status, sortOrder)
        }, context)
    }

    override suspend fun getRecentGames(
        context: Context,
        gameId: String,
        userId: String
    ): Flow<Result<RecentGameResponse>>? {
        return getDataFromNetwork({
            recentGameBaseService.getRecentGame(gameId = gameId, userId = userId)
        }, context)
    }

    override suspend fun getRewardsAndLeader(
        context: Context,
        gameId: String,
        user_id: String
    ): Flow<Result<LeaderAndRewardsResponse>>? {
        return getDataFromNetwork({
            apiService.getRewardsAndLeader(gameid = gameId, user_id = user_id)
        }, context)
    }


    override suspend fun setUpAvatarUserName(
        context: Context,
        image: MultipartBody.Part,
        userId: Int,
        username: String
    ): Flow<Result<AvatarResponse.MainResponse>> {
        return getDataFromNetwork(
            {
                userBaseService.setUpAvatarUsername(image, userId, username)
            }, context
        )
    }

    override suspend fun updateProfileName(
        context: Context,
        userId: Int,
        username: String
    ): Flow<Result<AvatarResponse.MainResponse>> {
        return getDataFromNetwork(
            {
                userBaseService.updateProfileName(userId, username)
            }, context
        )
    }

    override suspend fun updateProfilePic(
        context: Context,
        image: MultipartBody.Part,
        userId: Int
    ): Flow<Result<AvatarResponse.MainResponse>> {
        return getDataFromNetwork(
            {
                userBaseService.updateProfilePic(image, userId)
            }, context
        )
    }

    override suspend fun verifyOtp(
        context: Context,
        number: String,
        otp: String,
        type: String
    ): Flow<Result<MainOtpResponse>> {
        return getDataFromNetwork(
            {
                userBaseService.verifyOtp(number, otp, type)
            }, context
        )
    }

    override suspend fun signUp(
        context: Context,
        number: String
    ): Flow<Result<MainOtpResponse>> {
        return getDataFromNetwork(
            {
                userBaseService.signUp(number)
            }, context
        )
    }

    override suspend fun fetchUserDetails(
        context: Context,
        userId: Int
    ): Flow<Result<ProfileMainResponse>> {
        return getDataFromNetwork(
            {
                userBaseService.fetchUserDetails(userId)
            }, context
        )
    }


    override suspend fun fetchProfileTournaments(
        context: Context,
        userId: Int
    ): Flow<Result<BaseApiResponse<List<ProfileTournamentResponse>>>> {
        return getDataFromNetwork(
            {
                recentGameBaseService.fetchProfileTournaments(userId)
            }, context
        )
    }

    override suspend fun getFreePlayListInfo(
        context: Context,
        page: Int,
        size: Int
    ): Flow<Result<FreePlayListingResponse>>? {
        return getDataFromNetwork({
            apiService.getFreePlayListInfo(page, size)
        }, context)
    }

    override suspend fun tournamentBuyIn(
        context: Context,
        buyInResponse: BuyinPopUpResponse
    ): Flow<Result<TournamentBuyInResponse>>? {
        return getDataFromNetwork({
            recentGameBaseService.tournamentBuyIn(buyInResponse)
        }, context)
    }

    override suspend fun fetchSessionId(
        context: Context,
        sessionRequestBody: SessionRequestBody
    ): Flow<Result<SessionIdResponse>>? {
        return getDataFromNetwork(
            {
                recentGameBaseService.fetchSessionId(sessionRequestBody)
            }, context
        )
    }

    override suspend fun login(
        context: Context,
        number: String
    ): Flow<Result<MainOtpResponse>> {
        return getDataFromNetwork(
            {
                userBaseService.login(number)
            }, context
        )
    }

    override suspend fun tournamentRegisteration(
        context: Context,
        gameId: Int,
        tournamentId: Int,
        userId: Int
    ): Flow<Result<TournamentRegistered>>? {
        return getDataFromNetwork({
            recentGameBaseService.tournamentRegistered(
                gameId = gameId,
                tournamentId = tournamentId,
                userId = userId
            )
        }, context)
    }

    override suspend fun fetchCashBalance(
        context: Context,
        userId: Int
    ): Flow<Result<BaseApiResponse<MyFundsResponse>>> {
        return getDataFromNetwork(
            {
                recentGameBaseService.fetchCashBalance(userId)
            }, context
        )
    }

    override suspend fun addCashBalance(
        context: Context,
        addCashRequestBody: AddCashRequestBody
    ): Flow<Result<AddCashResponse>>? {
        return getDataFromNetwork({
            recentGameBaseService.addCash(addCashRequestBody)
        }, context)
    }

    override suspend fun withdrawCash(
        context: Context,
        withdrawCashRequestBody: WithdrawCashRequestBody
    ): Flow<Result<WithdrawCashResponse>>? {
        return getDataFromNetwork({
            recentGameBaseService.withdrawCash(withdrawCashRequestBody)
        }, context)
    }

    override suspend fun logout(
        context: Context,
        logoutRequestBody: LogoutRequestBody
    ): Flow<Result<LogoutResponse>>? {
        return getDataFromNetwork({
            apiService.logout(requestBody = logoutRequestBody)
        }, context)
    }
}

