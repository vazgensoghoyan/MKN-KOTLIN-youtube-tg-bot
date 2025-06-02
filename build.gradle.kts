plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization").version("1.6.21")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // TelegramBotAPI
    implementation("dev.inmo:tgbotapi:24.0.2")
    // implementation("dev.inmo:tgbotapi․core:9.0.0")
    // implementation("dev.inmo:tgbotapi․utils:9.0.0")

    // Ktor (HTTP-клиент)
    implementation("io.ktor:ktor-client-core:2.3.7") // Основной модуль
    implementation("io.ktor:ktor-client-cio:2.3.7") // Асинхронный движок
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7") // Для JSON
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7") // Сериализация

    // Kotlinx Serialization (для парсинга JSON)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // logging realization
    implementation("ch.qos.logback:logback-classic:1.5.18")

    // test
    testImplementation(kotlin("test"))
    testImplementation("io.github.serpro69:kotlin-faker:1.12.0") // Генерация тестовых данных
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
