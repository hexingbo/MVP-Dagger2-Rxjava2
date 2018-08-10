package debug;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.zenglb.framework.goodlife.R;

/**
 *
 *
 */
public class LauncherActivity extends AppCompatActivity {
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_layout);

//        //在这里传值给需要调试的Activity
//        Intent intent = new Intent(this, WebActivity.class);
////        intent.putExtra(BaseWebViewActivity.URL, "https://www.baidu.com");
//        intent.putExtra(BaseWebViewActivity.URL, "file:///android_asset/index.html");

//        startActivity(intent);
//        finish();

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-8621230724267558~7770389405");


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

}
