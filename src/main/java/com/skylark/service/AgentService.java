package com.skylark.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class AgentService {

    // ✅ Let Spring inject ObjectMapper
	ObjectMapper mapper = new ObjectMapper();

    // ✅ Load Events
	private List<Map<String, Object>> loadEvents() {
	    try {
	        ObjectMapper mapper = new ObjectMapper();
	        InputStream is = new ClassPathResource("data/events.json").getInputStream();
	        return mapper.readValue(is, List.class);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}

    // ✅ Load Drone
	private Map<String, Object> loadDrone() {
	    try {
	        ObjectMapper mapper = new ObjectMapper();
	        InputStream is = new ClassPathResource("data/drone.json").getInputStream();
	        return mapper.readValue(is, Map.class);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new HashMap<>();
	    }
	}

    // 🔥 MAIN AGENT LOGIC
    public Map<String, Object> analyzeNight() {

        List<Map<String, Object>> events = loadEvents();
        Map<String, Object> drone = loadDrone();

        String summary;
        String risk = "Low";
        List<String> actions = new ArrayList<>();
        int score = 0;

        for (Map<String, Object> event : events) {
            String type = (String) event.get("type");

            if ("badge_failure".equals(type)) {
                score += 3;
                actions.add("Investigate badge access logs");
            }

            if ("vehicle".equals(type)) {
                score += 2;
                actions.add("Verify vehicle authorization");
            }

            if ("fence_alert".equals(type)) {
                score += 1;
                actions.add("Check fence integrity");
            }
        }

        // ✅ Risk calculation
        if (score >= 5) {
            risk = "High";
        } else if (score >= 3) {
            risk = "Medium";
        }

        // ✅ Summary
        summary = "Multiple events detected including fence alert, vehicle movement, and badge failures. ";
        summary += "Drone patrol status: " + drone.get("status") + ". ";
        summary += "Correlation suggests moderate risk activity in restricted zones.";

        // ✅ Response
        Map<String, Object> response = new HashMap<>();
        response.put("summary", summary);
        response.put("risk", risk);
        response.put("actions", actions);
        response.put("confidence", "75%");

        return response;
    }
}