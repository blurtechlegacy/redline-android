package tech.blur.redline.core.model

class User(
    val _id: String,
    val login: String,
    val name: String,
    val preferences: ArrayList<String>
)