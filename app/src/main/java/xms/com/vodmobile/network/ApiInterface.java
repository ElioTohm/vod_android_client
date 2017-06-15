package xms.com.vodmobile.network;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import xms.com.vodmobile.objects.Artist;
import xms.com.vodmobile.objects.Client;
import xms.com.vodmobile.objects.Episode;
import xms.com.vodmobile.objects.Genre;
import xms.com.vodmobile.objects.Season;
import xms.com.vodmobile.objects.Serie;
import xms.com.vodmobile.objects.Video;

public interface ApiInterface {

    @POST("clientsingin")
    Call<Client> CheckUserEmail(@Body Object client);

    @POST("clientregister")
    Call<Client> RegisterClient(@Body Client client);

    @POST("getmovies")
    Call<List<Video>> GetVideos(@Body ArrayList genre);

    @POST("getgenres")
    Call<List<Genre>> GetGenres(@Body ArrayList genretype);

    @POST("getartists")
    Call<List<Artist>> GetArtists(@Body ArrayList genreid);

    @POST("getclips")
    Call<List<Episode>> GetClips(@Body ArrayList artists);

    @POST("getseries")
    Call<List<Serie>> GetSeries(@Body ArrayList genreid);

    @POST("getseasons")
    Call<List<Season>> GetSeasons(@Body ArrayList seriesid);

    @POST("getepisodes")
    Call<List<Episode>> GetEpisodes(@Body ArrayList seasonslist);

    @Streaming
    @GET("apk/shareeftube.apk")
    Call<ResponseBody> DownloadUpdate();
}
