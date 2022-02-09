package fr.lernejo.fileinjector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Launcher {

    public static void main(String[] args) throws Exception {
        try (AbstractApplicationContext springContext = new AnnotationConfigApplicationContext(Launcher.class)) {

            String path = Arrays.stream(args).findFirst().orElseThrow(() -> new Exception("invalid number of parameter"));
            File file = Paths.get(path).toFile();

            ObjectMapper objectMapper = new ObjectMapper();
            List<GameModel> gameModels =
                objectMapper.readValue(file, new TypeReference<>() {
            });

            RabbitTemplate template = springContext.getBean(RabbitTemplate.class);
            template.setMessageConverter(new Jackson2JsonMessageConverter());

            for (GameModel game : gameModels) {
                template.convertAndSend("",
                    "game_info",
                    game,
                    message -> {
                        message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
                        message.getMessageProperties().setHeader("game_id", game.id);
                        return message;
                    }
                );
            }
        }
    }
}
