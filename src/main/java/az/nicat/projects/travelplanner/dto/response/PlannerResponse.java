package az.nicat.projects.travelplanner.dto.response;

import az.nicat.projects.travelplanner.entity.User;
import az.nicat.projects.travelplanner.entity.enums.Interest;

import java.time.LocalDate;
import java.util.List;

public class PlannerResponse {
    private Long id;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double budget;
    private List<Interest> interest;
    private List<DayPlan> days;
    private List<String> tips;
    private User user;
    private String parsedData;
    private int kidCount;
    private int adultCount;
    private String additionalNeed;
    private String foodPreferences;


    public static class DayPlan {
        private String date;
        private List<String> activities;
        private Double estimatedCost;
        private String temperature;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<String> getActivities() {
            return activities;
        }

        public void setActivities(List<String> activities) {
            this.activities = activities;
        }

        public Double getEstimatedCost() {
            return estimatedCost;
        }

        public void setEstimatedCost(Double estimatedCost) {
            this.estimatedCost = estimatedCost;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<DayPlan> getDays() {
        return days;
    }

    public void setDays(List<DayPlan> days) {
        this.days = days;
    }

    public List<String> getTips() {
        return tips;
    }

    public void setTips(List<String> tips) {
        this.tips = tips;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getParsedData() {
        return parsedData;
    }

    public void setParsedData(String parsedData) {
        this.parsedData = parsedData;
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

    public String getAdditionalNeed() {
        return additionalNeed;
    }

    public void setAdditionalNeed(String additionalNeed) {
        this.additionalNeed = additionalNeed;
    }

    public String getFoodPreferences() {
        return foodPreferences;
    }

    public void setFoodPreferences(String foodPreferences) {
        this.foodPreferences = foodPreferences;
    }
}
