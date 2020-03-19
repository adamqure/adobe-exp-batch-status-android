package com.example.adobe_exp_batch_status_android.Login

import com.example.adobe_exp_batch_status_android.API
import com.example.adobe_exp_batch_status_android.Authentication
import com.example.adobe_exp_batch_status_android.ParameterClasses.AuthInfo
import com.example.adobe_exp_batch_status_android.ParameterClasses.AuthToken
import com.google.gson.Gson
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.interfaces.RSAPrivateKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import java.util.Base64.getDecoder


class LoginModel(var presenterCallback: LoginContract.LoginPresenterInterface) : LoginContract.LoginModelInterface {

    lateinit var authInfo: AuthInfo

    override fun login(
        clientId: String,
        clientSecret: String,
        orgId: String,
        techAccountId: String
    ) {
        val deserializer = Gson()
        val config = File("config.json")
        try {
            val text: String = Scanner(config).useDelimiter("\\A").next()
            authInfo = deserializer.fromJson<AuthInfo>(text, AuthInfo::class.java)
            authInfo.addAuthToken(AuthToken())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        createJwt(clientId, clientSecret, orgId, techAccountId)
    }

    fun createJwt(clientId: String, clientSecret: String, orgId: String, sub: String) { //Set a time for the JWT to expire, 10 minutes from the current time
        val exp = Date()
        exp.time = exp.time + 600000
        val bytes: ByteArray
        var privKey: RSAPrivateKey? = null
        try {
            var keyString: String = authInfo.getRsaKey()
            keyString = keyString.replace("\\s+", "")
            keyString = keyString.replace("\n", "")
            keyString = keyString.replace("-----BEGIN PRIVATE KEY-----", "")
            keyString = keyString.replace("-----END PRIVATE KEY-----", "")
            val factory: KeyFactory = KeyFactory.getInstance("RSA")
            bytes = getDecoder().decode(keyString)
            val keySpec: KeySpec = PKCS8EncodedKeySpec(bytes)
            privKey = factory.generatePrivate(keySpec) as RSAPrivateKey
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            println("Some error occured when attempting to get the Key factory for the RSAPrivateKey")
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
            println(
                "Invalid Key spec when attempting to create a JWT with your RSA private key. Please check your" +
                        "config file and RSA key"
            )
        }
        if (privKey == null) {
            println(
                "Something went wrong in the creation of the JSON Web Token (JWT) Most likely problem is" +
                        "a config file not formatted correctly or an incorrect RSA key formatting"
            )
        }
        val rs: SignatureAlgorithm = SignatureAlgorithm.RS256
        val metas: MutableMap<String, Any> = HashMap()
        metas["https://ims-na1.adobelogin.com/s/ent_dataservices_sdk"] = true
        var jwt: String = Jwts.builder()
            .setIssuer(orgId)
            .setExpiration(exp)
            .setSubject(sub)
            .setAudience("https://ims-na1.adobelogin.com/c/" + authInfo.apiKey)
            .addClaims(metas)
            .signWith(rs, privKey)
            .compact()
        val holder: ByteArray = jwt.toByteArray(StandardCharsets.ISO_8859_1)
        jwt = String(holder, StandardCharsets.UTF_8)
        authInfo.jwt = jwt
        exchangeJwtAuth(clientId)
    }

    fun exchangeJwtAuth(clientId: String) {
        val call: Call<AuthToken> = API.getAuthService()
            .getAuthToken(authInfo.apiKey, authInfo.clientSecret, authInfo.jwt)
        call.enqueue(object : Callback<AuthToken?> {
            override fun onResponse(
                call: Call<AuthToken?>?,
                response: Response<AuthToken?>
            ) {
                authInfo.addAuthToken(response.body())
                presenterCallback.loginSuccessful()
                println("EXCHANGED JWT: " + authInfo.accessToken)
                Authentication.authInfo = authInfo
            }

            override fun onFailure(call: Call<AuthToken?>?, t: Throwable?) {
                println("Error when exchanging JWT for Access Token")
                presenterCallback.loginFailed()
            }
        })
        while (authInfo.accessToken == "") {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

}