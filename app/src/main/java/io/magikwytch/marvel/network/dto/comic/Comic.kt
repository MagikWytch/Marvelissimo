package io.magikwytch.marvel.network.dto.comic

import io.magikwytch.marvel.network.dto.Image
import io.magikwytch.marvel.network.dto.character.CharacterList

data class Comic(val id: Int, val title: String, val description: String, val thumbnail: Image, val characters: CharacterList)