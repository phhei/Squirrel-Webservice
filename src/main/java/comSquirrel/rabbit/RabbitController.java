package comSquirrel.rabbit;

import com.SquirrelWebObject;
import comSquirrel.Application;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RabbitController {

    @RequestMapping(method = RequestMethod.GET, path = "/observer", produces = MediaType.APPLICATION_JSON_VALUE)
    public SquirrelWebObject observeFrontier() {
        SquirrelWebObject o = Application.listenerThread.getData();
        if (o == null)
            return new SquirrelWebObject();
        return o;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/observer/stat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String observeFrontierStat(@RequestParam(value="prop", defaultValue="World") String property) {
        StringBuilder ret = new StringBuilder();
        switch (property) {
            case "ls":
                //TODO: dataQueue.forEach(c -> ret.append(c.toString()));
                break;
            case "lsc":
                //TODO: ret.append("You have ").append(dataQueue.size()).append(" SquirrelWebObjects");
                break;
            default:
                ret.append("Please set another prop param");
                break;
        }

        return ret.toString();
    }
}
