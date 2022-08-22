package com.playverse.data.other

/**
 * Created by Subham on 7/11/2022.
 */
object Constants {

    const val BASE_URL = "https://api-dev.rumbleapp.gg/api/v1/"

    const val BASE_URL_LOGIN = "https://api-dev.rumbleapp.gg/api/v1/"

    const val BASE_URL_TRANSCATION = "https://api-dev.rumbleapp.gg/api/v1/"

    object Login {
        const val LOGIN_PATH = "user/mobile/login"
        const val SIGNUP_PATH = "user/mobile/signUp"
        const val VERIFY_OTP = "user/mobile/verify"
        const val SETUP_AVATAR_USERNAME = "user/mobile/userDetails"
        const val FETCH_USER_DETAILS = "user/mobile/user"
        const val VERSION_DETAIL = "transaction/app/update"
    }

    object ProfileTournaments {
        const val TOURNAMENTS = "transaction/tournament/myTournaments"
    }

    object TournamentBuyIn {
        const val TOURNAMENT_BUYIN = "transaction/tournament/buyIn"
    }

    object TournamentRegistered {
        const val TOURNAMENT_REGISTERED = "transaction/tournament/status"
    }

    object MyFunds{
        const val CASH_BALANCE = "transaction/user/funds/cashBalance"
    }

    object AddCash{
        const val ADD_CASH = "transaction/user/cash/add"
    }

    object WithdrawCash{
        const val WITHDRAW_CASH = "transaction/user/cash/withdraw"
    }

}