package in.usha.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import in.usha.dto.LoginFormDto;
import in.usha.dto.QuoteApiResponseDTO;
import in.usha.dto.RegisterFormDto;
import in.usha.dto.ResetFormDTO;
import in.usha.dto.UserDTO;
import in.usha.service.DashboardService;
import in.usha.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private DashboardService dashboardService;
	
	@GetMapping("/register")
	public String loadRegister(Model model) {
		
		Map<Integer,String> countries = userService.getCountries();
		model.addAttribute("countries", countries);
		
		RegisterFormDto registerFormDTO = new RegisterFormDto();
		model.addAttribute("registerFormDTO", registerFormDTO);
		return "register";
		
	}
	
	@GetMapping("States/{countryId}")
	@ResponseBody
	public Map<Integer, String> getStates(@PathVariable("countryId") Integer countryId) {
		Map<Integer,String> statesMap = userService.getStates(countryId);
		System.out.println("statesMap>>>>"+statesMap);
		System.out.println("Updated");
		return statesMap;
	}
	
	@GetMapping("Cities/{stateId}")
	@ResponseBody
	public Map<Integer, String> getCities(@PathVariable("stateId") Integer stateId) {
		Map<Integer,String> citiesMap = userService.getCities(stateId);
		return citiesMap;
	}
	
	@PostMapping("/register")
	public String handleRegistration(RegisterFormDto registerFromDTO,Model model) {
		boolean status = userService.duplicateEmailCheck(registerFromDTO.getEmail());
		
		if (status) {
			model.addAttribute("emsg","duplicated Email Found");
		}else {
			boolean saveUser = userService.saveUser(registerFromDTO);
			if(saveUser) {
				model.addAttribute("smsg", "Registration Success.Please check your email");
			}else {
				model.addAttribute("emsg", "Registration Failed");
			}
		}
		model.addAttribute("countries", userService.getCountries());
		RegisterFormDto registerFormDTO = new RegisterFormDto();
		model.addAttribute("registerFormDTO", registerFormDTO);
		return "register";
	}
	
	@GetMapping("/")
	public String index(Model model) {
		
		LoginFormDto loginFormDTO =  new LoginFormDto();
		model.addAttribute("loginFormDTO", loginFormDTO);
		return "login";
	}
	
	@PostMapping("/login")
	public String handleUserLogin(LoginFormDto loginFormDTO,Model model ) {
		
		UserDTO userDTO = userService.login(loginFormDTO);
		if(userDTO == null) {
			model.addAttribute("emsg", "Invalid Credentials");
			model.addAttribute("loginFormDTO", loginFormDTO);
		}else {
			String pwdUpdated = userDTO.getPwdUpdated();
			if("Yes".equals(pwdUpdated)) {
				//return dashboardpage
				return "redirect:dashborad";
			}else {
				//return resetpwd
				return "redirect:reset-pwd-page?email="+userDTO.getEmail();
			}
		}
		return "login";
	}
	
	@GetMapping("/dashborad")
	public String dashboard(Model model) {
		QuoteApiResponseDTO quoteApiResponseDTO = dashboardService.getQuote();
		model.addAttribute("quote", quoteApiResponseDTO);
		return "dashborad";
	}
	
	@GetMapping("/reset-pwd-page")
	public String loadResetPwdPage(@RequestParam("email") String email,Model model) {
		ResetFormDTO resetFormDTO = new ResetFormDTO();
		resetFormDTO.setEmail(email);
		model.addAttribute("resetFormDTO", resetFormDTO);
		return "resetPwd";
	}
	
	@PostMapping("/resetPwd")
	public String handleResetPwdPage(ResetFormDTO resetFormDTO,Model model) {
		
		boolean resetPwd = userService.resetPwd(resetFormDTO);
		if(resetPwd) {
			System.out.println("resetPwd>>>"+resetPwd);
			return "redirect:dashborad";
		}else {
			return "resetPwd";
		}
		
	}
	
	
	
	
	
}
