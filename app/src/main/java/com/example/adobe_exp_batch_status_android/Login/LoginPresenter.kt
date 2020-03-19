package com.example.adobe_exp_batch_status_android.Login

import com.example.adobe_exp_batch_status_android.Login.LoginContract
import com.example.adobe_exp_batch_status_android.Login.LoginModel

class LoginPresenter(var fragmentCallback: LoginContract.LoginFragmentInterface) : LoginContract.LoginPresenterInterface {

    val loginModel =
        LoginModel(this)

    override fun login(
        clientId: String,
        clientSecret: String,
        orgId: String,
        techAccountId: String
    ) {
        loginModel.login(clientId, clientSecret, orgId, techAccountId)
    }

    override fun loginSuccessful() {
        fragmentCallback.loginSuccessful()
    }

    override fun loginFailed() {
        fragmentCallback.loginFailed()
    }

}