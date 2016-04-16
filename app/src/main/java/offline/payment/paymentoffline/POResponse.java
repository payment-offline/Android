package offline.payment.paymentoffline;

import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import retrofit.Response;

/**
 * This is a file created by sunny on 4/16/16 for PaymentOffline
 * Contact sunny via sunny@mogara.org for cooperation.
 */
public class POResponse<T> {

    private final T mResult;
    private final ResponseBody mErrorBody;
    private Response<T> mResponse;

    public POResponse(Response<T> response) {
        mResponse = response;
        if (mResponse != null) {
            mResult = mResponse.body();
            mErrorBody = mResponse.errorBody();
        } else {
            mResult = null;
            mErrorBody = null;
        }
    }

    public boolean isSuccess() {
        return mResult != null;
    }

    public T getResult() {
        return mResult;
    }

    public String getErrorMessage() {
        if (mErrorBody != null) {
            try {
                return mErrorBody.string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
