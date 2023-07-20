package com.service.account.view;

import com.service.account.dto.LogMessageDTO;
import com.service.account.services.LogService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/security")
public class SecurityController {

    private final LogService logService;
    private final ModelMapper mapper;

    public SecurityController(LogService logService, ModelMapper mapper) {
        this.logService = logService;
        this.mapper = mapper;
    }

    @GetMapping("/events/")
    public ResponseEntity<List<LogMessageDTO>> getLogEvents() {
        List<LogMessageDTO> logMessageDTOs = logService
                .getAll()
                .stream()
                .map(v -> mapper.map(v, LogMessageDTO.class))
                .toList();

        return new ResponseEntity<>(logMessageDTOs, HttpStatus.OK);
    }
}
