package xms.com.vodmobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import xms.com.vodmobile.Adapters.RecyclerTouchListener;
import xms.com.vodmobile.Adapters.SeriesAdapter;
import xms.com.vodmobile.Adapters.VideosAdapter;
import xms.com.vodmobile.R;
import xms.com.vodmobile.Series.SeriesDetailActivity;
import xms.com.vodmobile.VideoDetailActivity;
import xms.com.vodmobile.network.ApiInterface;
import xms.com.vodmobile.network.ApiService;
import xms.com.vodmobile.objects.Genre;
import xms.com.vodmobile.objects.Serie;
import xms.com.vodmobile.objects.Video;

/**
 * Created by Elio on 11/28/2017.
 */

public class ListObjectFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private VideosAdapter videosAdapter;
    private List<Video> videoList;
    private List<Serie> serieList;
    private SeriesAdapter seriesAdapter;
    int genre_id;
    String TYPE;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_collection_object, container, false);
        Bundle args = getArguments();
        Gson gson = new Gson();
        final Genre genre = gson.fromJson(args.getString("genre"), Genre.class);
        genre_id = genre.getID();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        TYPE = args.getString("Type");

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(getContext(), 2, 10, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (TYPE.equals("Series")) {
            serieList = new ArrayList<>();
            seriesAdapter = new SeriesAdapter(getContext(), serieList);
            recyclerView.setAdapter(seriesAdapter);
            prepareSeries();
        } else {
            videoList = new ArrayList<>();
            videosAdapter = new VideosAdapter(getContext(), videoList);
            recyclerView.setAdapter(videosAdapter);
            prepareMovies();
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(),
                                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (TYPE.equals("Series")) {
                    startSerieDetailActivity(seriesAdapter.getItem(position));
                } else {
                    startVideoDetailActivity(videosAdapter.getItem(position));
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return rootView;
    }

    private void prepareMovies () {
        ApiInterface apiInterface = ApiService.getClient().create(ApiInterface.class);
        ArrayList Listgenre = new ArrayList();
        Genre genreid = new Genre();
        genreid.setGenre(genre_id);
        Listgenre.add(genreid);
        Call<List<Video>> call = apiInterface.GetVideos(Listgenre);
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, retrofit2.Response<List<Video>> response) {
                videoList.addAll(response.body());
                videosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e("VideoListRequest", t.toString());
            }
        });
    }

    private void prepareSeries () {
        ApiInterface apiInterface = ApiService.getClient().create(ApiInterface.class);
        ArrayList genrelist = new ArrayList();
        Genre genreid = new Genre();
        genreid.setGenre(genre_id);
        genrelist.add(genreid);

        Call<List<Serie>> call = apiInterface.GetSeries(genrelist);
        call.enqueue(new Callback<List<Serie>>() {
            @Override
            public void onResponse(Call<List<Serie>> call, retrofit2.Response<List<Serie>> response) {
                serieList.addAll(response.body());
                seriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Serie>> call, Throwable t) {

            }
        });
    }

    private void startVideoDetailActivity (Video video) {
        Gson gson = new Gson();
        String objstring = gson.toJson(video);
        startActivity(new Intent(getContext(), VideoDetailActivity.class)
                .putExtra("video",objstring));
    }

    private void startSerieDetailActivity (Serie serie) {
        Gson gson = new Gson();
        String objstring = gson.toJson(serie);
        startActivity(new Intent(getContext(), SeriesDetailActivity.class)
                .putExtra("serie",objstring));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Retrieve the SearchView and plug it into SearchManager
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TYPE.equals("Series")) {
            seriesAdapter.getFilter().filter(newText);
        } else {
            videosAdapter.getFilter().filter(newText);
        }
        return true;
    }
}
