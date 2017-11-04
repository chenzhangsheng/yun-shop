package springboot.controller; /**
 * Created by zhangshengchen on 2017/9/14.
 */


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import mongo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import persistence.manager.CityManager;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

@Controller
public class SampleController {
    @Autowired
    private CityManager cityManager;
    @Autowired
    private ArticleService articleService;
    @Autowired
    Configuration config;
    @ResponseBody
    public Object findOneCity(@RequestParam(value = "cityName", required = true) String cityName) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pageNo",1);
        map.put("rowCount",2);
        out.println(articleService.get(1L));
        return cityManager.findCityByName(map);
    }
    @RequestMapping("/web")
    public String freemarker(Map<String, Object> map,HttpServletRequest request) {
        try {
            //静态化
            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            Template temple=config.getTemplate("web");//获取模板
            Writer out = new OutputStreamWriter(new FileOutputStream(request.getServletContext().getRealPath("/")+"1.html"));//生成最终页面并写到文件
            temple.process(map, out);//处理
        } catch (TemplateException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally
        {
            out.close();
        }
        return "web";//返回的内容就是templetes下面文件的名称
    }
}