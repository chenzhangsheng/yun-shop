package springboot; /**
 * Created by zhangshengchen on 2017/9/15.
 */

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
//开启事务注解
@EnableTransactionManagement
//@ServletComponentScan //配置druid必须加的注解，如果不加，访问页面打不开，filter和servlet、listener之类的需要单独进行注册才能使用，spring boot里面提供了该注解起到注册作用
@ServletComponentScan   //扫描Servlet
@MapperScan("persistence.dao")
//@EnableAutoConfiguration习惯放在主方法类Application上，
// 当项目运行时，Spring容器去自动查找带特定注解的类，如：带@Entity、@Service等类。
@ComponentScan({"mongo","persistence","redis","common","springboot"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}