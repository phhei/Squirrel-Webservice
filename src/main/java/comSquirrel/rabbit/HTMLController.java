package comSquirrel.rabbit;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class HTMLController {

    @RequestMapping(value = {"/","/home"}, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String index() {
        return "<head>" +
                "<title>Philipp and Waleed presents</title>" +
                "</head>" +
                "<body>" +
                "<h1>Welcome :)</h1>" +
                "<img src=\"http://www.exterminatorstatenisland.com/large_image/squirrel/squirrel4.jpg\" />" +
                "<a href=\"./observer\">Observe the Frontier!</a>" +
                "</body>";
    }
}
