package com.ang.acb.popularmovies.utils;

import android.content.Context;

import com.ang.acb.popularmovies.data.local.AppDatabase;
import com.ang.acb.popularmovies.data.local.LocalMovieDataSource;
import com.ang.acb.popularmovies.data.remote.ApiClient;
import com.ang.acb.popularmovies.data.remote.ApiService;
import com.ang.acb.popularmovies.data.remote.RemoteMovieDataSource;
import com.ang.acb.popularmovies.data.repository.MovieRepository;

/**
 * Enables injection of data sources.
 *
 * See: https://github.com/googlesamples/android-sunflower
 */
public class InjectorUtils {

//    private static RemoteMovieDataSource provideMoviesRemoteDataSource() {
//        ApiService apiService = ApiClient.getInstance();
//        AppExecutors executors = AppExecutors.getInstance();
//        return RemoteMovieDataSource.getInstance(apiService, executors);
//    }
//
//    private static LocalMovieDataSource provideMoviesLocalDataSource(Context context) {
//        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
//        return LocalMovieDataSource.getInstance(database);
//    }
//
//    private static MovieRepository provideMovieRepository(Context context) {
//        RemoteMovieDataSource remoteDataSource = provideMoviesRemoteDataSource();
//        LocalMovieDataSource localDataSource = provideMoviesLocalDataSource(context);
//        AppExecutors executors = AppExecutors.getInstance();
//        return MovieRepository.getInstance(
//                localDataSource,
//                remoteDataSource,
//                executors);
//    }
//
//    public static ViewModelFactory provideViewModelFactory(Context context) {
//        MovieRepository repository = provideMovieRepository(context);
//        return ViewModelFactory.getInstance(repository);
//    }
}