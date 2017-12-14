package com.sneha.lnr.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sneha.lnr.models.User;
import com.sneha.lnr.services.UserService;

@Controller
public class MainController {
	
	private final UserService userService;
	
	public MainController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping("/")
	public String index(@ModelAttribute("user")User user) {
		return "index";
	}
	
	@PostMapping("/register/user")
	public String user_handler(HttpSession session, @RequestParam("c_password") String c_password, @Valid @ModelAttribute("user")User user, BindingResult result, Model model) {
		if( result.hasErrors()) {
			return "index";
		}else {
			System.out.println(user.getAlias());
			userService.saveUser(user , c_password, session);
			if(session.getAttribute("userid") != null) {
				return "redirect:/dashboard";
			}else {
				return "index";
			}
		}
	}
	
	@PostMapping("/login")
	public String login(@RequestParam("email") String email, @RequestParam("password") String  password , HttpSession session,Model model,RedirectAttributes redirectAttributes) {
		
				if ( userService.login(email , password, session) ) {
					return "redirect:/dashboard";
				}
				else {
					System.out.println("in else");
					redirectAttributes.addFlashAttribute("error", "Email And Password do not match. Please try again !!");
					return "redirect:/";
				} 
		
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	
}
