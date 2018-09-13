package jo.kotlin.mvpsample

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import jo.kotlin.mvpsample.remote.MainRepository
import jo.kotlin.mvpsample.util.Mockable
import jo.kotlin.mvpsample.view.adapter.MainAdapterContract
import jo.kotlin.mvpsample.view.data.PhotoResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableMaybeObserver
import jo.kotlin.mvpsample.util.ApiException


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
                    if (items.stat == "ok") {
                        return@filter true
                    } else {
                        throw items.code.ApiException()
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableMaybeObserver<PhotoResponse>() {
                    override fun onComplete() {
                        this.dispose()
                    }

                    override fun onSuccess(items: PhotoResponse) {
                        view.hideProgress()
                        adapterModel.addItems(items.photos.photo)
                        adapterView.updateView()
//                        onComplete()
                    }

                    override fun onError(e: Throwable) {
                        view.hideProgress()
                        view.showMessage("Failure : ${e.message}")
//                        onComplete()
                    }

                })
        compositeDisposable.add(disposable)
    }
}