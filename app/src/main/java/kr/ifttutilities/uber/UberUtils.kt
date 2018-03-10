package kr.ifttutilities.uber

import com.uber.sdk.core.auth.Scope
import com.uber.sdk.rides.client.SessionConfiguration
import java.util.*

/**
 * Created by krishan on 08/03/18.
 */


object UberUtils {
    private const val redirectUri = "app://kr.iftt"
    // todo hide this secrets
    private const val clientId = "6RmHtzO5rvg1SbevhUo77odrEysAzCLK"
    const val accessToken = "KA.eyJ2ZXJzaW9uIjoyLCJpZCI6InIwVFZ2ZE81UWtPTDVNV2Fnd29pTGc9PSIsImV4cGlyZXNfYXQiOjE1MjMxODMyNjYsInBpcGVsaW5lX2tleV9pZCI6Ik1RPT0iLCJwaXBlbGluZV9pZCI6MX0.jr-llu1PshubppmIdwnxAp-Ng976uMEekiFdTezpNJA"

    @JvmStatic
    fun getUberSessionConfiguration(): SessionConfiguration {
        return SessionConfiguration.Builder()
                .setClientId(clientId)
                .setRedirectUri(redirectUri)
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .setScopes(Arrays.asList(Scope.PROFILE, Scope.REQUEST))
                .build()
    }

}

