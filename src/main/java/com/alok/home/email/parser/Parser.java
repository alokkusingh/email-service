package com.alok.home.email.parser;

import com.alok.home.email.entity.Email;

public interface Parser {

    Email parseEmail(final String senderEmail, final String subject, final String content);
}
