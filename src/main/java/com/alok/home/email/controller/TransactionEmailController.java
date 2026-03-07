package com.alok.home.email.controller;

import com.alok.home.email.parser.dto.TransactionEmailDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RequestMapping("/transactions")
@RestController
@Tag(name = "Transaction Email API", description = "Endpoints for retrieving and updating parsed transaction emails")
public abstract class TransactionEmailController {

    @GetMapping()
    @Operation(summary = "List transactions", description = "Returns parsed transaction emails, optionally filtered by verification status.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of transactions returned",
                    content = @Content(schema = @Schema(implementation = TransactionEmailDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public abstract ResponseEntity<List<TransactionEmailDTO>> getTransactions(
            @Parameter(description = "Filter by verified state", required = false, example = "true")
            @RequestParam(value = "verified", defaultValue = "false") Boolean verified
    );

    @PutMapping("/{id}")
    @Operation(summary = "Update transaction", description = "Updates a parsed transaction by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or id"),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public abstract ResponseEntity<Void> updateTransaction(
            @Parameter(description = "Transaction id", required = true, example = "12345")
            @PathVariable(value = "id") String id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Transaction payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TransactionEmailDTO.class))
            )
            @RequestBody TransactionEmailDTO transactionEmailDTO
    );
}