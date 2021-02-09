package dominando.android.hotel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.*
import dominando.android.hotel.form.HotelFormViewModel
import dominando.android.hotel.model.Hotel
import dominando.android.hotel.repository.HotelRepository
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

/**
 *
 *  Author:     Anthoni Ipiranga
 *  Project:    Tests
 *  Date:       08/02/2021
 */

class HotelFormViewModelTests {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: HotelFormViewModel
    private val mockedRepo = mock<HotelRepository>()
    private val anHotelId = Random().nextLong()
    private val anHotel = Hotel(
        id = anHotelId,
        name = "Ritz recife",
        address = "Praia de boa viagem, SS",
        rating = 5.0f
    )

    @Before
    fun `before each test`() {
        viewModel = HotelFormViewModel(mockedRepo)
    }

    @Test
    fun `given an existing ID should load the hotel`() {
        //Given
        val liveData = MutableLiveData<Hotel>().apply {
            value = anHotel
        }
        //When
        whenever(mockedRepo.hotelById(any()))
            .thenReturn(liveData)
        //Then
        viewModel.loadHotel(anHotelId).observeForever {
            assertThat(it).isEqualTo(anHotel)
        }
    }

    @Test
    fun `given valid hotel info should save a hotel`() {
        //Given
        val info = Hotel(
            name = "Ritz Recife",
            address = "Pria da boa viagem,recife/PE",
            rating = 4.8f
        )
        //When
        val saved = viewModel.saveHotel(info)
        whenever(mockedRepo.save(any())).thenAnswer {
            Unit
        }
        //Then
        assertThat(saved).isTrue()
        verify(mockedRepo, times(1)).save(any())
        verifyNoMoreInteractions(mockedRepo)
    }

    @Test
    fun `given invalid hotel info should fail at save the hotel`() {
        //Given
        val invalidInfo = Hotel(
            name = "Y",
            address = "WW",
            rating = 4.8f
        )
        //When
        val saved = viewModel.saveHotel(invalidInfo)
        //Then
        assertThat(saved).isFalse()
        verifyZeroInteractions(mockedRepo)
    }

}