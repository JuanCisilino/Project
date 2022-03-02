package com.frost.project_wm.ui.addedit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frost.project_wm.model.Product
import com.frost.project_wm.network.RepoInstance
import com.frost.project_wm.network.Repository
import rx.schedulers.Schedulers

class AddEditViewModel: ViewModel() {

    private val instance = RepoInstance.getRetrofitInstance().create(Repository::class.java)
    var productLiveData = MutableLiveData<Product?>()

    fun findById(id: Int) =
        instance.getProdById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {it.body
                    ?.let { productLiveData.postValue(it) }
                    ?:run { productLiveData.postValue(null)}
                }, {})

}