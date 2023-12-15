package Security;

import Exceptions.APIException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;

import java.util.Date;

public class TokenCreator {

    private static TokenCreator instance;
    public static TokenCreator getInstance() {
        if(instance == null){
            instance = new TokenCreator();
        }
        return instance;
    }

    public String createToken(int id, String name, String role) throws APIException {
        Date date = new Date();

        JWTClaimsSet claimsSet = createClaimsSet(id, name, role, date);
        JWSObject headerAndPayload = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(claimsSet.toJSONObject()));
        String signedToken = signToken(headerAndPayload);
        return signedToken;
    }

    private JWTClaimsSet createClaimsSet(int id, String name, String role, Date date) {
        return new JWTClaimsSet.Builder()
                .subject(name)
                .issuer(System.getenv("ISSUER"))
                .claim("id", id)
                .claim("name", name)
                .claim("role", role)
                .expirationTime(new Date(date.getTime() + Integer.parseInt(System.getenv("TOKEN.EXPIRE.TIME"))))
                .build();
    }

    private String signToken(JWSObject headerAndPayload) throws APIException{
        try{
            JWSSigner signer = new MACSigner(System.getenv("KEY").getBytes());
            headerAndPayload.sign(signer);
            return headerAndPayload.serialize();
        }catch (JOSEException e){
            throw new APIException(500, "Signing of token failed");
        }
    }
}
