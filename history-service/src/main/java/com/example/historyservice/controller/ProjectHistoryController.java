package com.example.historyservice.controller;

import com.example.historyservice.service.ProjectHistoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectHistoryController {
    ProjectHistoryService projectHistoryService;

}
