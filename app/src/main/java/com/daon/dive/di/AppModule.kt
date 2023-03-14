package com.daon.dive.di

import android.app.Activity
import com.daon.dive.BuildConfig
import com.daon.dive.data.api.SweetTrackerApi
import com.daon.dive.data.api.Url
import com.daon.dive.data.db.AppDatabase
import com.daon.dive.data.entity.TrackingInformation
import com.daon.dive.data.entity.TrackingItem
import com.daon.dive.data.preference.PreferenceManager
import com.daon.dive.data.preference.SharedPreferenceManager
import com.daon.dive.data.repository.*
import com.daon.dive.presentation.addtrackingitem.AddTrackingItemFragment
import com.daon.dive.presentation.addtrackingitem.AddTrackingItemPresenter
import com.daon.dive.presentation.addtrackingitem.AddTrackingItemsContract
import com.daon.dive.presentation.trackinghistory.TrackingHistoryContract
import com.daon.dive.presentation.trackinghistory.TrackingHistoryFragment
import com.daon.dive.presentation.trackinghistory.TrackingHistoryPresenter
import com.daon.dive.presentation.trackingitems.TrackingItemsContract
import com.daon.dive.presentation.trackingitems.TrackingItemsFragment
import com.daon.dive.presentation.trackingitems.TrackingItemsPresenter
import com.daon.dive.work.AppWorkerFactory
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


val appModule = module {

    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().trackingItemDao() }
    single { get<AppDatabase>().shippingCompanyDao() }

    // Api
    single {
        OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }
    single<SweetTrackerApi> {
        Retrofit.Builder().baseUrl(Url.SWEET_TRACKER_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create()
    }

    // Preference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }

    // Repository
//    single<TrackingItemRepository> { TrackingItemRepositoryStub() }
    single<TrackingItemRepository> { TrackingItemRepositoryImpl(get(), get(), get()) }
    single<ShippingCompanyRepository> { ShippingCompanyRepositoryImpl(get(), get(), get(), get()) }

    // Work
    single { AppWorkerFactory(get(), get()) }

    // Presentation
    scope<TrackingItemsFragment> {
        scoped<TrackingItemsContract.Presenter> { TrackingItemsPresenter(getSource()!!, get()) }
    }
    scope<AddTrackingItemFragment> {
        scoped<AddTrackingItemsContract.Presenter> {
            AddTrackingItemPresenter(getSource()!!, get(), get())
        }
    }
    scope<TrackingHistoryFragment> {
        scoped<TrackingHistoryContract.Presenter> { (trackingItem: TrackingItem, trackingInformation: TrackingInformation) ->
            TrackingHistoryPresenter(getSource()!!, get(), trackingItem, trackingInformation)
        }
    }
}