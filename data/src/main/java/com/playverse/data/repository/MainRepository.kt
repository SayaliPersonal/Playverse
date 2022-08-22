package com.playverse.data.repository

import android.content.Context
import com.playverse.data.api.ApiHelper
import com.playverse.data.api.UserBaseService
import com.playverse.data.models.*
import com.playverse.data.util.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Created by Subham on 7/11/2022.
 */
class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val service: UserBaseService,
    @ApplicationContext appContext: Context
) {
    val context: Context = appContext

    suspend fun getBannerInfo(gameId: String) = apiHelper.getBannerInfo(context, gameId = gameId)

    suspend fun getDepositGameListing(status: String, size: String) =
        apiHelper.getDepositList(context, size = size, status = status)

    suspend fun getRecentGames(gameId: String, userId: String) =
        apiHelper.getRecentGames(gameId = gameId, userId = userId, context = context)

    suspend fun getRewardsAndLeaderBoard(gameId: String, user_id: String) =
        apiHelper.getRewardsAndLeader(gameId = gameId, context = context, user_id = user_id)


    suspend fun signUp(number: String): Flow<Result<MainOtpResponse>> {
        return apiHelper.signUp(context, number)
    }

    //verify otp
    suspend fun verifyOtp(
        number: String,
        otp: String,
        type: String
    ): Flow<Result<MainOtpResponse>> {
        return apiHelper.verifyOtp(context, number, otp, type)
    }

    //setup avatar and username
    suspend fun setUpAvatarUsername(
        image: MultipartBody.Part,
        user_id: Int,
        username: String
    ): Flow<Result<AvatarResponse.MainResponse>> {
        return apiHelper.setUpAvatarUserName(context, image, user_id, username)
    }

    suspend fun updateProfileName(
        user_id: Int,
        username: String
    ): Flow<Result<AvatarResponse.MainResponse>> {
        return apiHelper.updateProfileName(context, user_id, username)
    }

    suspend fun updateProfilePic(
        image: MultipartBody.Part,
        user_id: Int
    ): Flow<Result<AvatarResponse.MainResponse>> {
        return apiHelper.updateProfilePic(context, image, user_id)
    }

    suspend fun login(number: String): Flow<Result<MainOtpResponse>> {
        return apiHelper.login(context, number)
    }

    suspend fun fetchUserDetails(userId: Int): Flow<Result<ProfileMainResponse>> {
        return apiHelper.fetchUserDetails(context, userId)
    }


    suspend fun getTournamentListInfo(page: Int, size: Int, status: Int, sortOrder: String) =
        apiHelper.getTournamentListInfo(context, page, size, status, sortOrder)



    suspend fun getFreePlayListInfo(page: Int, size: Int) =
        apiHelper.getFreePlayListInfo(context, page, size)

    suspend fun fetchProfileTournaments(userId: Int): Flow<Result<BaseApiResponse<List<ProfileTournamentResponse>>>> {
        return apiHelper.fetchProfileTournaments(context, userId)
    }

    suspend fun fetchCashBalance(userId: Int): Flow<Result<BaseApiResponse<MyFundsResponse>>> {
        return apiHelper.fetchCashBalance(context, userId)
    }

    /* suspend fun tournamentBuyIn(gameId: Int,entreFee: Int,tournamentId :Int, userId: Int) =
         apiHelper.tournamentBuyIn(context, gameId,entreFee,tournamentId, userId)*/

    suspend fun addCashBalance(addCashRequestBody: AddCashRequestBody): Flow<Result<AddCashResponse>> {
        return apiHelper.addCashBalance(context, addCashRequestBody)!!
    }

    suspend fun withdrawCash(withdrawCashRequestBody: WithdrawCashRequestBody): Flow<Result<WithdrawCashResponse>> {
        return apiHelper.withdrawCash(context, withdrawCashRequestBody)!!
    }


    suspend fun logout(logoutRequestBody: LogoutRequestBody) =
        apiHelper.logout(context, logoutRequestBody = logoutRequestBody)
}