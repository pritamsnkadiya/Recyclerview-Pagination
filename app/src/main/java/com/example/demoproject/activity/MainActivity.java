package com.example.demoproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.demoproject.R;
import com.example.demoproject.adapter.CategoryAdapter;
import com.example.demoproject.adapter.VerticalLineDecorator;
import com.example.demoproject.api.ApiClient;
import com.example.demoproject.model.Item;
import com.example.demoproject.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public List<Item> itemList;
    public CategoryAdapter adapter;
    public String TAG = MainActivity.class.getName();
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        recyclerView = findViewById(R.id.recycler_view);
        itemList = new ArrayList<>();

        adapter = new CategoryAdapter(this, itemList);

        adapter.setLoadMoreListener(() -> {
            recyclerView.post(() -> {
                int index = itemList.size() - 1;
                loadMore(index);
            });
            //Calling loadMore function in Runnable to fix the
            // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        recyclerView.setAdapter(adapter);

        load(0);
    }

/*
    public void load(int index) {
        ApiClient.getSingletonApiClient().getMovies(index, new Callback<List<MovieModel>>() {
            @Override
            public void onResponse(Call<List<MovieModel>> call, Response<List<MovieModel>> response) {
                if (response.isSuccessful()) {

                    if (response.isSuccessful()) {
                        itemList.addAll(response.body());
                        adapter.notifyDataChanged();
                    } else {
                        Log.e(TAG, " Response Error " + response.code());
                    }

                } else {

                }
            }

            @Override
            public void onFailure(Call<List<MovieModel>> call, Throwable t) {
                Log.e(TAG, " Response Error " + t.getMessage());
            }
        });
    }

    public void loadMore(int index) {
        //add loading progress view
        itemList.add(new MovieModel("load"));
        adapter.notifyItemInserted(itemList.size() - 1);
        ApiClient.getSingletonApiClient().getMovies(index, new Callback<List<MovieModel>>() {
            @Override
            public void onResponse(Call<List<MovieModel>> call, Response<List<MovieModel>> response) {
                if (response.isSuccessful()) {
                    //remove loading view
                    itemList.remove(itemList.size() - 1);
                    List<MovieModel> result = response.body();
                    if (result.size() > 0) {
                        //add loaded data
                        itemList.addAll(result);
                    } else {                                    //result size 0 means there is no more data available at server
                        adapter.setMoreDataAvailable(false);
                        //telling adapter to stop calling load more as no more server data available
                        Toast.makeText(context, "No More Data Available", Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataChanged();
                    //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                } else {
                    Log.e(TAG, " Load More Response Error " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MovieModel>> call, Throwable t) {
                Log.e(TAG, " Load More Response Error " + t.getMessage());
            }
        });
    }*/

    public void load(int index) {

        ApiClient.getSingletonApiClient().getAllCategory(index, new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    itemList.addAll(response.body().getItems());
                    adapter.notifyDataChanged();
                } else {
                    Log.e(TAG, " Response Error " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e(TAG, " Response Error " + t.getMessage());
            }
        });
    }

    public void loadMore(int index) {
        //add loading progress view
        itemList.add(new Item("load"));
        adapter.notifyItemInserted(itemList.size() - 1);
        ApiClient.getSingletonApiClient().getAllCategory(index, new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    //remove loading view
                    itemList.remove(itemList.size() - 1);
                    List<Item> result = response.body().getItems();
                    if (result.size() > 0) {
                        //add loaded data
                        itemList.addAll(result);
                    } else {                                    //result size 0 means there is no more data available at server
                        adapter.setMoreDataAvailable(false);
                        //telling adapter to stop calling load more as no more server data available
                        Toast.makeText(context, "No More Data Available", Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataChanged();
                    //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                } else {
                    Log.e(TAG, " Load More Response Error " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e(TAG, " Load More Response Error " + t.getMessage());
            }
        });
    }
}