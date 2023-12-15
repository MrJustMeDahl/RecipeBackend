package Exceptions;

import io.javalin.http.Context;

import java.time.LocalDateTime;

public class ExceptionHandler {

    public void apiExceptionHandler(APIException e, Context context) {
            context.status(e.getStatusCode());
            context.json(new ErrorMessage(e.getStatusCode(), e.getMessage(), LocalDateTime.now().toString()));
    }

    public void numberFormatExceptionHandler(NumberFormatException e, Context context){
            context.status(400);
            context.json(new ErrorMessage(400, e.getMessage(), LocalDateTime.now().toString()));
    }

    public void exceptionHandler(Exception e, Context context){
            context.status(500);
            context.json(new ErrorMessage(500, e.getMessage(), LocalDateTime.now().toString()));
    }
}
