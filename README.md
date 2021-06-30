在项目根目录下添加

```groovy
allprojects {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/autught/recycleradapter")
            credentials {
                /**
                 * set environment variable GITHUB_USER corresponding to your github's username
                 * and GITHUB_PERSONAL_ACCESS_TOKEN corresponding to your github's token on your local machine
                 *
                 * alternatively, you can also creating a github.properties file within your root project
                 * and add properties gpr.usr=github's username,gpr.key=github's token
                 * PS. make sure you add this file to .gitignore for keeping the token private
                 */
                def properties = new Properties()
                properties.load(project.rootProject.file('github.properties').newDataInputStream())
                username = properties.getProperty('gpr.username') ?: System.getenv('GITHUB_USER')
                password = properties.getProperty('gpr.password') ?: System.getenv('GITHUB_PERSONAL_ACCESS_TOKEN')
                println("user:" + username)
                println("psw:" + password)
            }
        }
    }
}
```

在build.gradle下添加依赖

```
implementation "com.recyclerview:recycler-adapter:1.0.0"
```

