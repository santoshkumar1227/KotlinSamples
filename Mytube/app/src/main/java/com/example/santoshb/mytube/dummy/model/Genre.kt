package com.example.santoshb.mytube.dummy.model

import io.realm.RealmModel
import io.realm.annotations.RealmClass

data class Genre(var typeName: String, var list: List<Video>? = null)
