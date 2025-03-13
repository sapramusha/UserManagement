package in.usha.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.usha.entity.StateEntity;

public interface StateRepo  extends JpaRepository<StateEntity, Integer> {
	
	public List<StateEntity> findByCountryId(Integer countryId);

}
