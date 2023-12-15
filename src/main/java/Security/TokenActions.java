package Security;

import DTO.PersonDTO;
import Exceptions.APIException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public class TokenActions {

    private static TokenActions instance;
    private TokenVerifier verifier = new TokenVerifier();
    private TokenCreator creator = new TokenCreator();

    public static TokenActions getInstance() {
        if(instance == null){
            instance = new TokenActions();
        }
        return instance;
    }

    public PersonDTO verifyToken(String token) throws APIException{
        try {
            SignedJWT signedJWT = verifier.verifyToken(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            PersonDTO person = verifier.getPersonDetailsFromToken(claimsSet);
            return person;
        } catch (ParseException | JOSEException e){
            throw new APIException(401, e.getMessage());
        }
    }
}
