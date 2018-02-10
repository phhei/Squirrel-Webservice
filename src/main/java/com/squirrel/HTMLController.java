package com.squirrel;

import com.squirrel.Utilities.HTMLReader;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;
import java.util.Optional;

/**
 * A simple HTML page generator/ connector
 * @author Philipp Heinisch
 */
@RestController
public class HTMLController {

    /**
     * @return a HOME-page (just an HTML entry point)
     */
    @RequestMapping(value = {"/","/home"}, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String index() {
        return "<head>" +
                "<title>Crawler Subgroup (Philipp and Waleed) presents</title>" +
                "</head>" +
                "<body>" +
                "<h1>Welcome :)</h1>" +
                "<ul>" +
                "<li>Observe the <a href=\"./pages/index.html\">Frontier!</a> (<a href=\"./observer/html\">without Javascript</a>)</li>" +
                "<li><a href=\"./observer\">Backbone</a> (<a href=\"./observer/stat\">&gt;&gt;</a>)</li>" +
                "</ul>" +
                "<img src=\"http://easyscienceforkids.com/wp-content/uploads/2013/04/squirrel-eating-nut.jpg\" />" +
                "</body>";
    }

    /**
     * @return a simple static HTML page (only for testing)
     */
    @RequestMapping(value = "/staticPage", produces = MediaType.TEXT_HTML_VALUE)
    public String getStaticPage() {
        return HTMLReader.getHTMLErrorPage("https://www.tutorialspoint.com/spring/spring_static_pages_example.htm");
    }

    /**
     * WORKAROUND - because the common way doesn't work
     *
     * @param model   {@link Model} - we don't use it
     * @param request {@link HttpServletRequest} - we can read the input address string from the browser
     * @return if the asked file exists: the file content, but not the original file itself
     * if not: an ERROR-Page
     */
    @RequestMapping(value = "/pages/**", produces = MediaType.ALL_VALUE, method = RequestMethod.GET)
    public String getStaticCode(Model model, HttpServletRequest request) {
        String path = "./WEB-INF" + request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        File filepath = new File(path.substring(0, path.lastIndexOf('/')));

        if (filepath.isDirectory()) {
            final String name = path.substring(path.lastIndexOf('/')+1, path.length());
            Optional<File> searchedFile = Arrays.stream(filepath.listFiles()).filter(f -> f.getName().startsWith(name)).findFirst();
            if(searchedFile.isPresent()) {
                return HTMLReader.getText(searchedFile.get().getAbsolutePath());
            }
            return HTMLReader.getHTMLErrorPage("The path " + path + " is  not existing!");
        } else {
            return HTMLReader.getHTMLErrorPage("--ERROR -- No directory [" + path + "] " + request.getAttribute(HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE));
        }
    }
}
