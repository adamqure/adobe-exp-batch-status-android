package com.example.adobe_exp_batch_status_android.Login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.adobe_exp_batch_status_android.R


class LoginFragment : Fragment(), LoginContract.LoginFragmentInterface {
    lateinit var adobeWebsiteLink: TextView
    lateinit var clientIdEditText: EditText
    lateinit var clientSecretEditText: EditText
    lateinit var organizationIdEditText: EditText
    lateinit var technicalAccountIdEditText: EditText
    lateinit var loginButton: Button
    lateinit var presenter: LoginContract.LoginPresenterInterface


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adobeWebsiteLink = view.findViewById(R.id.adobe_website_button)
        clientIdEditText = view.findViewById(R.id.client_id_edit_text)
        clientSecretEditText = view.findViewById(R.id.client_secret_edit_text)
        organizationIdEditText = view.findViewById(R.id.organization_id_edit_text)
        technicalAccountIdEditText = view.findViewById(R.id.technical_account_id_edit_text)
        loginButton = view.findViewById(R.id.login_button)
        presenter =
            LoginPresenter(this)

        initializeWebsiteButton()
        initializeLoginButton()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initializeWebsiteButton() {
        adobeWebsiteLink.setOnClickListener {
            val url = "https://console.adobe.io/integrations"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    private fun initializeLoginButton() {
        loginButton.setOnClickListener {
            val clientId = clientIdEditText.text.toString()
            val clientSecret = clientSecretEditText.text.toString()
            val orgId = organizationIdEditText.text.toString()
            val techAccountId = technicalAccountIdEditText.text.toString()

            presenter.login(clientId, clientSecret, orgId, techAccountId)
        }
    }

    override fun loginSuccessful() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
    }

    override fun loginFailed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()

    }
}
