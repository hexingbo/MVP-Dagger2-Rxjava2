package com.zenglb.framework.dragger;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import javax.inject.Inject;

/**
 *
 * Created by zlb on 2017/8/24.
 */
public class AAA {
    private String a1;

    @Inject
    public AAA() {

    }

    public String getA1() {
        return a1;
    }

    public void setA1(String a1) {
        this.a1 = a1;
    }

    public void doSomething(Context context,String str){
        Toast.makeText(context,"doSomething "+str,Toast.LENGTH_SHORT).show();
    }

}
