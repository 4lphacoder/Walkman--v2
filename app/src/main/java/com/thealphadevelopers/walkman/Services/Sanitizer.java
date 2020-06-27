package com.thealphadevelopers.walkman.Services;

import com.vdurmont.emoji.EmojiParser;
import org.jsoup.Jsoup;

// THIS CLASS IS USED TO REMOVE HTML REFERENCE CODES ,NON-ASCII CHARS, IRRELEVANT INFORMATION FROM
// GIVEN STRING. USED TO MAKE A STRING MORE APPEALING TO DISPLAY.

public class Sanitizer {
    // INPUTS A STRING --> CAPITALISED ALL CHARS WHICH COMES AFTER SPACE
    private static String toCapitalizeFirstLetters(String str) {
        if (str == null) return new String();

        StringBuilder capitalizeFirstLetterString = new StringBuilder(str.length());
        for(int idx=0;idx<str.length();idx++) {
            if(idx == 0 || Character.isSpaceChar(str.charAt(idx-1)))
                capitalizeFirstLetterString.append(Character.toUpperCase(str.charAt(idx)));
            else
                capitalizeFirstLetterString.append(str.charAt(idx));
        }
        return capitalizeFirstLetterString.toString();
    }

    // INPUTS A STRING --> CONVERTED TO LOWERCASE --> CAPITALISED ALL CHARS
    // WHICH COMES AFTER SPACE, '(' AND '['
    public static String toTitleCase(String str) {
        if (str == null) return new String();

        str = str.toLowerCase();
        int length = str.length();
        boolean nextTitleCase = true;
        StringBuilder titleCaseStr = new StringBuilder(length);

        for(char ch : str.toCharArray()) {
            if(Character.isSpaceChar(ch) || ch == '(' || ch == '['
                    || ch == '.' || ch == '-' || ch == '\'' || ch == '\"'
                    ||Character.isDigit(ch)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                ch = Character.toUpperCase(ch);
                nextTitleCase = false;
            }
            titleCaseStr.append(ch);
        }

        return titleCaseStr.toString();
    }

    public static String getSanitizedString(String str) {
        if (str == null) return new String();

        // DECODING HTML REFERENCE CODES LIKE &amp; AND OTHERS TO UTF-8 CHARSET
        str = Jsoup.parse(str).text();
        // REMOVING EMOJIS FROM STRING
        str = EmojiParser.removeAllEmojis(str);

        // REMOVING SOME NON-USEFUL INFO FROM STR
        str = str.replaceAll("[|/~\"]"," ");
        str = str.replaceAll("[\\[\\]]","");
        str = str.replaceAll("(?i)official youtube channel","");
        str = str.replaceAll("(?i)youtube channel","");
        str = str.replaceAll("\"[^-_/.,\\\\p{L}0-9]+\",\"\"","");
        str = str.trim().replaceAll("\\s+"," ");

        return toCapitalizeFirstLetters(str);
    }

    // INPUTS A STRING --> SANITIZE IT --> CONVERTED INTO TITLE-CASE
    // --> SHORTEN THE LENGTH TO 27 CHARS
    public static String getSongTitle(String str) {
        if (str == null) return new String();

        str = str.replaceAll("(?i)full video song","");
        str = str.replaceAll("(?i)official Music video","");
        str = str.replaceAll("(?i)exclusive Music video","");
        str = str.replaceAll("(?i)full song","");
        str = str.replaceAll("(?i)blockbuster song","");
        str = str.replaceAll("(?i)full audio","");
        str = str.replaceAll("(?i)full video","");
        str = str.replaceAll("(?i)lyrical","");
        str = str.replaceAll("(?i)latest song","");
        str = str.replaceAll("(?i)latest punjabi song","");
        str = str.replaceAll("(?i)official video","");
        str = str.replaceAll("(?i)official audio","");
        str = str.replaceAll("(?i)hd","");
        str = str.replaceAll("(?i)official:","");
        str = str.replaceAll("(?i)official","");
        str = str.replaceAll("(?i)lyrics","");
        str = str.replaceAll("(?i)audio","");
        str = str.replaceAll("(?i)video","");
        str = str.replaceAll("(?i)music","");
        str = str.replaceAll("[()]","");

        str = getSanitizedString(str);
        str = toTitleCase(str);
        if (str.length() >= 27)
            str = str.substring(0,27 - 3) + "...";
        return str;
    }

    public static String getFormattedDate(String str) {
        // HENCE, YOUTUBE GIVES DATE-TIME IN THIS FORMAT : YYYY-MM-DDTHH:MM:SS.SSSZ
        // FOR EXAMPLE : 2019-12-02T11:30:18.000Z
        String date[] = str.substring(0,str.indexOf('T')).split("-");
        String formattedDate = new String();
        for (int idx=date.length-1; idx >= 0 ; idx--)
            if (idx != 0)       formattedDate += date[idx] + "/";
            else                formattedDate += date[idx];

        return formattedDate;
    }
}
