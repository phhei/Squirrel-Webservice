package com.squirrel.rabbit;

import com.SquirrelWebObject;
import com.squirrel.Application;
import com.squirrel.Utilities.HTMLReader;
import com.squirrel.Utilities.TemplateHelper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class RabbitController {

    @RequestMapping(method = RequestMethod.GET, path = "/observer", produces = MediaType.APPLICATION_JSON_VALUE)
    public SquirrelWebObject observeFrontier(@RequestParam(value="id", defaultValue="n/a") String property) {
        SquirrelWebObject o;
        try {
            o = Application.listenerThread.getSquirrel(Integer.parseInt(property));
        } catch (NumberFormatException e) {
            o = Application.listenerThread.getSquirrel();
        }

        if (o == null)
            return new SquirrelWebObject();
        return o;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/observer/html", produces = MediaType.TEXT_HTML_VALUE)
    public String observerFrontierHTML(@RequestParam(value="id", defaultValue="n/a") String property) {
        SquirrelWebObject o = observeFrontier(property);

        Map<String, List<String>> stringListMap = new HashMap<>();
        stringListMap.put("numberPendingURIs", Collections.singletonList(o.getCountOfPendingURIs() + ""));
        stringListMap.put("numberCrawledURIs", Collections.singletonList(o.getCountOfCrawledURIs() + ""));
        stringListMap.put("numberWorker", Collections.singletonList(o.getCountOfWorker() + ""));
        stringListMap.put("numberDeadWorker", Collections.singletonList(o.getCountOfDeadWorker() + ""));
        stringListMap.put("pendingURIs", o.getPendingURIs());
        stringListMap.put("nextCrawledURIs", o.getNextCrawledURIs());
        List<String> IPURImap = new ArrayList<>(o.getIpStringListMap().size());
        o.getIpStringListMap().forEach((k, v) -> {
            StringBuilder vString = new StringBuilder(": ");
            v.forEach(s -> vString.append(s + ", "));
            IPURImap.add(k + vString.substring(0, vString.length()-2));
        });
        stringListMap.put("IPURImap", IPURImap);

        return TemplateHelper.replace(HTMLReader.getText("./WEB-INF/pages/index.html"), stringListMap);
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
