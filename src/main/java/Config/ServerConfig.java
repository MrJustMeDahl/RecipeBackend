package Config;

import Controllers.AccessManagerController;
import Routing.Routes;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.plugin.bundled.RouteOverviewPlugin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfig {

    private static AccessManagerController accessManager = new AccessManagerController();

    public static void startJavalinServer(Javalin app, int port){
        app.updateConfig(ServerConfig::javalinConfiguration);
        Routes routes = new Routes();
        app.before(ServerConfig::corsConfig);
        app.options("/*", ServerConfig::corsConfig);
        app.routes(routes.getRoutes(app));
        app.start(port);
    }

    public static void corsConfig(Context ctx) {
        ctx.header("Access-Control-Allow-Origin", "*");
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true");
    }

    private static void javalinConfiguration(JavalinConfig config){
        config.routing.contextPath = "/api";
        config.http.defaultContentType = "application/json";
        config.plugins.register(new RouteOverviewPlugin("/"));
        config.accessManager(accessManager::accessManagerHandler);
    }

    private static void MockDB(){
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE public.persons RESTART IDENTITY cascade;").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE public.recipes_tags RESTART IDENTITY cascade;").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE public.recipes RESTART IDENTITY cascade;").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE public.tags RESTART IDENTITY cascade;").executeUpdate();
            em.createNativeQuery("INSERT INTO public.tags (name) VALUES ('tag1');").executeUpdate();
            em.createNativeQuery("INSERT INTO public.tags (name) VALUES ('tag2');").executeUpdate();
            em.createNativeQuery("INSERT INTO public.tags (name) VALUES ('tag3');").executeUpdate();
            em.createNativeQuery("INSERT INTO public.persons (name, email, role) VALUES ('navn1','email1','role1');").executeUpdate();
            em.createNativeQuery("INSERT INTO public.persons (name, email, role) VALUES ('navn2','email2','role2');").executeUpdate();
            em.createNativeQuery("INSERT INTO public.persons (name, email, role) VALUES ('navn3','email3','role3');").executeUpdate();
            em.createNativeQuery("INSERT INTO public.recipes (name, description, ingredients, instructions, author_id) VALUES ('recipe1','description1','ingredients1','instructions1', 1);").executeUpdate();
            em.createNativeQuery("INSERT INTO public.recipes (name, description, ingredients, instructions, author_id) VALUES ('recipe2','description2','ingredients2','instructions2', 2);").executeUpdate();
            em.createNativeQuery("INSERT INTO public.recipes (name, description, ingredients, instructions, author_id) VALUES ('recipe3','description3','ingredients3','instructions3', 3);").executeUpdate();
            em.createNativeQuery("INSERT INTO public.recipes_tags (recipes_id, tags_id) VALUES (1,1);").executeUpdate();
            em.createNativeQuery("INSERT INTO public.recipes_tags (recipes_id, tags_id) VALUES (2,2);").executeUpdate();
            em.createNativeQuery("INSERT INTO public.recipes_tags (recipes_id, tags_id) VALUES (3,3);").executeUpdate();
            em.getTransaction().commit();
        }
    }

    public static void main(String[] args) {

        Javalin app = Javalin.create();
        startJavalinServer(app, 7070);
        JavalinConfig config = new JavalinConfig();
        javalinConfiguration(config);
        MockDB();

    }
}