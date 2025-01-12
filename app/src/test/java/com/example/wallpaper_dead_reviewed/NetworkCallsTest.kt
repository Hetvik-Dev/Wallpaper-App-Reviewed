package com.example.wallpaper_dead_reviewed

import com.example.wallpaper_dead_reviewed.api.Utils.Resource
import com.example.wallpaper_dead_reviewed.api.data.PicSumApi
import com.example.wallpaper_dead_reviewed.api.data.WallpaperRepostiryImpl
import com.example.wallpaper_dead_reviewed.api.model.PicSumItem
import com.example.wallpaper_dead_reviewed.api.domain.entity.WallpaperLink
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class DataRepositoryTest {

    private lateinit var mockApiService: PicSumApi
    private lateinit var dataRepository: WallpaperRepostiryImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mockApiService = mock(PicSumApi::class.java)
        dataRepository = WallpaperRepostiryImpl(mockApiService)
    }

    @Test
    fun testGetImagesSuccess() = runBlocking {
        // Arrange
        val mockPicSumItem = mock(PicSumItem::class.java)
        val mockListPicSumItem = listOf(mockPicSumItem)

        // Assuming getWallpaperImages takes parameters for page and limit
        val currentPage = 1
        val limit = 300
        `when`(mockApiService.getWallpaperImages(currentPage, limit)).thenReturn(mockListPicSumItem)

        // Act
        val response = dataRepository.getImages(currentPage, limit).toList()

        // Assert
        val expectedWallpaperList: List<WallpaperLink> = mockListPicSumItem.map {
            WallpaperLink(it.downloadUrl.orEmpty())
        }

        assertEquals(expectedWallpaperList.size, response.size) // Ensure the lists have the same size

        for (i in response.indices) {
            val expectedResource = Resource.Success(expectedWallpaperList)
            val actualResource = response[i] as Resource.Success<List<WallpaperLink>>

            assertEquals(expectedResource.javaClass, actualResource.javaClass)
            assertEquals(expectedResource.data, actualResource.data)
        }
    }

    @Test
    fun testGetImagesFailure() = runBlocking {
        // Arrange
        val errorMessage = "Test message"
        val currentPage = 1
        val limit = 300
        `when`(mockApiService.getWallpaperImages(currentPage, limit)).thenThrow(Exception(errorMessage))

        // Act
        val result = dataRepository.getImages(currentPage, limit).toList()

        // Assert
        val expectedErrorResource = Resource.Error<List<WallpaperLink>>(null, errorMessage)

        assertEquals(expectedErrorResource.javaClass, result[0].javaClass)

        if (expectedErrorResource is Resource.Error && result[0] is Resource.Error) {
            assertEquals(expectedErrorResource.message, (result[0] as Resource.Error).message)
        } else {
            fail("Expected and actual are not Resource.Error instances")
        }
    }
}