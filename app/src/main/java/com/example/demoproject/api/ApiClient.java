package com.example.demoproject.api;

import android.content.Context;
import android.util.Log;

import com.example.demoproject.methods.AppConstants;
import com.example.demoproject.methods.method;
import com.example.demoproject.model.MovieModel;
import com.example.demoproject.model.RequestModel;
import com.example.demoproject.model.ResponseModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.demoproject.init.ApplicationAppContext.getAppContext;

public class ApiClient implements Serializable {

    private static final String TAG = ApiClient.class.getSimpleName();

    private static final boolean production = false;

    public static final String BASE_URL = "http://boutiq.artificial-technologies.com/";

    //  public static final String BASE_URL = "http://www.sab99r.com";

    public static boolean isProduction() {
        return production;
    }

    private static Retrofit retrofit = null;

    private static ApiClient apiClient;

    public Context context;

    private static final Object mLock = new Object();

    public ApiClient() {
    }

    public ApiClient(Context context) {
        this.context = context;
    }

    public static ApiClient getSingletonApiClient() {
        synchronized (mLock) {
            if (apiClient == null)
                apiClient = new ApiClient();
            return apiClient;
        }
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60 * 10, TimeUnit.SECONDS)
                    .readTimeout(60 * 10, TimeUnit.SECONDS)
                    .addInterceptor(new LoggingInterceptor())
                    .writeTimeout(60 * 10, TimeUnit.SECONDS);

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient.build())
                    .addConverterFactory(gsonConverterFactory)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            String token;
            AppConstants.TOKEN = method.getPreferences(getAppContext(), "Authorization");
            Request original = chain.request();
            if (getAppContext() != null && !AppConstants.TOKEN.equalsIgnoreCase("")) {
                token = method.getPreferences(getAppContext(), "Authorization");
                // // token = "Bearer" + " " + Method.getPreferences(AppConstants.CONTEXT, "Authorization");
            } else {
                token = "";
            }
            Request request = original.newBuilder()
                    .header("Authorization", token)
                    .method(original.method(), original.body())
                    .build();
            long t1 = System.nanoTime();
            String requestLog = String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers());
            if (request.method().compareToIgnoreCase("post") == 0) {
                requestLog = "\n" + requestLog + "\n" + bodyToString(request);
            }

            try {
                Log.d(TAG, "request" + "\n" + requestLog);
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            String responseLog = String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers());
            String bodyString = response.body().string();
            Log.d(TAG, "response" + "\n" + responseLog + "\n" + bodyString);


            try {
                return response.newBuilder()
                        .body(ResponseBody.create(response.body().contentType(), bodyString))
                        .build();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage() + "---- XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            }
            return null;
        }

        public static String bodyToString(final Request request) {
            try {
                final Request copy = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                copy.body().writeTo(buffer);
                return buffer.readUtf8();
            } catch (final IOException e) {
                return "did not work";
            }
        }
    }

    public void operatorsLogin(RequestModel request, Callback<String> callback) {
        Call<String> call = null;
        try {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            call = apiService.operatorsLogin(request);
            call.enqueue(callback);
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
            callback.onFailure(call, e);
        }
    }

    public void getMovies(int index, Callback<List<MovieModel>> callback) {
        Call<List<MovieModel>> call = null;
        try {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            call = apiService.getMovies(index);
            call.enqueue(callback);
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
            callback.onFailure(call, e);
        }
    }

    public void getAllCategory(int index, Callback<ResponseModel> callback) {
        Call<ResponseModel> call = null;
        try {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            call = apiService.getAllCategory(index);
            call.enqueue(callback);
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
            callback.onFailure(call, e);
        }
    }
}