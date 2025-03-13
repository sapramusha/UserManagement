package in.usha.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.usha.dto.LoginFormDto;
import in.usha.dto.RegisterFormDto;
import in.usha.dto.ResetFormDTO;
import in.usha.dto.UserDTO;
import in.usha.entity.CityEntity;
import in.usha.entity.CountryEntity;
import in.usha.entity.StateEntity;
import in.usha.entity.UserEntity;
import in.usha.repo.CityRepo;
import in.usha.repo.CountryRepo;
import in.usha.repo.StateRepo;
import in.usha.repo.UserRepo;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private CityRepo cityRepo;
	
	@Autowired
	private CountryRepo countryRepo;
	
	@Autowired
	private StateRepo stateRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private EmailService emailService;
	

	@Override
	public Map<Integer, String> getCountries() {
		
		List<CountryEntity> countriesList = countryRepo.findAll();
		Map<Integer,String> countryMap = new HashMap<>();
		countriesList.forEach(c->{
			countryMap.put(c.getCountryId(), c.getCountryName());
		});
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		
		List<StateEntity> stateList = stateRepo.findByCountryId(countryId);
		Map<Integer,String> stateMap = new HashMap<>();
		stateList.forEach(c->{
			stateMap.put(c.getStateId(), c.getStateName());
		});
		
		return stateMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {
		
		List<CityEntity> cityList = cityRepo.findByStateId(stateId);
		Map<Integer,String> cityMap = new HashMap<>();
		cityList.forEach(c->{
			cityMap.put(c.getCityId(), c.getCityName());
		});
		return cityMap;
	}

	@Override
	public boolean duplicateEmailCheck(String email) {
		   
		UserEntity byEmail=userRepo.findByEmail(email);
		
		if(byEmail != null) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean saveUser(RegisterFormDto regFormDTO) {
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(regFormDTO, userEntity);
		
		CountryEntity country = countryRepo.findById(regFormDTO.getCountryId()).orElse(null);
		userEntity.setCountry(country);
		
		StateEntity state = stateRepo.findById(regFormDTO.getStateId()).orElse(null);
		userEntity.setState(state);
		
		CityEntity city = cityRepo.findById(regFormDTO.getCityId()).orElse(null);
		userEntity.setCity(city);
		
		String generatedPWd = generatedPwd();
		userEntity.setPwd(generatedPWd);
		
		userEntity.setPwdUpdated("NO");
		
		UserEntity savedUser = userRepo.save(userEntity);
		
		if(null != savedUser.getUserId()) {
			String subject = "Your Account Created";
			String Body = "Your Password to login:"+generatedPWd;
			String to = regFormDTO.getEmail();
			emailService.sendEmail(subject, Body, to);
			return true;
		}
		
		return false;
	}
	
	public String generatedPwd() {
		
		String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
		String alphabets = upperCaseLetters+lowerCaseLetters;
		
		Random random = new Random();
		StringBuffer generatedPwd = new StringBuffer();
		
		for(int i=0;i<5;i++) {
			int randomIndex = random.nextInt(alphabets.length());
			generatedPwd.append(alphabets.charAt(randomIndex));
		}
		
		return generatedPwd.toString();
		
	}
	

	@Override
	public UserDTO login(LoginFormDto loginFormDTO) {
		
		UserEntity userEntity = userRepo.findByEmailAndPwd(loginFormDTO.getEmail(), loginFormDTO.getPwd());
		
		if(userEntity != null) {
			UserDTO userDTO = new UserDTO();
			BeanUtils.copyProperties(userEntity, userDTO);
			return userDTO;
		}
		return null;
	}

	@Override
	public boolean resetPwd(ResetFormDTO resetFormDTO) {
		
		String email = resetFormDTO.getEmail();
		UserEntity userEntity = userRepo.findByEmail(email);
		
		userEntity.setPwd(resetFormDTO.getNewPwd());
		userEntity.setPwdUpdated("Yes");
		userRepo.save(userEntity);
		
		return true;
	}

}
