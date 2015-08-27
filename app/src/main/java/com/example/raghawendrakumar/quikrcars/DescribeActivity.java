package com.example.raghawendrakumar.quikrcars;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DescribeActivity extends AppCompatActivity {

    TextView tvName, tvRating, tvCC, tvType, tvAbs, tvMileage, tvDesc, tvPrice;
    CollapsingToolbarLayout collapsingToolbar;
    int mutedColor = R.attr.colorPrimary;
    CarDetail selectedCar;
    Bitmap bitmap;
    ImageView header,ivColor;
    Button btnShare, btnLink, btnBack, btnSms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe);
        selectedCar = (CarDetail)(((Bundle)getIntent().getExtras()).get("selected"));
        //tvName = (TextView) findViewById(R.id.cars_name);
        tvRating = (TextView) findViewById(R.id.cars_rating);
        tvCC = (TextView) findViewById(R.id.cars_cc);
        tvType = (TextView) findViewById(R.id.cars_type);
        tvAbs = (TextView) findViewById(R.id.cars_abs);
        tvMileage = (TextView) findViewById(R.id.cars_mileage);
        tvDesc = (TextView) findViewById(R.id.cars_desc);
        ivColor=(ImageView)findViewById(R.id.ivProdColor);
        btnLink=(Button)findViewById(R.id.link);
        btnSms =(Button)findViewById(R.id.sms);
        new AsyncImage().execute();


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent inte = new Intent(Intent.ACTION_VIEW);
                if (selectedCar.link != null) {
                    inte.setData(Uri.parse(selectedCar.link.toString()));
                } else {
                    inte.setData(Uri.parse("http://bangalore.quikr.com/Cars-Bikes/cId-262"));
                }
                startActivity(inte);
            }
        });

        btnSms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);
            }
        });


        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        header = (ImageView) findViewById(R.id.mainimage);
        collapsingToolbar.setTitle(selectedCar.name);

        tvRating.setText("Rating            :   " + selectedCar.rating);
        ivColor.setBackgroundColor(Color.parseColor(selectedCar.color));
        tvCC.setText("Engine            :   "+ selectedCar.engine_cc);
        tvType.setText("Car Type        :   "+ selectedCar.type);
        tvAbs.setText("ABS                :   " + selectedCar.abs_exist);
        tvMileage.setText("Mileage          :   " + selectedCar.mileage);
        tvDesc.setText("Description    :   \n\n" + selectedCar.description);

        }

        public class AsyncImage extends AsyncTask<String, Void, Drawable>
        {
        public AsyncImage()
        {}
        @Override
        protected Drawable doInBackground(String... params) {
            URL myUrl;
            Drawable drawable= null;
            try {
                myUrl = new URL(selectedCar.image);
                InputStream inputStream = (InputStream)myUrl.getContent();
                drawable = Drawable.createFromStream(inputStream, null);


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            header.setImageDrawable(result);
            bitmap = ((BitmapDrawable) result).getBitmap();
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {

                    mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                    collapsingToolbar.setContentScrimColor(mutedColor);
                }
            });

        }
    }

}
