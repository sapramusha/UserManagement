package in.usha.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.usha.entity.CityEntity;

public interface CityRepo extends JpaRepository<CityEntity, Integer> {
	
	public List<CityEntity> findByStateId(Integer stateId);

}
