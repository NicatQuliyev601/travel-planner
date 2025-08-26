package az.nicat.projects.travelplanner.repository;

import az.nicat.projects.travelplanner.entity.Authority;
import az.nicat.projects.travelplanner.entity.enums.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {


    Optional<Authority> findByAuthority(UserAuthority userAuthority);

}
