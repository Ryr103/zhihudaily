package com.example.recyclerviewdemo;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;


import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private List<Map<String,Object>> list = new ArrayList<>();
    private TextView month;
    private TextView day;
    private RefreshLayout mReFreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        month = findViewById(R.id.tv_month);
        day = findViewById(R.id.tv_day);


        recyclerView = findViewById(R.id.recyclerView);
        mReFreshLayout = findViewById(R.id.swipeReFreshLayout);
        Calendar calendar = Calendar.getInstance();
        int Month = calendar.get(Calendar.MONTH)+1;
        month.setText(Month +"月");
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        day.setText(Day+"日");
        final int[] date = {20200000+Month*100+Day};





        mReFreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                list.clear();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        BufferedReader reader = null;
                        try {
                            URL url = new URL("https://news-at.zhihu.com/api/3/stories/latest");
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(4000);
                            connection.setReadTimeout(8000);
                            InputStream in = connection.getInputStream();
                            reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            showResponse(response.toString());
                        } catch (Exception e) {

                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                                }
                            });


                        } finally {
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                });
                thread.start();
                mReFreshLayout.finishRefresh();
            }
        });
        mReFreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        BufferedReader reader = null;
                        try {

                                URL url = new URL("https://news-at.zhihu.com/api/3/stories/before/" + String.valueOf(date[0]));
                                date[0] = date[0] -1;


                            connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);
                            InputStream in = connection.getInputStream();
                            reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            showResponse(response.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                                }
                            });

                        } finally {
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                
                thread.start();
                refreshlayout.finishLoadmore();
            }
        });









        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://news-at.zhihu.com/api/3/stories/latest");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                        }
                    });

                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });

        thread.start();




    }
    public void showResponse(final String string) {


        try {
            JSONObject jsonObject = new JSONObject(string);
            String date = jsonObject.getString("date");
            final JSONArray jsonArray = jsonObject.getJSONArray("stories");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                final String hint = jsonObject1.getString("hint");
                final String title = jsonObject1.getString("title");
                String imageUrl = jsonObject1.getJSONArray("images").getString(0);
                String url = jsonObject1.getString("url");
                String id =jsonObject1.getString("id");
                Map<String, Object> map = new HashMap<>();
                map.put("hint",hint);
                map.put("title",title);
                map.put("url",url);
                map.put("id",id);
                map.put("date",date);
                map.put("imageUrl",imageUrl);
                list.add(map);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));//垂直排列 , Ctrl+P
                    recyclerView.setAdapter(new MyAdapter(MainActivity.this, list));//绑定适配器
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }






}
