package com.example.adobe_exp_batch_status_android.Login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.navigation.fragment.findNavController
import com.example.adobe_exp_batch_status_android.R


class LoginFragment : Fragment(), LoginContract.LoginFragmentInterface {

    val CLIENT_ID = "client_id"
    val CLIENT_SECRET = "client_secret"
    val ORG_ID = "org_id"
    val SUB = "sub"

    lateinit var adobeWebsiteLink: TextView
    lateinit var clientIdEditText: EditText
    lateinit var clientSecretEditText: EditText
    lateinit var organizationIdEditText: EditText
    lateinit var technicalAccountIdEditText: EditText
    lateinit var loginButton: Button
    lateinit var presenter: LoginContract.LoginPresenterInterface
    lateinit var sharedPreferences: SharedPreferences


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

        sharedPreferences = activity!!.getSharedPreferences("aep_shared_preferences", Context.MODE_PRIVATE)
        val clientId = sharedPreferences.getString(CLIENT_ID, "")
        val clientSecret = sharedPreferences.getString(CLIENT_SECRET, "")
        val orgId = sharedPreferences.getString(ORG_ID, "")
        val techId = sharedPreferences.getString(SUB, "")

        if (!clientId.isNullOrEmpty()) {
            clientIdEditText.setText(clientId)
        }

        if (!clientSecret.isNullOrEmpty()) {
            clientSecretEditText.setText(clientSecret)
        }

        if (!orgId.isNullOrEmpty()) {
            organizationIdEditText.setText(orgId)
        }

        if (!techId.isNullOrEmpty()) {
            technicalAccountIdEditText.setText(techId)
        }

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
            val secret = resources.getString(R.string.secret)
            val apiKey = resources.getString(R.string.api_key)

            presenter.login(clientId, clientSecret, orgId, techAccountId, secret, apiKey)
        }
    }

    override fun loginSuccessful() {
        print("login successful")
        sharedPreferences.edit().apply {
            putString(CLIENT_ID, clientIdEditText.text.toString())
            putString(CLIENT_SECRET, clientSecretEditText.text.toString())
            putString(ORG_ID, organizationIdEditText.text.toString())
            putString(SUB, technicalAccountIdEditText.text.toString())
        }.apply()

//        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_loginFragment_to_datasetsFragment)
    }

    override fun loginFailed() {
        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
    }
}
