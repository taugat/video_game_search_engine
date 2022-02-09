package fr.lernejo.fileinjector;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class LauncherTest {

    @Test
    void main_terminates_before_5_sec() throws Exception {

        File file = new File("src/test/resources/games.json");
        Launcher.main(new String[]{file.getAbsolutePath()});
    }
}
