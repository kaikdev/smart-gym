package edu.projeto.smartgymfront

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform