import com.example.placeKeeper.data.entities.PlaceEntity
import com.example.placeKeeper.domain.model.Place

fun PlaceEntity.toPlace(): Place {
    return Place(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        categoryId = categoryId,
        description = description,
        rating = rating,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Place.toPlaceEntity(): PlaceEntity {
    return PlaceEntity(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        categoryId = categoryId,
        description = description,
        rating = rating,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}