package com.sunrise.dental.controller;

import com.sunrise.dental.entity.Staff;
import com.sunrise.dental.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) { this.authService = authService; }

    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password,
                          HttpSession session, Model model) {
        return authService.login(username, password)
            .map(staff -> {
                session.setAttribute("staff", staff);
                return "redirect:/dashboard";
            })
            .orElseGet(() -> {
                model.addAttribute("error", "Invalid username or password. Please try again.");
                return "login";
            });
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
