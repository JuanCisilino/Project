package com.frost.project_wm.ui.addedit

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frost.project_wm.model.Product
import com.frost.project_wm.network.ProductRepository
import com.frost.project_wm.network.RepoInstance
import rx.schedulers.Schedulers

class AddEditViewModel: ViewModel() {

    private val instance = RepoInstance.getRetrofitInstance().create(ProductRepository::class.java)
    var productLiveData = MutableLiveData<Product?>()
    var newProductLiveData = MutableLiveData<Product?>()
    var modifiedProductLiveData = MutableLiveData<Product?>()
    var deletedProductLiveData = MutableLiveData<Product?>()
    var imageString : String ?= null
    var imageUri : Uri?= null

    fun findById(id: Int) =
        instance.getProdById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {it.body
                    ?.let { productLiveData.postValue(it) }
                    ?:run { productLiveData.postValue(null)}
                }, {})

    fun saveProduct(product: Product) =
        instance.saveProduct(product)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { newProductLiveData.postValue(it) },
                { newProductLiveData.postValue(null)})

    fun updateProduct(product: Product) =
        instance.updateProduct(product)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { modifiedProductLiveData.postValue(it) },
                { modifiedProductLiveData.postValue(null)})

    fun deleteProduct(id: Int) =
        instance.deleteProduct(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { deletedProductLiveData.postValue(productLiveData.value) },
                { deletedProductLiveData.postValue(null) }
            )
}