import com.dajie.push.client.PushClient;

/**
 * Created by wills on 3/25/14.
 */
public class TestPushClient {

    public static void main(String[] args) {
        String apiKey="test_api_key";
        String secretKey="test_secret_key";
        String appId="appId1";
        PushClient pushClient=PushClient.getInstance(appId,apiKey,secretKey,"dev");
        boolean result=pushClient.sendPush("1010","lalalala");

        System.out.println(result);

    }
}
