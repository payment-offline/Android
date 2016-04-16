package offline.payment.paymentoffline;

import com.squareup.okhttp.OkHttpClient;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * This is a file created by sunny on 4/16/16 for PaymentOffline
 * Contact sunny via sunny@mogara.org for cooperation.
 */
public class ChargeClient {

    private static  ChargeInterface sChargeInterface;

    public static ChargeInterface getChargeClient() {
        if (sChargeInterface == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(new OkHttpClient())
                    .baseUrl(Const.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            sChargeInterface = retrofit.create(ChargeInterface.class);
        }

        return sChargeInterface;
    }

    public interface ChargeInterface {
        @POST("/charge")
        Call<ChargeResponse> requestCharge(@Body ChargeRequestBody request);
    }
}
