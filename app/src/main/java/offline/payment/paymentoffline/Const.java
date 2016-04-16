package offline.payment.paymentoffline;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This is a file created by sunny on 4/16/16 for PaymentOffline
 * Contact sunny via sunny@mogara.org for cooperation.
 */
public class Const {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String BASE_URL = "http://heckpsi.com:8080";
}
