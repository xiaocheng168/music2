package cn.zcc.music2.info.music

class LyricsInfo(id: Long, lyrics: String) {
    val id: Long
    val lyrics: String
    init {
        this.id = id
        this.lyrics = lyrics
    }
}