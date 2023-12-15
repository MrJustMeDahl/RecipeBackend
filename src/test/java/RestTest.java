import Config.HibernateConfig;
import Config.ServerConfig;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestTest {

    private EntityManagerFactory emf;
    private Javalin app;

    @BeforeAll
    void beforeAll(){
        HibernateConfig.setForTest(true);
        emf = HibernateConfig.getEntityManagerFactoryConfig();
        app = Javalin.create();
        ServerConfig.startJavalinServer(app, 7070);
    }

    @BeforeEach
    void setup(){
        //TODO: Clear and populate database
    }

    @AfterAll
    void tearDown(){
        HibernateConfig.setForTest(false);
        app.stop();
    }
}
