package xms.com.vodmobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import xms.com.vodmobile.Adapters.RecyclerViewDataAdapter;
import xms.com.vodmobile.objects.Artist;
import xms.com.vodmobile.objects.SectionDataModel;
import xms.com.vodmobile.objects.Serie;
import xms.com.vodmobile.objects.Video;

public class MainActivity extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    private HomeAdapter adapter;
//    private List<Type> typeList;
    ArrayList<SectionDataModel> allSampleData;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//
//        typeList = new ArrayList<>();
//        adapter = new HomeAdapter(this, typeList);
//
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(this, 1, 10, false));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);
//
//        try {
//            prepareAlbums();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                Type type = typeList.get(position);
//                StartIntent(type.getTitle());
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));
//    }
//
//    /**
//     * Adding few albums for testing
//     */
//    private void prepareAlbums() throws JSONException {
//        Type typeMovies = new Type("Movies", ApiService.BASE_URL + "videos/appimages/movies.png");
//        typeList.add(typeMovies);
//        Type typeSeries= new Type("Series", ApiService.BASE_URL + "videos/appimages/series.png");
//        typeList.add(typeSeries);
//        Type typeClips= new Type("Clips", ApiService.BASE_URL + "videos/appimages/clips.png");
//        typeList.add(typeClips);
//    }
//
//    private void StartIntent (String type) {
//        if (type.equals("Clips")) {
//            startActivity(new Intent(MainActivity.this, ArtistsList.class));
//        } else {
//            startActivity(new Intent(MainActivity.this, GenreActivity.class).putExtra("genre_type", type));
//        }
//
//    }
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

    final RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, allSampleData);

    my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    my_recycler_view.setAdapter(adapter);

}

    public void createDummyData() {
        List<Object> videoList = new ArrayList<Object>();
        List<Object> Series = new ArrayList<Object>();
        List<Object> Clips = new ArrayList<Object>();

        videoList.add(new Video("قلب الاسد", "http://shareeftube.net/videoimages/506f9fe5f3d53cfda6453ecbae2725e6.png"));
        videoList.add(new Video("يوم مالوش لازمة","http://shareeftube.net/videoimages/844738086fd9fcbb0cb9b31d7b78819b.png"));
        videoList.add(new Video("صابر جوجل","http://shareeftube.net/videoimages/ef99e300c3e5a1a42e22328065754606.png"));
        videoList.add(new Video("مراتي و زوجيتي","http://shareeftube.net/videoimages/3c3dc8bcd4e8697460a1710cb57bee9d.png"));
        videoList.add(new Video("اهواك ", "http://shareeftube.net/videoimages/1a375f7f7bf9a8c794f91f4dca825729.png"));

        Series.add(new Serie("1","عائلة الحاج نعمان","http://shareeftube.net/videoimages/64.png"));
        Series.add(new Serie("1", "في ال لا لا لاند","http://shareeftube.net/videoimages/63.png"));
        Series.add(new Serie("1","خاتون", "http://shareeftube.net/videoimages/61.png"));
        Series.add(new Serie("1","عيش و كول غيرا", "http://shareeftube.net/videoimages/60.png"));
        Series.add(new Serie("1","ضبو الشناتي ", "http://shareeftube.net/videoimages/59.png"));

        Clips.add(new Artist(71, "The Voice Kids", "http://shareeftube.net/videos/clips_posters/the-voice.jpg"));
        Clips.add(new Artist(20,"ابراهيم الحكمي" , "http://shareeftube.net/videos/clips_posters/7AKame.jpg"));
        Clips.add(new Artist(17,"ادم" ,"http://shareeftube.net/videos/clips_posters/adam.jpg"));
        Clips.add(new Artist(4, "ادهم النابلسي","http://shareeftube.net/videos/clips_posters/la-ba3d.jpg"));
        Clips.add(new Artist(18, "اصاله نصري","http://shareeftube.net/videos/clips_posters/assala2.jpg"));

        SectionDataModel newmovies= new SectionDataModel("New Movies", "Movies", videoList);
        SectionDataModel newseries = new SectionDataModel("New Series", "Series", Series);
        SectionDataModel newclips = new SectionDataModel("New Artists", "Clips", Clips);

        allSampleData.add(newmovies);
        allSampleData.add(newseries);
        allSampleData.add(newclips);
        }
}
