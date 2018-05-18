package jo.kotlin.mvpsample.dummy

import jo.kotlin.mvpsample.BuildConfig
import jo.kotlin.mvpsample.remote.MainDataSource
import jo.kotlin.mvpsample.remote.MainRepository
import jo.kotlin.mvpsample.util.Mockable
import jo.kotlin.mvpsample.view.data.Photo
import jo.kotlin.mvpsample.view.data.PhotoResponse
import jo.kotlin.mvpsample.view.data.Photos

/**
 * Created by Jo on 2018. 5. 16.
 */

@Mockable
class DummyHomePresenter() {


    private val message:String = "onFailure"
    private val arrayPhoto: ArrayList<Photo> = ArrayList()
    private val dummyPhoto:Photo = Photo("","","","", 0, "", 0, 0, 0)

    private val dummyPhotos: Photos by lazy {
        Photos(0,"",0,"", arrayPhoto)
    }
    private val dummyPhotoResponse: PhotoResponse by lazy {
        PhotoResponse(dummyPhotos,"",0,"")
    }

    fun loadFlickrPhotos(loadFlickrCallback : MainDataSource.LoadFlickrCallback) {

        arrayPhoto.add(dummyPhoto)

        if(dummyPhotoResponse != null){
            loadFlickrCallback.onSuccess(dummyPhotoResponse)
        }else{
            loadFlickrCallback.onFailure(message)
        }

//        mainRepository.getSearchPhotos("json", "1",
//                "flickr.photos.search", "LOVE", BuildConfig.FLICKR_API_KEY, 1,
//                200, object : MainDataSource.LoadFlickrCallback {
//
//            override fun onSuccess(photoResponse: PhotoResponse) {
//                loadFlickrCallback.onSuccess(photoResponse)
//            }
//
//            override fun onFailure(message: String) {
//                loadFlickrCallback.onFailure(message)
//            }
//
//        })
    }
}