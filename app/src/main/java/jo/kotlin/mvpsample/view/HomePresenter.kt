package jo.kotlin.mvpsample

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.internal.disposables.DisposableHelper
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import jo.kotlin.mvpsample.remote.MainDataSource
import jo.kotlin.mvpsample.remote.MainRepository
import jo.kotlin.mvpsample.util.Mockable
import jo.kotlin.mvpsample.view.adapter.MainAdapterContract
import jo.kotlin.mvpsample.view.data.PhotoResponse
import java.util.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableMaybeObserver
import jo.kotlin.mvpsample.network.retrofit.retrofit
import retrofit2.HttpException
import okhttp3.ResponseBody
import retrofit2.adapter.rxjava2.Result.response


/**
 * Created by Jo on 2018. 3. 26.
 */

@Mockable
class HomePresenter(val view: MainContract.View, val mainRepository: MainRepository,
                    val adapterView: MainAdapterContract.View,
                    val adapterModel: MainAdapterContract.Model) : MainContract.Presenter {


    init {

        view.presenter = this

        adapterView.onItemClicked = {
            view.showDetailInfo(adapterModel.getItem(it).title)
        }
    }

    lateinit var disposable: Disposable

    override val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun loadFlickrPhotos() {

        view.showProgress()
        disposable = mainRepository.getSearchPhotos("json", "1",
                "flickr.photos.search", "love", BuildConfig.FLICKR_API_KEY, 1,
                200)
                .filter { items ->
                    if(  items.stat == "ok") {
                        true
                    }else{
                        view.hideProgress()
                        Log.d("seongjun_code", "${items.code}")
//                        view.showMessage("Failure : ${items.code}")
                        false
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableMaybeObserver<PhotoResponse>() {
                    override fun onComplete() {
                    }

                    override fun onSuccess(items: PhotoResponse) {
                        view.hideProgress()
                        adapterModel.addItems(items.photos.photo)
                        adapterView.updateView()
//                        val stat = items?.stat
//                        if (stat == "ok") {
//                            adapterModel.addItems(items.photos.photo)
//                            adapterView.updateView()
//                        } else {
//                            view.showMessage("stat : $stat")
//                        }
                    }

                    override fun onError(e: Throwable) {
                        view.hideProgress()
                        view.showMessage("Failure : ${e.message}")
                    }

                })

//        mainRepository.getSearchPhotos("json", "1",
//                "flickr.photos.search", "LOVE", BuildConfig.FLICKR_API_KEY, 1,
//                200, object : MainDataSource.LoadFlickrCallback {
//
//            override fun onSuccess(photoResponse: PhotoResponse) {
//                view.hideProgress()
//
//                val stat = photoResponse?.stat
//                if(stat.equals("ok")){
////                    Log.d("seongjun", stat);
//                    adapterModel.addItems(photoResponse.photos.photo)
//                    adapterView.updateView()
//                }else{
//                    view.showMessage("stat : $stat")
//                }
//            }
//
//            override fun onFailure(message: String) {
//                view.hideProgress()
//                view.showMessage("Failure : $message")
//            }
//
//        })

        compositeDisposable.add(disposable)

    }

}