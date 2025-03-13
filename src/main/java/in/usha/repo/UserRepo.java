package in.usha.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.usha.entity.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {
	
	public UserEntity findByEmail(String email);
	
	public UserEntity findByEmailAndPwd(String email,String pwd);
	
}
