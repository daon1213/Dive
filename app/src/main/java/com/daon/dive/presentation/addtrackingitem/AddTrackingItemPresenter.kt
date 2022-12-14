package com.daon.dive.presentation.addtrackingitem

import androidx.lifecycle.LifecycleCoroutineScope
import com.daon.dive.data.entity.ShippingCompany
import com.daon.dive.data.entity.TrackingItem
import com.daon.dive.data.repository.ShippingCompanyRepository
import com.daon.dive.data.repository.TrackingItemRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AddTrackingItemPresenter(
    private val view: AddTrackingItemsContract.View,
    private val shippingCompanyRepository: ShippingCompanyRepository,
    private val trackerRepository: TrackingItemRepository
) : AddTrackingItemsContract.Presenter {

    override val scope: LifecycleCoroutineScope = MainScope() as LifecycleCoroutineScope

    override var invoice: String? = null
    override var shippingCompanies: List<ShippingCompany>? = null
    override var selectedShippingCompany: ShippingCompany? = null

    override fun onViewCreated() {
        fetchShippingCompanies()
    }

    override fun onDestroyView() {}

    override fun fetchShippingCompanies() {
        scope.launch {
            view.showShippingCompaniesLoadingIndicator()
            if (shippingCompanies.isNullOrEmpty()) {
                shippingCompanies = shippingCompanyRepository.getShippingCompanies()
            }

            shippingCompanies?.let { view.showCompanies(it) }
            view.hideShippingCompaniesLoadingIndicator()
        }
    }

    override fun fetchRecommendShippingCompany() {
        scope.launch {
            view.showRecommendCompanyLoadingIndicator()
            shippingCompanyRepository.getRecommendShippingCompany(invoice!!)?.let { view.showRecommendCompany(it) }
            view.hideRecommendCompanyLoadingIndicator()
        }
    }

    override fun changeSelectedShippingCompany(companyName: String) {
        selectedShippingCompany = shippingCompanies?.find { it.name == companyName }
        enableSaveButtonIfAvailable()
    }

    override fun changeShippingInvoice(invoice: String) {
        this.invoice = invoice
        enableSaveButtonIfAvailable()
    }

    override fun saveTrackingItem() {
        scope.launch {
            try {
                view.showSaveTrackingItemIndicator()
                trackerRepository.saveTrackingItem(
                    TrackingItem(
                        invoice!!,
                        selectedShippingCompany!!
                    )
                )
                view.finish()
            } catch (exception: Exception) {
                view.showErrorToast(exception.message ?: "???????????? ????????? ????????? ???????????? ???????????? ???????????? ????")
            } finally {
                view.hideSaveTrackingItemIndicator()
            }
        }
    }

    private fun enableSaveButtonIfAvailable() {
        if (!invoice.isNullOrBlank() && selectedShippingCompany != null) {
            view.enableSaveButton()
        } else {
            view.disableSaveButton()
        }
    }
}