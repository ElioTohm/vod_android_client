package xms.com.vodmobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xms.com.vodmobile.Adapters.RecyclerViewDataAdapter;
import xms.com.vodmobile.network.ApiInterface;
import xms.com.vodmobile.network.ApiService;
import xms.com.vodmobile.objects.Artist;
import xms.com.vodmobile.objects.SectionDataModel;
import xms.com.vodmobile.objects.Serie;
import xms.com.vodmobile.objects.Video;

public class MainActivity extends AppCompatActivity {
    private ArrayList<SectionDataModel> allSampleData;
    private RecyclerViewDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allSampleData = new ArrayList<SectionDataModel>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        createDummyData();


        RecyclerView my_recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        my_recycler_view.setHasFixedSize(true);

        adapter = new RecyclerViewDataAdapter(this, allSampleData);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        my_recycler_view.setAdapter(adapter);

    }

    public void createDummyData() {
        final List<Object> videoList = new ArrayList<Object>();
        final List<Object> seriesList = new ArrayList<Object>();
        final List<Object> artitsList = new ArrayList<Object>();

        ApiInterface apiInterface = ApiService.getClient().create(ApiInterface.class);
        Call<List<SectionDataModel>> call = apiInterface.GetNewItems();
        call.enqueue(new Callback<List<SectionDataModel>>() {
            @Override
            public void onResponse(Call<List<SectionDataModel>> call, Response<List<SectionDataModel>> response) {
                for (SectionDataModel sectionDataModel : response.body()) {
                    if (sectionDataModel.getType().equals(SectionDataModel.MOVIES)){
                        for (Video video: sectionDataModel.getVideoList()) {
                            videoList.add(new Video(video.getTitle(),video.getVideo_id(),video.getThumbnail(),
                                    video.getPlot(),video.getActors(),video.getReleased(),video.getRuntime(),
                                    video.getRated(),video.getSubtitle(),video.getStream()));
                        }
                        allSampleData.add(new SectionDataModel(sectionDataModel.getHeaderTitle(), sectionDataModel.getType(), videoList));
                    } else if (sectionDataModel.getType().equals(SectionDataModel.SERIES)){
                        for (Serie series : sectionDataModel.getSerieList()) {
                            seriesList.add(new Serie(series.getTitle(),series.getVideoID(),
                                    series.getThumbnail(),series.getPlot(),series.getActors(),
                                    series.getReleased(),series.getRuntime(),series.getRated()));
                        }
                        allSampleData.add(new SectionDataModel(sectionDataModel.getHeaderTitle(), sectionDataModel.getType(), seriesList));
                    } else if (sectionDataModel.getType().equals(SectionDataModel.ARTISTS)) {
                        for (Artist artist : sectionDataModel.getArtistList()) {
                            artitsList.add(new Artist(artist.getId(),artist.getName(),artist.getImage()));
                        }
                        allSampleData.add(new SectionDataModel(sectionDataModel.getHeaderTitle(), sectionDataModel.getType(), artitsList));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<SectionDataModel>> call, Throwable t) {

            }
        });
        }
}
