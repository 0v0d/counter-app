// 全サブプロジェクト・モジュールに共通する設定を書くトップレベルのビルドファイル
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.detekt)
}

detekt {
    source.setFrom(files("app/src/main/java", "app/src/test/java", "app/src/androidTest/java"))
    config.setFrom(files("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    basePath.set(rootDir)
}
