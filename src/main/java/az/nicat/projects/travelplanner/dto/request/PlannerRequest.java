package az.nicat.projects.travelplanner.dto.request;

import az.nicat.projects.travelplanner.entity.enums.Interest;

import java.time.LocalDate;
import java.util.List;

public class PlannerRequest {
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double budget;
    private List<Interest> interest;
    private int kidCount;
    private int adultCount;
    private String foodPreferences;
    private String additionalNeed;


    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public List<Interest> getInterest() {
        return interest;
    }

    public void setInterest(List<Interest> interest) {
        this.interest = interest;
    }

    public int getKidCount() {
        return kidCount;
    }

    public void setKidCount(int kidCount) {
        this.kidCount = kidCount;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public String getFoodPreferences() {
        return foodPreferences;
    }

    public void setFoodPreferences(String foodPreferences) {
        this.foodPreferences = foodPreferences;
    }

    public String getAdditionalNeed() {
        return additionalNeed;
    }

    public void setAdditionalNeed(String additionalNeed) {
        this.additionalNeed = additionalNeed;
    }
}
