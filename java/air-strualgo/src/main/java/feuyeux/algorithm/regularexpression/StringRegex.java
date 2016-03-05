package feuyeux.algorithm.regularexpression;

public class StringRegex {
    public static void main(final String[] args) {
        final String regex = "\\w+@.+\\..+";
        final boolean b = "feuyeux@gmaill.com".matches(regex);
        System.out.println(b);
    }
}
