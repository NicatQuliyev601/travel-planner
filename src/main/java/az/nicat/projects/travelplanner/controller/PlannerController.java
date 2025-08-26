package az.nicat.projects.travelplanner.controller;

import az.nicat.projects.travelplanner.dto.request.PlannerRequest;
import az.nicat.projects.travelplanner.dto.response.PlannerResponse;
import az.nicat.projects.travelplanner.service.PlannerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/planner")
public class PlannerController {

    private final PlannerService plannerService;

    public PlannerController(PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<PlannerResponse> planTrip(@RequestBody PlannerRequest request,@PathVariable Long userId) {
        return new ResponseEntity<>(plannerService.planTrip(request,userId), HttpStatus.OK);
    }

    @GetMapping("/saved-trips/{userId}")
    public ResponseEntity<List<PlannerResponse>> getSavedTrips(@PathVariable Long userId) {
        return new ResponseEntity<>(plannerService.getSavedTrips(userId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{plannedId}")
    public void delete(@PathVariable Long plannedId) {
        plannerService.deleteTrip(plannedId);
    }
}
