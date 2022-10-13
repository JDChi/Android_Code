# 相关说明
## Compose
### 基本依赖
[Adding Jetpack Compose to your app](https://developer.android.com/jetpack/compose/interop/adding)
在 app 的 build.gradle 文件里增加 Compose 的依赖：
```groovy
android {
    buildFeatures {
        // Enables Jetpack Compose for this module
        compose true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

dependencies {
    implementation 'androidx.activity:activity-compose:1.6.0'
    implementation 'androidx.compose.material:material:1.2.1'
}

```
要注意下 Compose 与 Kotlin 之间的版本关系。[Compose to Kotlin Compatibility Map](https://developer.android.com/jetpack/androidx/releases/compose-kotlin)
### UI 预览
增加依赖：
```groovy
dependencies {
    // Tooling support (Previews, etc.)
    implementation 'androidx.compose.ui:ui-tooling:1.2.1'
}
```
在想要预览的地方增加 `@Preview` 注解：
```kotlin
    @Preview
    @Composable
    private fun takeAScreenshot() {
        Button(onClick = { Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show() }) {

        }
    }
```
