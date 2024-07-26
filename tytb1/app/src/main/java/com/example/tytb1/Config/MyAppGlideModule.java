package com.example.tytb1.Config;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

@GlideModule
public final class MyAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Đặt giới hạn bộ nhớ cache
        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));

        // Đặt giới hạn bộ nhớ đĩa cache
        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100mb
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));

        // Sử dụng chế độ nén RGB_565 để tiết kiệm bộ nhớ
        builder.setDefaultRequestOptions(
                new RequestOptions()
                        .format(DecodeFormat.PREFER_RGB_565)
                        .disallowHardwareConfig() // Sử dụng cấu hình này nếu bạn gặp vấn đề với phần cứng bitmap
        );
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        // Đăng ký các thành phần bổ sung tại đây (nếu cần)
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false; // Tắt việc phân tích AndroidManifest.xml
    }
}