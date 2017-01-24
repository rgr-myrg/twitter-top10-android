package net.usrlib.twittersearch.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creates Retrofit Client with Retrofit.Builder()
 * Created by rgr-myrg on 1/21/17.
 */

public class RetrofitClient {
	private static Retrofit sRetrofit = null;

	public static final Retrofit getClientWithUrl(final String url) {
		if (sRetrofit == null) {
			sRetrofit = new Retrofit.Builder()
					.baseUrl(url)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		}

		return sRetrofit;
	}
}
