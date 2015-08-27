package com.example.raghawendrakumar.quikrcars;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;

public class MainActivity extends AppCompatActivity {

    private List<CarDetail> carDetails;
    private RecyclerView recyclerView;
    private static Context mContext;
    private View content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        carDetails = new ArrayList<CarDetail>();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        //call the online API to get the response
        new HttpAsyncTask().execute("http://quikr.0x10.info/api/cars?type=json&query=list_cars");
        // Set a toolbar to replace the action bar.
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                boolean hideToolBar = false;
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (hideToolBar) {
                        getSupportActionBar().hide();
                    } else {
                        getSupportActionBar().show();
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 20) {
                        hideToolBar = true;

                    } else if (dy < -5) {
                        hideToolBar = false;
                    }
                }
            });
        }

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        initFab();
    }
    private void initFab() {
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                View coordinatorLayoutView = findViewById(R.id.coordinatorLayoutView);
                Snackbar
                        .make(coordinatorLayoutView, "FAB Clicked", Snackbar.LENGTH_LONG)
                        .show();
                //Snackbar.make(content, "FAB Clicked", Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    public static Context getContext() {
        return mContext;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        public Context c = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            HttpParams httpParams = new BasicHttpParams();
            HttpClient httpclient = new DefaultHttpClient(httpParams);
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(urls[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                    System.out.println("ResponseString :" + responseString);
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                responseString = "1|" + e.getMessage();
                //TODO Handle problems..
            } catch (IOException e) {
                responseString = "1|" + e.getMessage();
            } finally {
                httpclient.getConnectionManager().shutdown();
            }

            String Result = responseString;

            String[] errorResult = Result.split("\\|", 2);
            if (errorResult[0].equals("1")) {
                //Result.split("|")[1] for internal reference debugging purposes
                // showConnectionError();
                //   Toast.makeText(c, "no tracking data.. " + errorResult[1], Toast.LENGTH_SHORT).show();
                return "";
            }
           ArrayList mItems = new ArrayList<CarDetail>();
            try {
                long OptionId;
                String OptionName;
                int ItemCount;
                JSONArray array = new JSONArray(Result);
                System.out.println("RAGHU::JSONOutput :: "+array.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject carDetail = array.getJSONObject(i);
                    System.out.println(carDetail);
                    carDetails.add(new CarDetail(carDetail));
                }
                System.out.println("size of aaaryList"+carDetails.size());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return Result;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);

        }

        @Override
        protected void onPostExecute(String Result) {
            super.onPostExecute(Result);
            initializeAdapter();
        }
    }

    private void initializeAdapter(){
        RVAdapter adapter = null;
        System.out.println("SIZEREAGU"+carDetails.size());
        if(carDetails!= null) {
            adapter = new RVAdapter(carDetails);
        }
        else
            System.out.println("CardDetail is null during Adaptor call");
        AlphaInAnimationAdapter
                alphaAdapter = new AlphaInAnimationAdapter
                (adapter);
        alphaAdapter.setFirstOnly(true);
        alphaAdapter.setDuration(0);
        recyclerView.setAdapter(alphaAdapter);
    }
}
