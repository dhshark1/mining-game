package org.losttribe.miningGame;

public enum LangEnum {
    ENGLISH,
    HEBREW;

    public LangEnum convert(String st) {
        if (st.equalsIgnoreCase("HEBREW")) {
            return HEBREW;
        } else {
            return ENGLISH;
        }
    }
}
