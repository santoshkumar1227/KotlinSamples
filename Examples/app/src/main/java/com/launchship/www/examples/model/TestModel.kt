package com.launchship.www.examples.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Response {
    @SerializedName("numFound")
    @Expose
    var numFound: Int? = null
    @SerializedName("start")
    @Expose
    var start: Int? = null
    @SerializedName("maxScore")
    @Expose
    var maxScore: Double? = null
    @SerializedName("docs")
    @Expose
    var docs: List<Doc>? = null
}

class Example {
    @SerializedName("response")
    @Expose
    var response: Response? = null
}

class Doc : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("journal")
    @Expose
    var journal: String? = null
    @SerializedName("eissn")
    @Expose
    var eissn: String? = null
    @SerializedName("publication_date")
    @Expose
    var publicationDate: String? = null
    @SerializedName("article_type")
    @Expose
    var articleType: String? = null
        @SerializedName("author_display")
    @Expose
    var authorDisplay: List<String>? = null
    @SerializedName("abstract")
    @Expose
    var abstract: List<String>? = null
    @SerializedName("title_display")
    @Expose
    var titleDisplay: String? = null
    @SerializedName("score")
    @Expose
    var score: Double? = null
}