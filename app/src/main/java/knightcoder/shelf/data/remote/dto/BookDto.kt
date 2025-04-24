package knightcoder.shelf.data.remote.dto

data class BookDto(
    val ISBN: String,
    val Notes: List<String>,
    val Pages: Int,
    val Publisher: String,
    val Title: String,
    val Year: Int,
    val created_at: String,
    val handle: String,
    val id: Int,
    val villainDtos: List<VillainDto>
)