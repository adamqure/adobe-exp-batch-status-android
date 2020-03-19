package com.example.adobe_exp_batch_status_android.Login

class LoginContract {
    interface LoginPresenterInterface {
        fun login(clientId: String, clientSecret: String, orgId: String, techAccountId: String)
        fun loginSuccessful()
        fun loginFailed()
    }

    interface LoginFragmentInterface {
        fun loginSuccessful()
        fun loginFailed()
    }

    interface LoginModelInterface {
        fun login(clientId: String, clientSecret: String, orgId: String, techAccountId: String)
    }
}