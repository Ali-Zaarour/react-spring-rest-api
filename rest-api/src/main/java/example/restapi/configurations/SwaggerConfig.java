package example.restapi.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Uni App",
                        email = "example@outlook.com",
                        url = "https://example-url"),
                description = "OpenApi documentation for university API",
                title = "OpenApi specification uni-api",
                version = "1.0",
                license = @License(name = "Licence name",url = "https://example-url"),
                termsOfService = "Terms of service"
        ),
        servers ={
                @Server(description = "Local Env",url = "http://localhost:8080")
                //@Server(description = "Prod Enc",url = "https://prod-ex.com")
        }
        //if all endpoint is secure 100% add this as global config
        //else set annotation on every secure controller
        //security = @SecurityRequirement(name = "bearerAuth")

)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig{

}
