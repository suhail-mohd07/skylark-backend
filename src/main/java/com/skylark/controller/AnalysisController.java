package com.skylark.controller;

import com.skylark.service.AgentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AnalysisController {

    private final AgentService agentService;

    public AnalysisController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/analyze")
    public Object analyze() {
        return agentService.analyzeNight();
    }
}