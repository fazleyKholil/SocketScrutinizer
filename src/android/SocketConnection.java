import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import android.util.Log;
import android.provider.Settings;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;

import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;



public class SocketConnection extends CordovaPlugin {
    
    public static final String TAG = "SocketConnection Plugin";
    public static final String MyPREFERENCES = "SocketConnection" ;
    ServerSocket serverSocket;
    private Thread getModelFromClient;
    private String ticketJSON;
    private Thread ticketThread = null;
    
    /**
     * Constructor.
     */
    public SocketConnection() {}

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.v(TAG,"Init SocketConnection");
    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if ("listen".equals(action)) {
            
            final int IP = args.getInt(0);
            
            Thread t = new Thread() {
                public void run() {

                    Socket socket = null;
                    ObjectInputStream objectInputStream = null;
                    try {
                        cordova.getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(cordova.getActivity().getApplicationContext(),"Initializing socket",Toast.LENGTH_LONG).show();
                            }
                        });

                        serverSocket = new ServerSocket(IP);
                        while (true) {
                            socket = serverSocket.accept();
                            objectInputStream = new ObjectInputStream(
                                    socket.getInputStream());

                            final String str = objectInputStream.readObject().toString();
                            
                            JSONObject jo = new JSONObject();
                            jo = new JSONObject();
                            jo.put("response", str);
                            PluginResult pluginResult = new  PluginResult(PluginResult.Status.OK,jo); 
                            pluginResult.setKeepCallback(true);
                            callbackContext.sendPluginResult(pluginResult);
                           
                            cordova.getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                 webView.loadUrl("javascript:var webWorker = 'Ticket received';console.log(webWorker);");
                            }
                        });
                        }
                    } catch (Exception e) {
                        try {
                            if (socket != null) socket.close();

                            if (objectInputStream != null) objectInputStream.close();

                        } catch (IOException exp) {
                            exp.printStackTrace();
                        }
                        e.printStackTrace();
                    } finally {

                        try {
                            if (socket != null) socket.close();

                            if (objectInputStream != null) objectInputStream.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            t.start();
            return true;
        }
        return false;
    }

}