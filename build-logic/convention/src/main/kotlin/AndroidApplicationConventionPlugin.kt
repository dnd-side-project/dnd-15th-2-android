import com.android.build.api.dsl.ApplicationExtension
import internal.configureKotlinAndroid
import internal.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx.core.ktx").get())
                add("implementation", libs.findLibrary("timber").get())

                add("testImplementation", libs.findBundle("test.unit").get())
                add("androidTestImplementation", libs.findBundle("test.android").get())
            }
        }
    }
}