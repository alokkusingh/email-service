package com.alok.home.email.controller;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.alok.home.commons.dto.EmailRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RequestMapping("/send")
@RestController
@Tag(name = "Email API", description = "Endpoints for sending emails and email reports")
public abstract class EmailController {

    @GetMapping()
    @Operation(summary = "Send plain text email",
            description = "Sends a simple plain-text email using query parameters.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Server error while sending email")
    })
    public abstract ResponseEntity<Void> sendEmail(
            @Parameter(description = "Recipient email address", required = false, example = "alok.ku.singh@gmail.com")
            @RequestParam(name = "name", required = false, defaultValue = "alok.ku.singh@gmail.com") String to,

            @Parameter(description = "Email subject", required = false, example = "Mail from Home Stack")
            @RequestParam(name = "subject", required = false, defaultValue = "Mail from Home Stack") String subject,

            @Parameter(description = "Email body", required = false, example = "Test mail")
            @RequestParam(name = "body", required = false, defaultValue = "Test mail") String body
    );

    @PostMapping()
    @Operation(summary = "Send email (JSON payload)",
            description = "Sends an email using a JSON payload with recipient, subject and body.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Server error while sending email")
    })
    public abstract ResponseEntity<Void> sendEmail(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Email request payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EmailRequest.class))
            )
            @RequestBody() EmailRequest emailRequest
    );

    @PostMapping(value = "/html")
    @Operation(summary = "Send HTML email",
            description = "Sends an HTML formatted email. May throw MessagingException for mail errors.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTML email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Server error while sending HTML email")
    })
    public abstract ResponseEntity<Void> sendHtmlEmail(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Email request payload (HTML)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EmailRequest.class))
            )
            @RequestBody() EmailRequest emailRequest
    ) throws MessagingException;

    @PostMapping("/dailyReport")
    @Operation(summary = "Send daily report email",
            description = "Sends a daily report email. May throw MessagingException for mail errors.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Daily report sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Server error while sending daily report")
    })
    public abstract ResponseEntity<Void> sendDailyReport(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Email request payload for daily report",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EmailRequest.class))
            )
            @RequestBody() EmailRequest emailRequest
    ) throws MessagingException;

    @PostMapping("/yearExpenseReport")
    @Operation(summary = "Send yearly expense report email",
            description = "Sends the yearly expense report. May throw MessagingException for mail errors.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Yearly expense report sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Server error while sending yearly expense report")
    })
    public abstract ResponseEntity<Void> yearExpenseReport(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Email request payload for yearly expense report",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EmailRequest.class))
            )
            @RequestBody() EmailRequest emailRequest
    ) throws MessagingException;

    @PostMapping("/investmentReport")
    @Operation(summary = "Send investment report email",
            description = "Sends an investment report email. May throw MessagingException for mail errors.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Investment report sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Server error while sending investment report")
    })
    public abstract ResponseEntity<Void> sendInvestmentReport(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Email request payload for investment report",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EmailRequest.class))
            )
            @RequestBody() EmailRequest emailRequest
    ) throws MessagingException;
}