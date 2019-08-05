package com.ang.acb.popularmovies.data.remote;

import com.ang.acb.popularmovies.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Because we are requesting an API which accepts an API key as a request parameter, it’s
 * valuable to use an interceptor that would add the query parameter to every request method.
 *
 * See: https://futurestud.io/tutorials/retrofit-2-how-to-add-query-parameters-to-every-request
 */
public class ApiRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();

        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build();

        Request request = original.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}

