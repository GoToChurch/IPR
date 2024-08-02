package pages;

import annotations.Page;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

@Page(url = "https://www.google.com/")
public class GoogleStartPage {
    SelenideElement searchInput = $x("//textarea[@aria-label='Найти']");
    SelenideElement searchButton = $x("(//input[@value='Поиск в Google'])[2]");
    SelenideElement googleLogo = $x("//img[@alt='Google']");

    public SearchResultPage search(String searchValue) {
        searchInput.setValue(searchValue);
        googleLogo.click();
        searchButton.click();

        return new SearchResultPage();
    }


}
