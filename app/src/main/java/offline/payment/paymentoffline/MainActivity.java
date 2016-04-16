package offline.payment.paymentoffline;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pingplusplus.android.Pingpp;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.charge)
    Button chargeButton;

    @Bind(R.id.charge_input)
    EditText chargeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

        // Start the queue
        mRequestQueue.start();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
//
//    @Bind(R.id.charge)
//    Button chargeButton;

    String order_id;

    RequestQueue mRequestQueue;


    @OnClick(R.id.charge)
    public void charge() {
        if (chargeEdit.length() == 0) {
            Toast.makeText(MainActivity.this, "Please input amount!", Toast.LENGTH_SHORT).show();
        } else {
            int amount = Integer.parseInt(chargeEdit.getText().toString());
//            checkStatus();
//
//            ChargeRequestBody body = new ChargeRequestBody();
//            body.money = amount;
//            body.channel = "wx";
//            Call<ChargeResponse> call = ChargeClient.getChargeClient().requestCharge(body);
//
//            call.enqueue(new POCallback<ChargeResponse>() {
//                @Override
//                public void onCallback(POResponse<ChargeResponse> response) {
//                    if (response.isSuccess()) {
//                        ChargeResponse chargeResponse = response.getResult();
//                        response.toString();
//                        order_id = chargeResponse.getOrderId();
//                    }
//                    String error = response.getErrorMessage();
//
//                }
//            });

            requestCharging(amount, "wx");
        }
    }

    private void checkStatus() {
        Call<Status> call = CheckStatusClient.getCheckStatusClient().getStatus();
        call.enqueue(new POCallback<Status>() {
            @Override
            public void onCallback(POResponse<Status> response) {
                if (response.isSuccess()) {
                    Status status = response.getResult();
                    Log.d("Sunny", status.getStatus());
                }
            }
        });
    }

    private void requestCharging(final int money, final String channel) {
//        HttpUtil.get(Const.BASE_URL, "/status", new ArrayList<NameValuePair>(), new HttpCallbackListener() {
//            @Override
//            public void onFinished(String response) {
//                List<NameValuePair> body = new ArrayList<NameValuePair>();
//                body.add(new BasicNameValuePair("money", String.valueOf(money)));
//                body.add(new BasicNameValuePair("channel", channel));
//                HttpUtil.post(Const.BASE_URL, "/charge", body, new HttpCallbackListener() {
//                    @Override
//                    public void onFinished(String response) {
//                        Log.d("Sunny", response);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("money", money);
        params.put("channel", channel);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Const.BASE_URL + "/charge", new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            order_id = response.getString("order_no");
                            Log.e("eeeeeee", order_id);
                            final WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://heckpsi.com:8080/waiting/" + order_id)) {
                                @Override
                                public void onOpen(ServerHandshake handshakedata) {

                                }

                                @Override
                                public void onMessage(String message) {
                                    Log.e("nnnnnn", message);
                                    if (message == "{\"status\":\"charged\"}")
                                        this.close();
                                }

                                @Override
                                public void onClose(int code, String reason, boolean remote) {

                                }

                                @Override
                                public void onError(Exception ex) {

                                }
                            };
                            webSocketClient.connect();

                            Pingpp.createPayment(MainActivity.this, response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        mRequestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
