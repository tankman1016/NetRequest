package com.lin.netrequestdemo.data;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 加密请求参数
 */

public class EncryptParamInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Map<String, String> mapForJson = new ArrayMap<>();

        if (request.method().equals("GET")) {

            for (int i = 0; i < request.url().querySize(); i++) {

                mapForJson.put(request.url().queryParameterName(i), request.url().queryParameterValue(i));

            }
        } else if (request.method().equals("POST") && (request.body() instanceof FormBody)) {
            FormBody oldFormBody = (FormBody) request.body();
            for (int i = 0; i < oldFormBody.size(); i++) {
                mapForJson.put(oldFormBody.name(i), oldFormBody.value(i));
            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(mapForJson);

        String pwdText = null;

        try {
            //这里是你自己的加密方式(和后台人员商定)
            // pwdText = AESUtil.encrypt(json);
            pwdText = "****************";

        } catch (Exception e) {
            e.printStackTrace();
        }

        //防止服务器"+"被编码成空格
        if (!TextUtils.isEmpty(pwdText)) {
            pwdText = pwdText.replace("+", "%2B");
        }

        FormBody.Builder bodyBuilder = new FormBody.Builder();
        FormBody formBody;

        if (TextUtils.isEmpty(pwdText)) {
            formBody = bodyBuilder
                    .addEncoded("param", "")
                    .build();
        } else {
            formBody = bodyBuilder
                    .addEncoded("param", handlePwdTextXx(pwdText))
                    .build();
        }

        Request newRequest = request.newBuilder()
                .post(formBody)
                .build();

        return chain.proceed(newRequest);
    }

    /**
     * 处理密文中的0x 0X
     * 防止解密失败
     */

    private static String handlePwdTextXx(String pwdText) {

        if (pwdText.contains("0x")) {
            pwdText = pwdText.replace("0x", "0&#120;");
        }
        if (pwdText.contains("0X")) {
            pwdText = pwdText.replace("0X", "0&#88;");
        }
        return pwdText;

    }
}
