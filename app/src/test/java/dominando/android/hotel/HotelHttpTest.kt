package dominando.android.hotel

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dominando.android.hotel.auth.Auth
import dominando.android.hotel.model.Hotel
import dominando.android.hotel.repository.HotelRepository
import dominando.android.hotel.repository.http.HotelHttp
import dominando.android.hotel.repository.http.HotelHttpApi
import dominando.android.hotel.repository.http.Status
import dominando.android.hotel.repository.imagefiles.FindHotelPicture
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 *
 *  Author:     Anthoni Ipiranga
 *  Project:    Tests
 *  Date:       08/02/2021
 */

class HotelHttpTest {

    private val photoGallery = mock<FindHotelPicture>()
    private val hotelRepository = mock<HotelRepository>()
    private val auth = mock<Auth>()
    private lateinit var hotelHttp: HotelHttp
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun `before each test`() {
        mockWebServer = MockWebServer()
        val remote = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/").toString())
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HotelHttpApi::class.java)
        hotelHttp = HotelHttp(remote, hotelRepository, photoGallery, auth)
    }

    @After
    fun `after each test`() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should synchronize local hotel with remote server`() {
        //Given
        val hotel = Hotel(
            name = "ritz,recife",
            rating = 5.0f
        )
        `auth manager must return a valid user`()
        `local hotel storage have one item`(hotel)
        `server should receive data from hotel`()
        //When
        hotelHttp.synchronizeWithServer()
        //Then
        `local storage should have hotel status as updated`(hotel)

    }

    private fun `local hotel storage have one item`(hotel: Hotel) {
        whenever(hotelRepository.pending())
            .thenReturn(listOf(hotel))
    }

    private fun `server should receive data from hotel`() {
        mockWebServer.enqueue(
            MockResponse().apply {
                setResponseCode(200)
                setBody("{\"id\":$HOTEL_SERVER_ID}")
            }
        )
        mockWebServer.enqueue(
            MockResponse().apply {
                setResponseCode(200)
                setBody(
                    """
                        [
                        {
                        "id":$HOTEL_SERVER_ID,
                        "name":"Ritz Recife"
                        }
                        ]
                                       
                    """
                )
            }
        )
    }

    private fun `local storage should have hotel status as updated`(hotel: Hotel) {
        assertThat(hotel.status).isEqualTo(Status.OK)
        assertThat(hotel.serverId).isEqualTo(HOTEL_SERVER_ID)
        verify(hotelRepository).update(hotel)
    }

    private fun `auth manager must return a valid user`() {
        whenever(auth.getUserId())
            .thenReturn("anthoni ipiranga")
    }

    companion object {

        val HOTEL_SERVER_ID = Random().nextLong()
    }

}