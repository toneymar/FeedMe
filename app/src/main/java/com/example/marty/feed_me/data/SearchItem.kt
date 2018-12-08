package com.example.marty.feed_me.data

class SearchItem{

    lateinit var title: String
    lateinit var picURL: String
    lateinit var webURL: String

    constructor(title: String, picURL : String, webURL : String){
        this.picURL = picURL
        this.title = title
        this.webURL = webURL
    }
}