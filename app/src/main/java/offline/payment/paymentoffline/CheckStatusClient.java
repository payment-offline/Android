package offline.payment.paymentoffline;

import com.squareup.okhttp.OkHttpClient;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;

/**
 * This is a file created by sunny on 4/16/16 for PaymentOffline
 * Contact sunny via sunny@mogara.org for cooperation.
 */
public class CheckStatusClient {

    private static CheckStatusInterface sCheckStatusInterface;

    public static CheckStatusInterface getCheckStatusClient() {
        if (sCheckStatusInterface == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(new OkHttpClient())
                    .baseUrl(Const.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            sCheckStatusInterface = retrofit.create(CheckStatusInterface.class);
        }

        return sCheckStatusInterface;
    }

    public interface CheckStatusInterface {
        @GET("/status")
        Call<Status> getStatus();
    }
}
