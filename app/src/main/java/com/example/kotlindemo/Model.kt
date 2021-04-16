package com.example.kotlindemo

class Model {

    lateinit var key: String
    lateinit var name: String
    lateinit var thumbnailUrl: String
    lateinit var heroImageUrl: String
    lateinit var iconUrl: String
    lateinit var capacity: String



    constructor()
    constructor(key: String, name: String, thumbnailUrl: String,heroImageUrl: String,iconUrl: String, capacity: String) {
        this.key = key
        this.name = name
        this.thumbnailUrl = thumbnailUrl
        this.capacity = capacity
    }
}