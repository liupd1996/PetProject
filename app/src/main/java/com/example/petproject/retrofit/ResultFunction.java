package com.example.petproject.retrofit;


import com.example.petproject.bean.RemoteResult;

import io.reactivex.functions.Predicate;

public class ResultFunction implements Predicate<RemoteResult> {
    @Override
    public boolean test(RemoteResult t) throws Exception {
        if (!t.code.equals("000000")) {
            throw new ResultException(1, t.msg); //过滤被观察者发送的事件，如果返回true则发送事件，否则不会发送 couldn't load library
        }
        return true;
    }
}
