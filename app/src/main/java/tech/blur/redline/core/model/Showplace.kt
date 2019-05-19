package tech.blur.redline.core.model

class Showplace(
    val id: String,
    val name: String,
    val description: String,
    val tags: ArrayList<String>,
    val geo: ArrayList<Double>,
    val address: String,
    val city: String
)
