package az.nicat.projects.travelplanner.entity;

import az.nicat.projects.travelplanner.entity.enums.Interest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;


@Entity
public class Planner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double budget;
    @ElementCollection
    private List<Interest> interest;
    @Column(name = "parsed_data", columnDefinition = "TEXT")
    private String parsedData;
    private int kidCount;
    private int adultCount;
    private String foodPreferences;
    private String additionalNeed;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

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
