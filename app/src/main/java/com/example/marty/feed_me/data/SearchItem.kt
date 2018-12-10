package com.example.marty.feed_me.data

data class SearchItem(var title : String?,
                      var picURL : String?,
                      var webURL : String?,
                      var isFavorited : Boolean)


/*class SearchItem(title : String?, picURL : String?, webURL : String?){

    var title : String?
    var picURL : String?
    var webURL : String?

    init {
        this.title = title
        this.picURL = picURL
        this.webURL = webURL
    }
}*/