package in.usha.service;

import java.util.Map;

import in.usha.dto.LoginFormDto;
import in.usha.dto.RegisterFormDto;
import in.usha.dto.ResetFormDTO;
import in.usha.dto.UserDTO;

public interface UserService {
	
	public Map<Integer, String> getCountries();
	
	public Map<Integer, String> getStates(Integer countryId);
	
	public Map<Integer, String> getCities(Integer stateId);
	
	public boolean duplicateEmailCheck(String email);
	
	public boolean saveUser(RegisterFormDto regFormDTO);
	
	public UserDTO login(LoginFormDto loginFormDTO);
	
	public boolean resetPwd(ResetFormDTO resetFormDTO);
	
	
}
