package Controllers;


import io.javalin.http.Handler;

public interface IController {
    Handler getAll();
    Handler getById();
    Handler getByName();
    Handler create();
    Handler update();
    Handler delete();
}
