package com.example.adobe_exp_batch_status_android

class LoginPresenter(var fragmentCallback: LoginContract.LoginFragmentInterface) : LoginContract.LoginPresenterInterface {

    val loginModel = LoginModel(this)

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