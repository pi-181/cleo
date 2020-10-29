package com.css.cleo.voice.language;

public enum Language {
    ENGLISH(
            "English",
            "",
            "",
            ""
    ),
    RUSSIAN(
            "Russian",
            "https://www.dropbox.com/s/k7acnt2qtizavia/ru_hmm.zip?dl=1",
            "https://www.dropbox.com/s/cjkfliaizzb14ru/ru_dictionary.zip?dl=1",
            "https://www.dropbox.com/s/hm30tnew5m86397/ru_text.zip?dl=1"
    ),
    ;

    private final String name;
    private final String acousticModelLink;
    private final String dictionaryLink;
    private final String grammarLink;

    Language(String name, String acousticModelLink, String dictionaryLink, String grammarLink) {
        this.name = name;
        this.acousticModelLink = acousticModelLink;
        this.dictionaryLink = dictionaryLink;
        this.grammarLink = grammarLink;
    }

    public String getName() {
        return name;
    }

    public String getAcousticModelLink() {
        return acousticModelLink;
    }

    public String getDictionaryLink() {
        return dictionaryLink;
    }

    public String getGrammarLink() {
        return grammarLink;
    }

    public static Language defaultLanguage() {
        return RUSSIAN;
    }
}
