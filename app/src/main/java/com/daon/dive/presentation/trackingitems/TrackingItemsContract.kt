package com.daon.dive.presentation.trackingitems

import com.daon.dive.data.entity.TrackingInformation
import com.daon.dive.data.entity.TrackingItem
import com.daon.dive.presentation.BasePresenter
import com.daon.dive.presentation.BaseView

class TrackingItemsContract {

    abstract inner class View(override val presenter: Presenter) : BaseView<Presenter> {

        abstract fun showLoadingIndicator()

        abstract fun hideLoadingIndicator()

        abstract fun showNoDataDescription()

        abstract fun showTrackingItemInformation(trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>)
    }

    interface Presenter : BasePresenter {

        var trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>

        fun refresh()
    }
}