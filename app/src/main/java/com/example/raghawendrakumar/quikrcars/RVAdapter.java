package com.example.raghawendrakumar.quikrcars;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by raghawendra.kumar on 11-07-2015.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView car_name;
        TextView car_price;
        ImageView personPhoto;

        PersonViewHolder(final View itemView) {
            super(itemView);
            System.out.println("Raghu::PersonViewHolder constructor");
            cv = (CardView)itemView.findViewById(R.id.cv);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            car_name = (TextView)itemView.findViewById(R.id.car_name);
            car_price = (TextView)itemView.findViewById(R.id.car_price);

            //Set onClick listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    CarDetail card = (CarDetail)((CardView) v).getTag(R.string.value_pass);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("selected", card);
                    intent.putExtras(bundle);
                    intent.setClass(MainActivity.getContext(), DescribeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    MainActivity.getContext().startActivity(intent);
                    }
            });
        }
    }

    List<CarDetail> carDetails;
    RVAdapter(List<CarDetail> CarDetails){
        this.carDetails = CarDetails;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        new AsyncImage(personViewHolder,i).execute();
    }

    @Override
    public int getItemCount() {
        return carDetails.size();
    }

    public class AsyncImage extends AsyncTask<String, Void, Drawable>
    {
        PersonViewHolder personViewHolder;
        int i;
        public AsyncImage(PersonViewHolder pv, int i)
        {
            this.personViewHolder = pv;
            this.i = i;
        }
        @Override
        protected Drawable doInBackground(String... params) {
            URL myUrl;
            Drawable drawable= null;
            try {
                myUrl = new URL(carDetails.get(i).image);
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
            personViewHolder.personPhoto.setImageDrawable(result);
            personViewHolder.car_name.setText(carDetails.get(i).name);
            personViewHolder.car_price.setText(carDetails.get(i).price+" L");
            personViewHolder.cv.setTag(R.string.value_pass,carDetails.get(i));

        }
    }
}
