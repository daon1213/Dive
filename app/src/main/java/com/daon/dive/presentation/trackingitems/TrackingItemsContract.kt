package com.daon.dive.presentation.trackingitems

import com.daon.dive.data.entity.TrackingInformation
import com.daon.dive.data.entity.TrackingItem
import com.daon.dive.presentation.BasePresenter
import com.daon.dive.presentation.BaseView

class TrackingItemsContract {

    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showNoDataDescription()

        fun showTrackingItemInformation(trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>)
    }

    interface Presenter : BasePresenter {

        var trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>

        fun refresh()
    }
}