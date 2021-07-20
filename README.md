## 一、使用

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

## 二、设计思路

一个完整的RecyclerView的adapter包含两部分，第一部分是视图创建和视图数据的绑定，通过定义一个IRecyclerController控制器来控制规则，第二个部分是数据的控制，通过定义IRecyclerModel数据处理器来处理数据。

IRecyclerController中只定义了三个方法，即视图的创建和数据的绑定。其直接子类包含LayoutController和BindingController,前者使用布局来创建视图和数据赋值，后者使用ViewBinding来实现目的，在最初的RecyclerAdapter中未直接持有IRecyclerController的子类，因为考虑到如果直接继承则不必多此一举的设置一个IRecyclerController类，如果是多布局则会不止一个IRecyclerController类，故因应后者，创建了实现RecyclerAdapter类的PolyRecyclerAdapter。

PolyRecyclerAdapter构造方法中有两个参数，第一个是一个IRecyclerController的不定参数，第二个是根据数据和位置生成Type的方法，默认是第一个参数的对应下标，（通过反射来获取IRecyclerController与position的关系）

而对前者，在RecyclerAdapter中定义了一个静态方法create(IRecyclerController)来直接生成一个adapter，这是为了复用项目中可能由于多布局产生的IRecyclerController。

IRecyclerModel定义了四个方法，数据的count,所有数据的集合，对应位置的数据和提交新数据。子类CollectionsModel使用一个可变集合MutableList来控制，子类DiffUtilModel则通过DiffUtils来实现，使用过程中发现需要更为细致的数据控制与处理，比如增删改一个数据，因为DiffUtilModel使用的是不可变的集合，故新建了一个CollectionModifiedModel继承CollectionsModel,实现数据的处理