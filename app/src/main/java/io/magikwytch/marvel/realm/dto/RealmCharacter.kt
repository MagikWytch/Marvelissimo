package io.magikwytch.marvel.realm.dto

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class RealmCharacter : RealmObject() {

    @PrimaryKey
    var id: Int = 0
    var name: String? = null
    var thumbnail: String? = null
}