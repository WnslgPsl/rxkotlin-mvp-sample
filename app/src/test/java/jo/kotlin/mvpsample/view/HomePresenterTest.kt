package jo.kotlin.mvpsample.view

import com.nhaarman.mockitokotlin2.*
import jo.kotlin.mvpsample.BuildConfig
import jo.kotlin.mvpsample.HomePresenter
import jo.kotlin.mvpsample.MainContract
import jo.kotlin.mvpsample.dummy.DummyHomePresenter
import jo.kotlin.mvpsample.remote.MainDataSource
import jo.kotlin.mvpsample.remote.MainRepository
import jo.kotlin.mvpsample.view.adapter.MainAdapterContract
import jo.kotlin.mvpsample.view.data.Photo
import jo.kotlin.mvpsample.view.data.PhotoResponse
import jo.kotlin.mvpsample.view.data.Photos
import junit.framework.Assert.assertEquals
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.mockito.*

import org.mockito.ArgumentCaptor
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

/**
 * Created by Jo on 2018. 5. 15.
 */

class HomePresenterTest {

    @Mock
    private lateinit var mainContractView: MainContract.View
    @Mock
    private lateinit var mainAdapterContractView: MainAdapterContract.View
    @Mock
    private lateinit var mainAdapterContractModel: MainAdapterContract.Model
    @Mock
    private lateinit var mainRepository: MainRepository


    @Mock
    private lateinit var photo: ArrayList<Photo>
    @Mock
    private lateinit var Photos: Photos
    @Mock
    private lateinit var photoResponse: PhotoResponse

    @Captor
    private val getLoadFlickrCallbackCaptor:
            ArgumentCaptor<MainDataSource.LoadFlickrCallback> = ArgumentCaptor.forClass(MainDataSource.LoadFlickrCallback::class.java)


    private lateinit var homePresenter: HomePresenter

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        homePresenter = HomePresenter(mainContractView, mainRepository, mainAdapterContractView, mainAdapterContractModel)
//        mainContractView = Mockito.mock(MainContract.View::class.java)
//        mainAdapterContractView = Mockito.mock(MainAdapterContract.View::class.java)
//        mainAdapterContractModel = Mockito.mock(MainAdapterContract.Model::class.java)
//        mainRepository = Mockito.mock(MainRepository::class.java)

//        dummyHomePresenter = Mockito.mock(DummyHomePresenter(mainRepository)::class.java)

//        photoResponse = Mockito.mock(PhotoResponse::class.java)

    }

    @After
    fun tearDown() {
    }

    @Test
    fun createPresenter_setsThePresenterToView() {

        given(photoResponse.stat).willReturn("ok")
        given(photoResponse.photos).willReturn(Photos)
        given(Photos.photo).willReturn(arrayListOf())
        doNothing().`when`(mainAdapterContractModel).addItems(any())

        //doAnswer는 무엇이 발생했는지 알 수 있게 테스트가 빨리 실패 하도록 파라미터를 변경할 수 있다.
        Mockito.doAnswer {
            //            println(it);
            it.getArgument<MainDataSource.LoadFlickrCallback>(7).onSuccess(photoResponse)
            // 에러를 내기위해 다른값을 넣을 수 있다
            assertEquals("json", it.getArgument(0))

            null
        }.`when`(mainRepository).getSearchPhotos(any(), any(), any(), any(), any(), any(), any(), any())

        homePresenter.loadFlickrPhotos()
        verify(mainContractView).showProgress()
        verify(mainContractView).hideProgress()
        verify(mainAdapterContractModel).addItems(any())
        verify(mainAdapterContractView).updateView()

    }

    @Test
    fun createPresenter_setValue_Async() {

        photo = ArrayList()
        Photos = Photos(1, "20", 1, "20", photo)
        photoResponse = PhotoResponse(Photos, "ok", 200, "success")

        homePresenter.loadFlickrPhotos()

        // ArgumentCaptor는 콜백을 우리가 원하는 상황에 맞게 콜백 할 수 있기 때문에 더 많은 제어를 할 수 있다.
        verify(mainRepository).getSearchPhotos(any(), any(),
                any(), any(), any(), any(),
                any(), capture(getLoadFlickrCallbackCaptor))

        getLoadFlickrCallbackCaptor.value.onSuccess(photoResponse)

        verify(mainContractView).showProgress()
        verify(mainContractView).hideProgress()
        verify(mainAdapterContractModel).addItems(any())
        verify(mainAdapterContractView).updateView()
    }

}