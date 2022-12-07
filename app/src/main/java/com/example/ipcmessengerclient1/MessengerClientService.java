package com.example.ipcmessengerclient1;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

public class MessengerClientService extends Service {
    public MessengerClientService() {
    }

    static final int FROM_CLIENT_TO_SERVER = 1;
    static final int FROM_SERVER_TO_CLIENT = 2;

    Messenger messenger;

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {

            Log.d("MymessengerClient", "Client : handling Message " + msg.what);

            switch (msg.what) {
                case FROM_SERVER_TO_CLIENT:

                    Log.d("MymessengerClient", "Client Receiver : " + msg.getData().getString("FROM_SERVER_TO_CLIENT"));

                    Message message = Message.obtain(null, FROM_CLIENT_TO_SERVER);
                    Bundle bundle = new Bundle();
                    bundle.putString("FROM_CLIENT_TO_SERVER", "Hey Server!, I Received Your Message: " + msg.getData().getString("FROM_SERVER_TO_CLIENT"));
                    message.setData(bundle);
                    try {
                        if (msg.replyTo != null)
                            msg.replyTo.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                default:
                    super.handleMessage(msg);
            }

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        messenger = new Messenger(new IncomingHandler());
        return messenger.getBinder();
    }
}