package jo.kotlin.mvpsample

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Jo on 2018. 3. 26.
 */

interface MainContract {



    interface View : BaseView<Presenter>{
        fun hideProgress()
        fun showProgress()
        fun showMessage(msg: String)
        fun showDetailInfo(title: String)
    }

    interface Presenter{
        val compositeDisposable: CompositeDisposable
        fun loadFlickrPhotos()

    }

    
}