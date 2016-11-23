package com.example.ehsueh.appygolucky.OfflineOnline;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Maxwell on 2016-11-22.
 * A bound Service that instantiates the authenticator
 * when started.
 *
 * This code was taken directly from:
 * https://developer.android.com/training/sync-adapters/creating-authenticator.html
 */
public class AuthenticatorService extends Service {
    //...
    // Instance field that stores the authenticator object
    private AuthenticatorStub mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new AuthenticatorStub(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
