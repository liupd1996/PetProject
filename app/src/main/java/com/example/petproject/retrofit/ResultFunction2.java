package com.example.petproject.retrofit;


import com.example.petproject.bean.RemoteResult;
import com.example.petproject.bean.RemoteResult2;

import io.reactivex.functions.Predicate;

public class ResultFunction2 implements Predicate<RemoteResult2> {
    @Override
    public boolean test(RemoteResult2 t) throws Exception {
        if (t.code != 1) {
            throw new ResultException(t.code, t.msg); //过滤被观察者发送的事件，如果返回true则发送事件，否则不会发送 couldn't load library
        }
        return true;
    }
}
