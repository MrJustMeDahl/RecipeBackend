package Security;

import DTO.PersonDTO;
import Exceptions.APIException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.Date;

public class TokenVerifier {

    private String KEY = System.getenv("KEY");
    public SignedJWT verifyToken(String token) throws ParseException, JOSEException, APIException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(KEY.getBytes());

        if(!signedJWT.verify(verifier)){
            throw new APIException(401, "Your token is not valid");
        }
        return signedJWT;
    }

    public PersonDTO getPersonDetailsFromToken(JWTClaimsSet claimsSet) throws APIException {
        if(new Date().after(claimsSet.getExpirationTime())){
            throw new APIException(401, "Your token has expired");
        }

        int ID = Integer.parseInt(claimsSet.getClaim("id").toString());
        String name = claimsSet.getClaim("name").toString();
        String role = claimsSet.getClaim("role").toString();

        return new PersonDTO(ID, name, role);
    }
}
