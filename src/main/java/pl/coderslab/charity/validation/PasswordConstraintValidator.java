package pl.coderslab.charity.validation;

import lombok.SneakyThrows;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword arg0) {
    }

    @SneakyThrows
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        LengthComplexityRule lengthComplexityRule = new LengthComplexityRule();
        lengthComplexityRule.addRules("[0,0]", new LengthRule(0));
        lengthComplexityRule.addRules("[3,10]", new LengthRule(3, 10));

        URL resource = this.getClass().getClassLoader().getResource("messages_pl.properties");
        Properties props = new Properties();
        props.load(new FileInputStream(resource.getPath()));
        MessageResolver resolver = new PropertiesMessageResolver(props);

        /**Same in different way */
//        Properties props = new Properties();
//        props.load(new FileInputStream("/home/tomek/Workspace/Portfolio/Charity/src/main/resources/messages_pl.properties"));
//        MessageResolver resolver = new PropertiesMessageResolver(props);
        /***/

        PasswordValidator validator = new PasswordValidator(resolver, lengthComplexityRule);

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        List<String> messages = validator.getMessages(result);

        String messageTemplate = messages.stream()
                .collect(Collectors.joining(","));
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}