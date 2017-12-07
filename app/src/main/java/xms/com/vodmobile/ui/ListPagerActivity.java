package xms.com.vodmobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import xms.com.vodmobile.R;
import xms.com.vodmobile.network.ApiInterface;
import xms.com.vodmobile.network.ApiService;
import xms.com.vodmobile.objects.Genre;

/**
 * Created by Elio on 11/28/2017.
 */

public class ListPagerActivity extends AppCompatActivity {
    ListPagerPagerAdapter listPagerPagerAdapter;
    static String TYPE;
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create an adapter that when requested, will return a fragment representing an object in
        // the collection.
        //
        // ViewPager and its adapters use support library fragments, so we must use
        // getSupportFragmentManager.
        listPagerPagerAdapter = new ListPagerPagerAdapter(getSupportFragmentManager());

        Intent intent = getIntent();
        TYPE = intent.getStringExtra("Type");

        getSupportActionBar().setTitle(TYPE);

        final ArrayList<Genre> genres = new ArrayList<Genre>();
        ApiInterface apiInterface = ApiService.getClient().create(ApiInterface.class);
        final ArrayList listgenre = new ArrayList();
        Genre genretype = new Genre();
        genretype.setType(TYPE);
        listgenre.add(genretype);
        Call<List<Genre>> call = apiInterface.GetGenres(listgenre);
        call.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, retrofit2.Response<List<Genre>> response) {
                genres.add(new Genre("All",9999));
                genres.addAll(response.body());
                listPagerPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                Log.e("MovieGenreRequest", t.toString());
            }
        });

        listPagerPagerAdapter.setGenreList(genres);

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(listPagerPagerAdapter);
    }


    public static class ListPagerPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Genre> genres = new ArrayList<Genre>();
        public ListPagerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setGenreList(ArrayList<Genre> genres) {
            this.genres = genres;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new ListObjectFragment();
            Bundle args = new Bundle();
            Gson gson = new Gson();
            String objstring = gson.toJson(genres.get(i));
            args.putString("genre", objstring);
            if (TYPE.equals("Series")) {
                args.putString("Type", "Series");
            } else {
                args.putString("Type", "Movies");
            }

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return this.genres.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return genres.get(position).getTitle();
        }
    }
}
