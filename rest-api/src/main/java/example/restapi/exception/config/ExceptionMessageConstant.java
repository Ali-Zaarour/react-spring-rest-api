package example.restapi.exception.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessageConstant {

    public final String USER_APP_DISABLED_CONTACT_MANAGER = "user.app.still.disabled.contact.your.manager";
    public final String  DATA_INTEGRITY_VIOLATION = "data.conflict.detected.while.creating.new.data";
    public final String USERNAME_NOT_FOUND = "username.not.found";
    public final String ENTITY_NOT_FOUND = "entity.not.found";
    public final String SQL_BATCH_OPERATION = "error.while.saving.entities";

}
