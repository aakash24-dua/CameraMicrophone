package com.example.hardware

import com.example.hardware.data.MostPopular
import com.example.hardware.servicemanager.NetworkService
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.internal.stubbing.defaultanswers.ReturnsMocks
import org.mockito.junit.MockitoJUnit
import retrofit2.Response


internal class NetworkServiceTest {
    @get:Rule
    var rule = MockitoJUnit.rule()

    lateinit var service: NetworkService

    @Before
    fun setUp() {
        service = mock(NetworkService::class.java, ReturnsMocks())
    }

    @Test
    fun serviceTest() {
        Mockito.`when`(service.apiInterface.getPopularItems("")).thenReturn(Single.just(Response.success(
            MostPopular(listOf(), "success")
        )))

        val testObserver: TestObserver<MostPopular> = TestObserver()
        service.getPopularItems("")
        service.subject.subscribe(testObserver)
        testObserver.assertValue {
            it.status == "success"
        }
    }
}