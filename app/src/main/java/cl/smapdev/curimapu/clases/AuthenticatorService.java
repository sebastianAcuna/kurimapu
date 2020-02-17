package cl.smapdev.curimapu.clases;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class AuthenticatorService extends Service {

    private Authenticator mAuthenticator;

    // Instance field that stores the authenticator object
    @Override
    public void onCreate() {

        // Create a new authenticator object
        mAuthenticator = new Authenticator(this);
    }


    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     * https://academiaandroid.com/ejemplo-uso-sync-adapter-en-proyecto-android/
     * https://github.com/aamatte/tutorial-sync-adapter/wiki/Content-provider
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }


}
