package az.nicat.projects.travelplanner.service;

import az.nicat.projects.travelplanner.dto.request.PlannerRequest;
import az.nicat.projects.travelplanner.dto.response.PlannerResponse;
import az.nicat.projects.travelplanner.entity.Planner;
import az.nicat.projects.travelplanner.entity.User;
import az.nicat.projects.travelplanner.repository.PlannerRepository;
import az.nicat.projects.travelplanner.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlannerService {

    private final UserRepository userRepository;
    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.api.url}")
    private String openaiApiUrl;

    private final PlannerRepository plannerRepository;
    private final ModelMapper modelMapper;

    public PlannerService(PlannerRepository plannerRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.plannerRepository = plannerRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public PlannerResponse planTrip(PlannerRequest plannerRequest, Long userId) {
        String prompt = """
                You are an intelligent and experienced travel planning assistant.

                Your task is to generate a **comprehensive and personalized travel itinerary** based on the user's input. 
                The itinerary should be tailored to the user's interests, budget, trip duration, and number of travelers.
                Use your knowledge of global destinations, travel trends, budgeting practices, and typical seasonal weather 
                data to deliver an efficient and enjoyable trip plan.

                ### User Preferences:
                - **Destination:** %s
                - **Start Date:** %s
                - **End Date:** %s
                - **Total Budget:** %.2f USD
                - **Number of kidCount:** %d
                - **Number of adultCount:** %d
                - **Preference Food:** %s
                - **Interest Category:** %s
                - **Additional Need:** %s

                ### Your Response Must Include:
                1. A **structured, day-by-day travel itinerary**.
                2. **Suggested places to visit** and **activities** for each day aligned with the user's interest.
                3. A realistic **estimated cost per day** considering the number of travelers.
                4. **Typical weather temperature** for each day (in Celsius or Fahrenheit).
                5. A **total estimated cost** for the entire trip.
                6. **Travel tips and recommendations** relevant to the destination.

                ### Formatting Instructions:
                Respond **ONLY** in **valid, parsable JSON**. 
                Do not include code blocks, markdown, or extra commentary. 
                Your response should follow this exact structure:

                {
                  "destination": "Destination name",
                  "peopleCount": Number,
                  "days": [
                    {
                      "date": "YYYY-MM-DD",
                      "activities": ["Activity 1", "Activity 2", "..."],
                      "estimatedCost": Number,
                      "temperature": "20°C"
                    },
                    ...
                  ],
                  "totalEstimatedCost": Number,
                  "tips": ["Tip 1", "Tip 2", "..."]
                }
                """.formatted(
                plannerRequest.getDestination(),
                plannerRequest.getStartDate(),
                plannerRequest.getEndDate(),
                plannerRequest.getBudget(),
                plannerRequest.getKidCount(),
                plannerRequest.getAdultCount(),
                plannerRequest.getFoodPreferences(),
                plannerRequest.getInterest(),
                plannerRequest.getAdditionalNeed()
        );


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o");

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "You are a helpful assistant. Respond ONLY with valid JSON. No backticks."));
        messages.add(Map.of("role", "user", "content", prompt));
        body.put("messages", messages);
        body.put("temperature", 0.2);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                openaiApiUrl,
                request,
                String.class
        );

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            String content = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            String cleanedJson = content
                    .replaceAll("(?s)```json\\s*", "")
                    .replaceAll("(?s)```", "")
                    .trim();

            JsonNode json = mapper.readTree(cleanedJson);

            PlannerResponse plannerResponse = new PlannerResponse();
            plannerResponse.setDestination(json.path("destination").asText());
            plannerResponse.setStartDate(plannerRequest.getStartDate());
            plannerResponse.setEndDate(plannerRequest.getEndDate());
            plannerResponse.setBudget(json.path("totalEstimatedCost").asDouble(plannerRequest.getBudget()));
            plannerResponse.setInterest(plannerRequest.getInterest());
            plannerResponse.setKidCount(json.path("kidCount").asInt(plannerRequest.getKidCount()));
            plannerResponse.setAdultCount(json.path("adultCount").asInt(plannerRequest.getAdultCount()));
            plannerResponse.setFoodPreferences(plannerRequest.getFoodPreferences());
            plannerResponse.setAdditionalNeed(plannerRequest.getAdditionalNeed());

            List<PlannerResponse.DayPlan> dayPlans = new ArrayList<>();
            JsonNode daysNode = json.path("days");
            if (daysNode.isArray()) {
                for (JsonNode dayNode : daysNode) {
                    PlannerResponse.DayPlan dayPlan = new PlannerResponse.DayPlan();
                    dayPlan.setDate(dayNode.path("date").asText());

                    List<String> activities = new ArrayList<>();
                    for (JsonNode activity : dayNode.path("activities")) {
                        activities.add(activity.asText());
                    }
                    dayPlan.setActivities(activities);

                    dayPlan.setEstimatedCost(dayNode.path("estimatedCost").asDouble());
                    dayPlan.setTemperature(dayNode.path("temperature").asText());

                    dayPlans.add(dayPlan);
                }
            }

            plannerResponse.setDays(dayPlans);

            List<String> tips = new ArrayList<>();
            JsonNode tipsNode = json.path("tips");
            if (tipsNode.isArray()) {
                for (JsonNode tip : tipsNode) {
                    tips.add(tip.asText());
                }
            }
            plannerResponse.setTips(tips);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User Not Found"));

            plannerResponse.setUser(user);

            Planner entity = modelMapper.map(plannerResponse, Planner.class);
            entity.setUser(user);
            entity.setParsedData(content);
            plannerRepository.save(entity);

            return plannerResponse;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }

    public List<PlannerResponse> getSavedTrips(Long userId) {
        return plannerRepository
                .findAllByUserId(userId)
                .stream()
                .map(planner -> modelMapper.map(planner, PlannerResponse.class))
                .collect(Collectors.toList());
    }

    public void deleteTrip(Long plannerId) {
        Planner plannedTripNotFounded = plannerRepository.findById(plannerId).orElseThrow(
                () -> new RuntimeException("Planned trip not founded")
        );
        plannerRepository.delete(plannedTripNotFounded);
    }
}
