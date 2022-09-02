package com.daon.dive.data.repository

import com.daon.dive.data.entity.ShippingCompany

interface ShippingCompanyRepository {

    suspend fun getShippingCompanies(): List<ShippingCompany>

    suspend fun getRecommendShippingCompany(invoice: String): ShippingCompany?
}