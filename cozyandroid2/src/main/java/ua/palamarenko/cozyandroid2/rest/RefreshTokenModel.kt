package ua.palamarenko.cozyandroid2.rest


class RefreshTokenModel(val processedRequest: () -> String) {

    private val hashMap = HashMap<String, String>()

    @Synchronized
    fun getNewToken(refreshToken: String): String {
        return if (hashMap[refreshToken] == null) {
            val token = processedRequest.invoke()
            hashMap[refreshToken] = token
            token
        } else {
            hashMap[refreshToken]!!
        }
    }
}