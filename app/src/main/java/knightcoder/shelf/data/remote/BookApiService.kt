package knightcoder.shelf.data.remote

import knightcoder.shelf.data.remote.dto.BookDto
import retrofit2.Response
import retrofit2.http.GET

interface BookApiService {

    @GET("/api/books")
    suspend fun getAllBooks(): Response<List<BookDto>>
}