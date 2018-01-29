package comSquirrel;

import comSquirrel.Utilities.HTMLReader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * A simple HTML page...
 * @author Philipp Heinisch
 */
@RestController
public class HTMLController {

    @RequestMapping(value = {"/","/home"}, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String index() {
        return "<head>" +
                "<title>Philipp and Waleed presents</title>" +
                "</head>" +
                "<body>" +
                "<h1>Welcome :)</h1>" +
                "<a href=\"./observer\">Observe the Frontier!</a><br>" +
                "<img src=\"http://www.exterminatorstatenisland.com/large_image/squirrel/squirrel4.jpg\" />" +
                "</body>";
    }

    @RequestMapping(value = "/staticPage", produces = MediaType.TEXT_HTML_VALUE)
    public String redirect() {
        //https://www.tutorialspoint.com/spring/spring_static_pages_example.htm
        return HTMLReader.getHTMLErrorPage("lol");
    }

}
