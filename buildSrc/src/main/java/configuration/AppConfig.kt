package configuration

object AppConfig {

    const val projectName = "template"
    const val applicationId = "template.app.id"

    const val compileSdk = 33
    const val targetSdk = 33
    const val minsdk = 26

    const val versionCode = 1
    const val versionName = "0.0.0"

    object Flavors {
        const val dimension = projectName
        object Staging {
            const val name = "staging"
            const val versionCode = AppConfig.versionCode
            const val suffix = ".${name}"
        }

        object Uat {
            const val name = "uat"
            const val versionCode = AppConfig.versionCode
            const val suffix = ".${name}"
        }

        object Production {
            const val name = "production"
            const val versionCode = AppConfig.versionCode
        }
    }

}
