package rocks.waffle.telekt.examples.fsm

enum class Gender { MALE, FEMALE, OTHER, PARSE_ERROR }

fun parseGender(from: String?): Gender = when (from?.toLowerCase()) {
    "male" -> Gender.MALE
    "female" -> Gender.FEMALE
    "other" -> Gender.OTHER
    else -> Gender.PARSE_ERROR
}

data class UserInfo(
    val name: String? = null,
    val age: Int? = null,
    val gender: Gender? = null
)

/**
 * Simulation of data base (Don't do like that in real code)
 */
class DataBase {
    private val map = mutableMapOf<Long, UserInfo>()

    fun setName(userId: Long, name: String) {
        map[userId] = map[userId]?.copy(name = name) ?: UserInfo(name = name)
    }

    fun setAge(userId: Long, age: Int) {
        map[userId] = map[userId]?.copy(age = age) ?: UserInfo(age = age)
    }

    fun setGender(userId: Long, gender: Gender) {
        map[userId] = map[userId]?.copy(gender = gender) ?: UserInfo(gender = gender)
    }

    fun getUser(userId: Long) = map.getOrPut(userId) { UserInfo() }
}