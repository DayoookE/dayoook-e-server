package inha.dayoook_e.mapping.domain.repository;

import inha.dayoook_e.common.BaseEntity.State;
import inha.dayoook_e.mapping.api.controller.dto.response.SearchCountryResponse;
import inha.dayoook_e.mapping.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * CountryJpaRepository는 Country 엔티티에 대한 데이터 액세스 기능을 제공.
 */
public interface CountryJpaRepository extends JpaRepository<Country, Integer> {

    List<Country> findByName(String countryName);

    List<SearchCountryResponse> findAllByState(State state);

    Optional<Country> findByIdAndState(Integer id, State state);
}
