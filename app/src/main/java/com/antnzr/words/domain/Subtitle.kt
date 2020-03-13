package com.antnzr.words.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Subtitle(
    @Expose
    @SerializedName("MovieName")
    val name: String,
    @Expose
    @SerializedName("MovieReleaseName")
    val releaseName: String,
    @Expose
    @SerializedName("SubLanguageID")
    val lang: String,
    @Expose
    @SerializedName("SubRating")
    val subRating: String,
    @Expose
    @SerializedName("SubFileName")
    val subFileName: String,
    @Expose
    @SerializedName("SeriesSeason")
    val seriesSeason: String,
    @Expose
    @SerializedName("SeriesEpisode")
    val seriesEpisode: String,
    @Expose
    @SerializedName("MovieKind")
    val movieKind: String,
    @Expose
    @SerializedName("IDMovieImdb")
    val idMovieImdb: String,
    @Expose
    @SerializedName("SubEncoding")
    val subEncoding: String,
    @Expose
    @SerializedName("ZipDownloadLink")
    val zipDownloadLink: String
)