# 基于Retrofit2和RxJava2的封装 
# 一切都为了整洁、简单、实用；

使用方法(很简单有木有)

```
  showLoading();
  Map<String, String> map = new ArrayMap<>();
  map.put("key1", "value1");
  map.put("key2", "value2");
  map.put("key3", "value3");
  addDispose(RxNet.request(ApiManager.getInstance().getCat(map), new RxNetCallBack<List<CatBean>>() {
         @Override
         public void onSuccess(List<CatBean> data) {
                hideLoading();
                showToast("获取信息成功" + data.get(0).toString());
         }

         @Override
            public void onFailure(String msg) {
                hideLoading();
                showToast(msg);
          }
   }));
```      
# 它是怎么实现的呢？

# (一)RxNet(有三种请求方式，很据需求可增加比如上传、下载):

1：返回数据带有body
```
{
code:"200",
msg:"请求成功",
pwd:"*******"
}
```
2：返回数据没有body
```
{
code:"200",
msg:"请求成功"
}
```

3：请求返回错误请求码（-1表示请求失败）
```
{
code:"201",
msg:"修改失败"
}

```

核心代码如下：

```
 /**
     * 一般请求，返回数据带有body
     */

    public static <T> Disposable request(Observable<BaseResponse<T>> observable, final RxNetCallBack<T> callBack) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, BaseResponse<T>>() {
                    @Override
                    public BaseResponse<T> apply(Throwable throwable) {

                        callBack.onFailure(ExceptionHandle.handleException(throwable));
                        return null;
                    }
                })
                .subscribe(new Consumer<BaseResponse<T>>() {
                    @Override
                    public void accept(BaseResponse<T> tBaseResponse) {
                        if (tBaseResponse.getCode().equals("200")) {
                            callBack.onSuccess(tBaseResponse.getData());

                        } else {
                            callBack.onFailure(tBaseResponse.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        AppLogUtil.printE("请求错误:" + throwable.getMessage());
                    }
                });
    }

    /**
     * 返回数据没有body
     */

    public static Disposable requestWithoutBody(Observable<BaseResponse> observable,
                                                final RxNetCallBack<String> callBack) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, BaseResponse>() {
                    @Override
                    public BaseResponse apply(Throwable throwable) {
                        callBack.onFailure(ExceptionHandle.handleException(throwable));
                        return null;
                    }
                })
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) {
                        if (baseResponse.getCode().equals("200")) {
                            callBack.onSuccess(baseResponse.getMsg());
                        } else {
                            callBack.onFailure(baseResponse.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        AppLogUtil.printE("请求错误:" + throwable.getMessage());
                    }
                });

    }

    /**
     * 请求返回错误请求码
     * -1表示请求失败
     */

    public static <T> Disposable requestForCode(Observable<BaseResponse<T>> observable, final RxNetCallBackForCode<T> callBack) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, BaseResponse<T>>() {
                    @Override
                    public BaseResponse<T> apply(Throwable throwable) {
                        // -1表示请求失败
                        callBack.onFailure("-1", ExceptionHandle.handleException(throwable));
                        return null;
                    }
                })
                .subscribe(new Consumer<BaseResponse<T>>() {
                    @Override
                    public void accept(BaseResponse<T> tBaseResponse) {
                        if (tBaseResponse.getCode().equals("200")) {
                            callBack.onSuccess(tBaseResponse.getData());
                        } else {
                            callBack.onFailure(tBaseResponse.getCode(), tBaseResponse.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        AppLogUtil.printE("请求错误:" + throwable.getMessage());
                    }
                });
    }

    /**
     * 下载和上传
     */
```

#（二）实现Retrofit2的全局请求单例 

```
public class ApiManager {

    private Retrofit client;

    private ApiManager() {
        client = new Retrofit.Builder()
                .baseUrl(AppConstants.Base_Url_Test)
                .client(initClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static volatile AppApi INSTANCE;

    public static AppApi getInstance() {
        if (INSTANCE == null) {
            synchronized (ApiManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ApiManager().getAppApi();
                }
            }
        }
        return INSTANCE;
    }

    private AppApi getAppApi() {
        return client.create(AppApi.class);
    }

    private static OkHttpClient initClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //声明日志类
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                AppLogUtil.printE(message);
            }
        });
        //设定日志级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //延时
        builder.addInterceptor(httpLoggingInterceptor)
                //设置参数加密
                //.addInterceptor(new EncryptParamInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        return builder.build();
    }

}

```
ps:参数加密的步骤：拦截带有请求参数的请求，对参数进行加密后 ，重新组装成请求体，再进行请求,
核心代码如下：

```
**
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
 
```
# (三)addDispose()方法 
先看代码：
```

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements BaseView {

    //........................

    //添加当前类名(lin.frameapp.xxx)的dispose
    public void addDispose(Disposable disposable) {
        RxDisposeManager.get().add(getClass().getName(), disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除dispose
        RxDisposeManager.get().cancel(getClass().getName());
    }

   //........................


}

```
用RxDisposeManager去管理每个请求的disposable，在适当的时候去dispose，防止内存泄露 

RxDisposeManager也是用单例实现：

```

public class RxDisposeManager {

    private static volatile RxDisposeManager sInstance = null;

    private ArrayMap<Object, Disposable> maps;

    public static RxDisposeManager get() {

        if (sInstance == null) {
            synchronized (RxDisposeManager.class) {
                if (sInstance == null) {
                    sInstance = new RxDisposeManager();
                }
            }
        }
        return sInstance;
    }

    private RxDisposeManager() {
        maps = new ArrayMap<>();
    }


    public void add(Object tag, Disposable disposable) {
        maps.put(tag, disposable);
    }


    public void remove(Object tag) {
        if (!maps.isEmpty()) {
            maps.remove(tag);
        }
    }

    public void removeAll() {
        if (!maps.isEmpty()) {
            maps.clear();
        }
    }


    public void cancel(Object tag) {
        if (maps.isEmpty()) {
            return;
        }
        if (maps.get(tag) == null) {
            return;
        }
        if (!maps.get(tag).isDisposed()) {
            maps.get(tag).dispose();
            maps.remove(tag);
        }
    }

    public void cancelAll() {
        if (maps.isEmpty()) {
            return;
        }
        Set<Object> keys = maps.keySet();
        for (Object apiKey : keys) {
            cancel(apiKey);
        }
    }
}

```














 

 
