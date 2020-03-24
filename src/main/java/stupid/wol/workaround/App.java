package stupid.wol.workaround;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class App {
    private static Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        Spark.port(8080);
        Spark.get("/wol", (req, res) -> {
            LOGGER.info("Received request: {}", req.userAgent());
            return "Hello world";
        });

        Spark.post("/wol", (req, res) -> {
            LOGGER.info("{}", req.body());
            return "OK";
        });
    }
}
