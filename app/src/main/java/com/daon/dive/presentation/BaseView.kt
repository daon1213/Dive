package com.daon.dive.presentation

interface BaseView<PresenterT: BasePresenter> {

    val presenter: PresenterT

}