package jo.kotlin.mvpsample.remote

import io.reactivex.Single
import jo.kotlin.mvpsample.view.data.PhotoResponse

/**
 * Created by Jo on 2018. 3. 29.
 */

interface MainDataSource {


    interface LoadFlickrCallback {
        fun onSuccess(t: PhotoResponse)
        fun onFailure(message: String)
    }

    fun getSearchPhotos(format: String, nojsoncallback: String, method: String, searchKeyword: String, flickrKey: String, requestPage: Int,
                        requestPerPage: Int): Single<PhotoResponse>

//fun getSearchPhotos(format: String, nojsoncallback: String, method: String, searchKeyword: String, flickrKey: String, requestPage: Int,
//                    requestPerPage: Int, callback: LoadFlickrCallback)
}