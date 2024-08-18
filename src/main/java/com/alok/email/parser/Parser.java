package com.alok.email.parser;

import com.alok.email.model.Email;

public interface Parser {

    Email parseEmail(final String senderEmail, final String subject, final String content);
}
