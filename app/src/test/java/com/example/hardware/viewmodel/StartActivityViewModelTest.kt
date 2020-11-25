package com.example.hardware.viewmodel

import com.example.hardware.data.MostPopular
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


class StartActivityViewModelTest {

    val viewModel: StartActivityViewModel by lazy {
        StartActivityViewModel()
    }


    @Before
    fun setUp() {

    }

    @Test
    fun getService() {
    }

    @Test
    fun setService() {
    }

    @Test
    fun getItems() {
        val testObserver = TestObserver<MostPopular>()
        viewModel.response.subscribe(testObserver)
        Mockito.verify(viewModel.getMostPopular())
        testObserver.assertValue {
            it.status == "success"
        }
        testObserver.dispose()

    }

}