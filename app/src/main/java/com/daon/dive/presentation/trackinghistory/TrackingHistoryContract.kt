package com.daon.dive.presentation.trackinghistory

import com.daon.dive.data.entity.TrackingInformation
import com.daon.dive.data.entity.TrackingItem
import com.daon.dive.presentation.BasePresenter
import com.daon.dive.presentation.BaseView

class TrackingHistoryContract {

    interface View : BaseView<Presenter> {

        fun hideLoadingIndicator()

        fun showTrackingItemInformation(trackingItem: TrackingItem, trackingInformation: TrackingInformation)

        fun finish()
    }

    interface Presenter : BasePresenter {

        fun refresh()

        fun deleteTrackingItem()
    }
}