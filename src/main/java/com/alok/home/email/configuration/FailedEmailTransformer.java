package com.alok.home.email.configuration;

import com.alok.home.email.entity.Email;
import com.alok.home.email.entity.impl.FailedEmail;
import com.alok.home.email.parser.ParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.stereotype.Component;

@Component
public class FailedEmailTransformer implements GenericTransformer<FailedEmail, Email> {

    private static Logger log = LoggerFactory.getLogger(FailedEmailTransformer.class);

    @Override
    public Email transform(FailedEmail failedEmail) {
        return processPayload(failedEmail) ;
    }

    protected Email processPayload(FailedEmail failedEmail) {
        try {
            String subject = failedEmail.getSubject();
            String emailText = failedEmail.getEmail();
            String content = failedEmail.getContent();

            var email = ParserUtils.parseEmail(emailText, subject, content);
            email.setId(failedEmail.getId());
            return email;
        } catch (Exception e) {
            log.error("Exception: {}", e);
        }

        return null;
    }
}
