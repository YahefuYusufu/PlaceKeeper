import com.example.placeKeeper.data.entities.CategoryEntity
import com.example.placeKeeper.domain.model.Category


fun CategoryEntity.toCategory(): Category {
    return Category(
        id = id,
        name = name,
        color = color,
        iconName = iconName,
        createdAt = createdAt
    )
}

fun Category.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        color = color,
        iconName = iconName,
        createdAt = createdAt
    )
}