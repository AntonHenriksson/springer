package se.jensen.anton.springer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The endpoints are currently incomplete.
 * REST controller for admin-related functionality.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    /**
     * This endpoint allows users with the ADMIN-role to access the admin page.
     * The endpoint hasn't been implemented yet and currently returns a string for demonstration purpose.
     *
     * @return String "Admin page"
     */
    @GetMapping
    public String getAdminPage() {
        return "Admin page";
    }
}
