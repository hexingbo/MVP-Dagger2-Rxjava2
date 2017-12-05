package com.zenglb.framework.activity.architecture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.zenglb.framework.R;
import com.zenglb.framework.retrofit.core.HttpCall;
import com.zenglb.framework.retrofit.result.CustomWeatherResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 尝试使用 Android Architecture Components 来做一些事情
 *
 */
public class ArchitectureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_architecture);

        // Add product list fragment if this is first creation
        if (savedInstanceState == null) {
            //User ID:用户的标识符。最好使用Fragment的参数将此信息传递到Fragment中。如果Android操作系统回收了Fragment，则会保留此信息，以便下次重新启动应用时，该ID可用。
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, ArchitectureDemoFragment.newInstance("expired"), ArchitectureDemoFragment.TAG).commit();
        }


        test();

    }


    private void test(){
        String url = "http://localhost:8080/hello";
        Call<String> test = HttpCall.getApiService().getUserProfile(url);
        //上面的实现是非常的精巧  http://www.jianshu.com/p/c1a3a881a144

        test.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s = response.body();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ewwew","dasdad");
            }
        });
    }


}
