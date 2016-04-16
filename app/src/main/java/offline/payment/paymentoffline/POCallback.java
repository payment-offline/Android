package offline.payment.paymentoffline;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * This is a file created by sunny on 4/16/16 for PaymentOffline
 * Contact sunny via sunny@mogara.org for cooperation.
 */
public abstract class POCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Response<T> response, Retrofit retrofit) {
        onCallback(new POResponse<>(response));
    }

    @Override
    public void onFailure(Throwable t) {
        POResponse<T> response = new POResponse<>(null);
        onCallback(response);
    }

    public abstract void onCallback(POResponse<T> response);
}
