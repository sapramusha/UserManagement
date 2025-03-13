package in.usha.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.usha.entity.CountryEntity;

public interface CountryRepo  extends JpaRepository<CountryEntity, Integer>{

}
