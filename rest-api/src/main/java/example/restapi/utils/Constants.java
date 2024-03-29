package example.restapi.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static  final String APP_USER_DTO = "appUserDTO";

    public static  final String SECURITY_ATTRIBUTE_TOKEN ="token";

    public static final String SECURITY_ATTRIBUTE_AUTHORIZATION = "Authorization";

    public static final  String JWT_SUBJECT = "User Details";

    public static  final  String JWT_CLAIM_USERNAME = "username";

    public static final String JWT_ISSUER = "spring-demo/rest-api/uni-api";

    public static  final  String GRANTED_AUTHORITY_FIRST_KEY  = "ROLE_";

}
