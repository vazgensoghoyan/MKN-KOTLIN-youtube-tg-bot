plugins {
    kotlin("jvm") version "2.1.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // TelegramBotAPI
    implementation("dev.inmo:tgbotapi:9.0.0")
    //implementation("dev.inmo:tgbotapi․core:9.0.0")
    //implementation("dev.inmo:tgbotapi․utils:9.0.0")

    // Ktor (HTTP-клиент)
    implementation("io.ktor:ktor-client-core:2.3.3")                // Основной модуль
    implementation("io.ktor:ktor-client-cio:2.3.3")                 // Асинхронный движок
    implementation("io.ktor:ktor-client-content-negotiation:2.3.3") // Для JSON
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.3") // Сериализация

    //////////////////////////////// YOUTUBE API
    // Google API Client для общих операций
    implementation("com.google.api-client:google-api-client:2.2.0")
    // YouTube Data API v3
    implementation("com.google.apis:google-api-services-youtube:v3-rev20230816-2.0.0")
    // OAuth2 для аутентификации
    implementation("com.google.auth:google-auth-library-oauth2-http:1.19.0")
    // Дополнительно: для удобства работы с JSON
    implementation("com.google.code.gson:gson:2.10.1")
    //////////////////////////////////////////////

    // Kotlinx Serialization (для парсинга JSON)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // logging
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.11")

    testImplementation(kotlin("test"))

    //api("org.slf4j:slf4j-api:2.0.9")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}