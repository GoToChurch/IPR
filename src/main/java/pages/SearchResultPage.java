package pages;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$$x;

public class SearchResultPage {
    String searchResults = "//div[@lang]//div[@data-sncf='2' or @data-sncf='1']//span[contains(., '%s')]";

    public SearchResultPage checkIfUfcWordPresentsInSearchResults() {
        checkIfWordPresentsInSearchResults("UFC");

        return this;
    }

    public SearchResultPage checkIfWordPresentsInSearchResults(String word) {
        $$x(String.format(searchResults, word)).shouldHave(sizeGreaterThan(0));

        return this;
    }
}
