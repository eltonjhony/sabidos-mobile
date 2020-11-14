package com.sabidos.data.local.cache

class CacheHandler(
    private val accountCache: AccountCache,
    private val avatarCache: AvatarCache,
    private val categoryCache: CategoryCache
) {

    fun invalidateAll() {
        accountCache.invalidate()
        avatarCache.invalidate()
        categoryCache.invalidate()
    }

}