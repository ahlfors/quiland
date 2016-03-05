package feuyeux.algorithm.regularexpression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {
    private void testGroup() {
        final String regex = "\\w(\\d\\d)(\\w+)";
        final String candidate = "x99SuperJava";

        final Pattern p = Pattern.compile(regex);
        final Matcher matcher = p.matcher(candidate);
        if (matcher.find()) {
            final int gc = matcher.groupCount();
            for (int i = 0; i <= gc; i++) {
                System.out.println("group " + i + " :" + matcher.group(i));
            }
        }
    }

    private void testReplace() {
        final StringBuffer sb = new StringBuffer();
        final String replacement = "Smith";
        final Pattern pattern = Pattern.compile("Bond");
        final Matcher matcher = pattern.matcher("My name is Bond. James Bond. I would like a martini.");
        while (matcher.find()) {
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        System.out.println(sb.toString());
    }

    private void testFind() {
        final String candidate = "A Matcher examines the results of applying a pattern.";
        final String regex = "\\ba\\w*\\b";//"\\b[Aa]\\w*\\b";
        final Pattern p = Pattern.compile(regex);
        final Matcher m = p.matcher(candidate);
        String val = null;
        System.out.println("INPUT: " + candidate);
        System.out.println("REGEX: " + regex + "\r\n");
        while (m.find()) {
            val = m.group();
            System.out.println("MATCH: " + val);
        }
        if (val == null) {
            System.out.println("NO MATCHES: ");
        }
    }

    public static void main(final String[] args) {
        final PatternMatcher pm = new PatternMatcher();
        pm.testGroup();
        pm.testReplace();
        pm.testFind();
    }
}
