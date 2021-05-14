package com.wecode.multiplefmstations.radio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.wecode.multiplefmstations.data.network.responses.Radio;

import org.greenrobot.eventbus.EventBus;

public class RadioManager {

    private static RadioManager instance = null;

    private static RadioService service;

    private Context context;

    private boolean serviceBound;



    private static final String TAG = "RadioManager";

    private RadioManager(Context context) {
        Log.d(TAG, "RadioManager: constructer");
        this.context = context;
        serviceBound = false;
    }

    public static RadioManager with(Context context) {
        Log.d(TAG, "with: ");
        if (instance == null) instance = new RadioManager(context);


        return instance;
    }

    public static RadioService getService() {
        return service;
    }

    public boolean isPlaying(){
        return  service.isPlaying();
    }

    public void play(Radio radio) {
        service.play(radio);
    }

    public void playOrPause(Radio radio) {
        service.playOrPause(radio);
    }

    public void stopPlayer() {
        if (service != null)
            service.stopPlayer();
    }

    public void bind() {

        Intent intent = new Intent(context, RadioService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        if (service != null)
            EventBus.getDefault().post(service.getStatus());
    }

    public void unbind() {
        context.unbindService(serviceConnection);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder binder) {
            service = ((RadioService.LocalBinder) binder).getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serviceBound = false;
        }
    };

}
