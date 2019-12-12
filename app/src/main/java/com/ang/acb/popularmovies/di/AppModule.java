package com.ang.acb.popularmovies.di;

import android.app.Application;

import androidx.room.Room;

import com.ang.acb.popularmovies.BuildConfig;
import com.ang.acb.popularmovies.data.local.AppDatabase;
import com.ang.acb.popularmovies.data.local.CastDao;
import com.ang.acb.popularmovies.data.local.MovieDao;
import com.ang.acb.popularmovies.data.local.ReviewDao;
import com.ang.acb.popularmovies.data.local.TrailerDao;
import com.ang.acb.popularmovies.data.remote.ApiService;
import com.ang.acb.popularmovies.utils.Constants;
import com.ang.acb.popularmovies.utils.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * We annotate this class with @Module to signal to Dagger to search within the available
 * methods for possible instance providers. The methods that will actually expose available
 * return types should also be annotated with the @Provides annotation. The @Singleton
 * annotation also signals to the Dagger compiler that the instance should be created
 * only once in the application.
 *
 * See: https://github.com/codepath/android_guides/wiki/Dependency-Injection-with-Dagger-2
 */

@Module(includes = ViewModelModule.class)
class AppModule {

    @Provides
    @Singleton
    AppDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, "movies.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    MovieDao provideMovieDao(AppDatabase database) {
        return database.movieDao();
    }

    @Provides
    @Singleton
    CastDao provideCastDao(AppDatabase database) {
        return database.castDao();
    }

    @Provides
    @Singleton
    ReviewDao provideReviewDao(AppDatabase database) {
        return database.reviewDao();
    }

    @Provides
    @Singleton
    TrailerDao provideTrailerDao(AppDatabase database) {
        return database.trailerDao();
    }

    @Provides
    @Singleton
    Interceptor provideApiRequestInterceptor() {
        // Because we are requesting an API which accepts an API key as a request parameter,
        // we can use an interceptor that could add the query parameter to every request method.
        // See: https://futurestud.io/tutorials/retrofit-2-how-to-add-query-parameters-to-every-request
        return chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                    .build();

            Request request = original.newBuilder().url(url).build();
            return chain.proceed(request);
        };
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        // Retrofit 2 completely relies on OkHttp for any network operation.
        // Since logging isn’t integrated by default anymore in Retrofit 2,
        // we need to add a logging interceptor for OkHttp.
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        // Set the desired log level. Warning: using the HEADERS or BODY levels
        // have the potential to leak sensitive information such as "Authorization"
        // or "Cookie" headers and the contents of request and response bodies.
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        }
        else {
            loggingInterceptor.level(HttpLoggingInterceptor.Level.NONE);
        }

        return loggingInterceptor;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor,
                                     Interceptor apiRequestInterceptor) {
        // Add the logging and auth interceptors to our OkHttp client.
        return new OkHttpClient.Builder()
                .addInterceptor(apiRequestInterceptor)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    ApiService provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(Constants.TMDB_BASE_URL)
                // Configure which converter is used for the data serialization.
                .addConverterFactory(GsonConverterFactory.create())
                // Add a call adapter factory for supporting service method
                // return types other than Retrofit2.Call. We will use a custom
                // Retrofit adapter that converts the Retrofit2.Call into a
                // LiveData of ApiResponse.
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(client)
                .build()
                .create(ApiService.class);
    }
}
