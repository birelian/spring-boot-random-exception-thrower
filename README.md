# MisterBean application
Congratulations! You have found the most useless application in the world...

### What does MisterBean do?
MisterBean may throw a random exception when a method annotated with `@MisterBean` is executed. The list of possible
exceptions that may be thrown must be defined by the user in the `application.properties`.

### How does it work?
MisterBean uses `Spring AOP` to capture the execution of a `@MisterBean` method and uses reflection to instantiate the
desired exception and then it just throws it.

### How do I use it?

Just follow the next steps:

1. Clone the repo.
2. Install the application in your local Maven repository executing `mvn clean install`. Be careful. Random exceptions
may be thrown while compiling. Noooo, just joking.
3. Add the dependency to your Spring Boot project:
    ```
    <dependency>
	    <groupId>net.birelian</groupId>
	    <artifactId>mister-bean</artifactId>
	    <version>1.0</version>
    </dependency>
    ```
4. Enable it in your application. To do so, just include `@EnableMisterBean` annotation in a Spring configuration class.
For example, it can be set on the Main application class:
    ```
    @SpringBootApplication
    @EnableMisterBean
    public class MisterBeanTesterApplication {

        public static void main(String[] args) {
            SpringApplication.run(MisterBeanTesterApplication.class, args);
        }
    }
    ```

5. Annotate with `@MisterBean` the methods you want to test.
6. Configure, in the `application.properties`, the exceptions that may be thrown and the message that should be used to
create them:
    ```
    misterBean.exceptions=java.lang.NullPointerException,java.io.FileNotFoundException
    misterBean.exceptionMessage=Powered by the useless Mister Bean application
    ```
7. Launch yout application and see what happens.

### ToDo
+ Ability to override the exception list in the annotation `@MisterBean` itself.
+ Make it useful?
