package test;

import annotations.Page;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import annotations.Ui;
import pages.GoogleStartPage;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.open;

@Ui
public class GoogleTest {

    @ParameterizedTest
    @RepeatedTest(value = 2)
    @MethodSource("queryArguments")
    public void test1(String query) {
        GoogleStartPage swerve = new GoogleStartPage();

        open(swerve.getClass().getAnnotation(Page.class).url());

        swerve
                .search(query)
                .checkIfUfcWordPresentsInSearchResults();
    }

    @ParameterizedTest(name = "Поиск в гугле запроса {0} и проверка, что никнейм бойца {1} содержится хотя бы в одном результате")
    @RepeatedTest(value = 2)
    @MethodSource("queryArguments2")
    public void test2(String query, String check) {
        GoogleStartPage googleStartPage = new GoogleStartPage();

        open(googleStartPage.getClass().getAnnotation(Page.class).url());

        googleStartPage
                .search(query)
                .checkIfWordPresentsInSearchResults(check);
    }

    private static Stream<Arguments> queryArguments() {
        return Stream.of(
                "Michael Bisping",
                "Jon Jones",
                "Michael Chandler"
        ).map(Arguments::of);
    }

    private static Stream<Object[]> queryArguments2() {
        return Stream.of(
                new Object[]{"Michael Bisping", "UFC"},
                new Object[]{"Jon Jones", "Bones"},
                new Object[]{"Michael Chandler", "Iron"}
        );
    }
}
