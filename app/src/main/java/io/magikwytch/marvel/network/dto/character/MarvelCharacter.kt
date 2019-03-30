package io.magikwytch.marvel.network.dto.character

import io.magikwytch.marvel.network.dto.Image
import io.magikwytch.marvel.network.dto.comic.ComicList

data class MarvelCharacter(val id: Int, val name: String, val thumbnail: Image, val comics: ComicList)
