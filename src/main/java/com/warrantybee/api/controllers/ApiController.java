package com.warrantybee.api.controllers;

import com.warrantybee.api.dto.response.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller exposing utility endpoints for session and system validation.
 */
@Controller
@RequestMapping("/api")
public class ApiController {

    /**
     * Endpoint used to verify whether the user's session is still active.
     * @return {@link ResponseEntity} containing a generic {@link APIResponse} indicating success
     * @throws Exception if any unexpected error occurs during execution
     */
    @PostMapping("/alive")
    public ResponseEntity<APIResponse<?>> alive() throws Exception {
        return ResponseEntity.ok(new APIResponse<>(null, null));
    }

    /**
     * Endpoint used for checking the health/status of the API.
     * @return a redirect instruction to {@code status.html}
     */
    @GetMapping("/status")
    public String get() {
        return "redirect:/status.html";
    }
}
