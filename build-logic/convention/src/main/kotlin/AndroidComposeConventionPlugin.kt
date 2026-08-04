import com.android.build.api.dsl.CommonExtension
import internal.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val extension = extensions.getByName("android") as CommonExtension
            extension.apply {
                buildFeatures.apply {
                    compose = true
                }
            }

            dependencies {
                val bom = libs.findLibrary("androidx.compose.bom").get()
                add("implementation", platform(bom))

                add("implementation", libs.findBundle("compose").get())
                add("implementation", libs.findLibrary("androidx.activity.compose").get())

                add("implementation", libs.findBundle("lifecycle").get())
                add("implementation", libs.findBundle("navigation3").get())

                add("androidTestImplementation", platform(bom))
                add("androidTestImplementation", libs.findLibrary("androidx.compose.ui.test.junit4").get())

                add("debugImplementation", libs.findBundle("compose.debug").get())
            }
        }
    }
}