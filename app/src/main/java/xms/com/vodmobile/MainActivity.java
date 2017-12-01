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
                    if (sectionDataModel.getType().equals("Movies")){
                        for (Video video: sectionDataModel.getVideoList()) {
                            videoList.add(new Video(video.getTitle(),video.getVideo_id(),video.getThumbnail(),
                                    video.getPlot(),video.getActors(),video.getReleased(),video.getRuntime(),
                                    video.getRated(),video.getSubtitle(),video.getStream()));
                        }
                        allSampleData.add(new SectionDataModel(sectionDataModel.getHeaderTitle(), sectionDataModel.getType(), videoList));
                    } else if (sectionDataModel.getType().equals("Series")){
                        for (Serie series : sectionDataModel.getSerieList()) {
                            seriesList.add(new Serie(series.getTitle(),series.getVideoID(),
                                    series.getThumbnail(),series.getPlot(),series.getActors(),
                                    series.getReleased(),series.getRuntime(),series.getRated()));
                        }
                        allSampleData.add(new SectionDataModel(sectionDataModel.getHeaderTitle(), sectionDataModel.getType(), seriesList));
                    } else if (sectionDataModel.getType().equals("Artists")) {
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


//        videoList.add(new Video("181",
//                "قلب الاسد",
//                "N/A",
//                "2017-10-30",
//                "N/A",
//                "N/A",
//                "N/A",
//                "http://shareeftube.net/videoimages/506f9fe5f3d53cfda6453ecbae2725e6.png",
//                null,
//                "/Lion.Heart.mkv"));
//        videoList.add(new Video("180",
//                "يوم مالوش لازمة",
//                "N/A",
//                "2017-10-28",
//                "N/A",
//                "N/A",
//                "N/A",
//                "http://shareeftube.net/videoimages/844738086fd9fcbb0cb9b31d7b78819b.png",
//                null,
//                "/Youm.Malosh.Lazma.mkv"));
//        videoList.add(new Video("179",
//                "صابر جوجل",
//                "N/A",
//                "2017-10-28",
//                "N/A",
//                "N/A",
//                "N/A",
//                "http://shareeftube.net/videoimages/ef99e300c3e5a1a42e22328065754606.png",
//                null,
//                "/Saber.Google.mkv"));
//        videoList.add(new Video("178",
//                "مراتي و زوجيتي",
//                "N/A",
//                "2017-10-28",
//                "N/A",
//                "N/A",
//                "N/A",
//                "http://shareeftube.net/videoimages/3c3dc8bcd4e8697460a1710cb57bee9d.png",
//                null,
//                "/Meraty.W.Zawgaty.mkv"));
//        videoList.add(new Video("177",
//                "اهواك ",
//                "N/A",
//                "2017-10-20",
//                "N/A",
//                "N/A",
//                "N/A",
//                "http://shareeftube.net/videoimages/1a375f7f7bf9a8c794f91f4dca825729.png",
//                null,
//                "/ahwak2015.mkv"));
//
//
//        seriesList.add(new Serie("64","عائلة الحاج نعمان","http://shareeftube.net/videoimages/64.png"));
//        seriesList.add(new Serie("63", "في ال لا لا لاند","http://shareeftube.net/videoimages/63.png"));
//        seriesList.add(new Serie("61","خاتون", "http://shareeftube.net/videoimages/61.png"));
//        seriesList.add(new Serie("60","عيش و كول غيرا", "http://shareeftube.net/videoimages/60.png"));
//        seriesList.add(new Serie("59","ضبو الشناتي ", "http://shareeftube.net/videoimages/59.png"));
//
//        artitsList.add(new Artist(7, "The Voice Kids", "http://shareeftube.net/videos/clips_posters/the-voice.jpg"));
//        artitsList.add(new Artist(20,"ابراهيم الحكمي" , "http://shareeftube.net/videos/clips_posters/7AKame.jpg"));
//        artitsList.add(new Artist(17,"ادم" ,"http://shareeftube.net/videos/clips_posters/adam.jpg"));
//        artitsList.add(new Artist(4, "ادهم النابلسي","http://shareeftube.net/videos/clips_posters/la-ba3d.jpg"));
//        artitsList.add(new Artist(18, "اصاله نصري","http://shareeftube.net/videos/clips_posters/assala2.jpg"));
//
//        SectionDataModel newmovies= new SectionDataModel("New Movies", "Movies", videoList);
//        SectionDataModel newseries = new SectionDataModel("New Series", "Series", seriesList);
//        SectionDataModel newclips = new SectionDataModel("New Artists", "Clips", artitsList);
//
//        allSampleData.add(newmovies);
//        allSampleData.add(newseries);
//        allSampleData.add(newclips);

        }
}
