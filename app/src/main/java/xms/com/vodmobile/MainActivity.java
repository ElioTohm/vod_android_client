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
    ArrayList<SectionDataModel> allSampleData;

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

        Series.add(new Serie("64","عائلة الحاج نعمان","http://shareeftube.net/videoimages/64.png"));
        Series.add(new Serie("63", "في ال لا لا لاند","http://shareeftube.net/videoimages/63.png"));
        Series.add(new Serie("61","خاتون", "http://shareeftube.net/videoimages/61.png"));
        Series.add(new Serie("60","عيش و كول غيرا", "http://shareeftube.net/videoimages/60.png"));
        Series.add(new Serie("59","ضبو الشناتي ", "http://shareeftube.net/videoimages/59.png"));

        Clips.add(new Artist(7, "The Voice Kids", "http://shareeftube.net/videos/clips_posters/the-voice.jpg"));
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
