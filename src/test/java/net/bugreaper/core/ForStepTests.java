package net.bugreaper.core;

import io.qameta.allure.Step;


class TestStepNoEnv {

    @Step("Step with {}")
    public static String stepNoEnv(String data){
        return data + "2";
    }

}


class TestStepWrongEnv {


    @Step("Step with {data2}")
    public static String stepWrongEnv(String data){
        return data + "1";
    }

}

