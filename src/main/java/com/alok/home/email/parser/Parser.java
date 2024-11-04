package com.alok.home.email.parser;

import com.alok.home.email.entity.Email;

public sealed interface Parser permits TransactionParser, StatementParser, IgnoreParser {

    Email parseEmail(final String senderEmail, final String subject, final String content);
}
