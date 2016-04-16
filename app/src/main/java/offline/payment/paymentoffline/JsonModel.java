package offline.payment.paymentoffline;

import java.io.Serializable;

/**
 * This is a file created by sunny on 4/16/16 for PaymentOffline
 * Contact sunny via sunny@mogara.org for cooperation.
 */
public class JsonModel implements Serializable {

    @Override
    public String toString() {
        return Const.GSON.toJson(this);
    }
}
