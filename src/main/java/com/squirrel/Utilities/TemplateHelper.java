package com.squirrel.Utilities;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TemplateHelper {

    private static Pattern pattern;

    public static String replace(String input, Map<String, List<String>> replacements) {
        Pattern pattern = Pattern.compile("\\$\\{\\w+\\}");
        StringBuilder output = new StringBuilder(input);
        Matcher m = pattern.matcher(output.toString());

        while (m.find()) {
            String replaceStringKey = m.group().substring(2, m.group().length()-1);
            Optional<Map.Entry<String, List<String>>> match = replacements.entrySet().stream().filter(e -> e.getKey().equals(replaceStringKey)).findFirst();
            final StringBuilder replaceString = new StringBuilder("");
            match.ifPresent(matchEntry -> {
                if (matchEntry.getValue().isEmpty()) {
                    replaceString.append("No data to replace...");
                } else if (matchEntry.getValue().size() == 1) {
                    replaceString.append(matchEntry.getValue().get(0));
                } else {
                    replaceString.append("<ul>");
                    matchEntry.getValue().forEach(matchEntryElement -> replaceString.append("<li>" + matchEntryElement + "</li>"));
                    replaceString.append("</ul>");
                }
            });
            output = output.replace(m.start(), m.end(), replaceString.toString());
            m = pattern.matcher(output.toString());
        }

        return output.toString();
    }
}
