package Security;

public enum RoutingRoles implements io.javalin.security.RouteRole{

    ANYONE("anyone"), ADMIN("admin"), READER("reader"), AUTHOR("author");

    private String role;

    RoutingRoles(String role) {
        this.role = role;
    }

    @Override
    public String toString(){
        return role;
    }
}
