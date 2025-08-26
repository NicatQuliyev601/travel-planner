package az.nicat.projects.travelplanner.repository;

import az.nicat.projects.travelplanner.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerRepository extends JpaRepository<Planner, Long> {
    List<Planner> findAllByUserId(Long userId);

    Planner findByUserId(Long userId);

}
