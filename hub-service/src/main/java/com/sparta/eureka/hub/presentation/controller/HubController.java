package com.sparta.eureka.hub.presentation.controller;

import com.sparta.eureka.hub.application.dto.hub.HubDto;
import com.sparta.eureka.hub.application.service.HubDataService;
import com.sparta.eureka.hub.application.service.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hub")
public class HubController {
    private final HubService hubService;
    private final HubDataService hubDataService;

    @PostMapping("/initData")
    public ResponseEntity<HubDto.ResponseDto> createHubs(@RequestHeader("X-Role") String role) {
        hubDataService.createInitDataHubs(role);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<HubDto.ResponseDto> createHub(@RequestHeader("X-Role") String role,
                                                        @RequestBody HubDto.CreateDto request) {
        HubDto.ResponseDto response = hubService.createHub(role, request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{hubId}")
    public ResponseEntity<HubDto.ResponseDto> updateHub(@RequestHeader("X-Role") String role,
                                                        @PathVariable("hubId") UUID hubId,
                                                        @RequestBody HubDto.UpdateDto request) {
        HubDto.ResponseDto response = hubService.updateHub(role, hubId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<HubDto.ResponseDto>> getHubs(@RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "1") int page) {
        Page<HubDto.ResponseDto> response = hubService.getHubs(page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{hubId}")
    public ResponseEntity<HubDto.ResponseDto> getHub(@PathVariable("hubId") UUID hubId) {
        HubDto.ResponseDto response = hubService.getHub(hubId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<HubDto.ResponseDto>> searchHubs(@RequestParam(defaultValue = "10") int size,
                                                               @RequestParam(defaultValue = "1") int page,
                                                               @RequestParam String keyword,
                                                               @RequestParam(defaultValue = "true") boolean isDesc) {
        Page<HubDto.ResponseDto> response = hubService.searchHubs(size, page, keyword, isDesc);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/manager")
    public ResponseEntity<HubDto.ResponseDto> getHubByManager(@RequestHeader("X-User-Id") String userId) {
        HubDto.ResponseDto response = hubService.getHubByManager(userId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{hubId}/updateCoordinates")
    public ResponseEntity<Void> updateCoordinates(@RequestHeader("X-Role") String role,
                                                  @PathVariable UUID hubId) {
        hubService.updateHubCoordinates(role, hubId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/grant/{userId}")
    public ResponseEntity<HubDto.ResponseDto> grantHub(@RequestHeader("X-Role") String role,
                                                       @PathVariable Long userId,
                                                       @RequestBody HubDto.UpdateUserDto request) {
        HubDto.ResponseDto response = hubService.addHubAuth(role, userId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/{hubId}")
    public ResponseEntity<HubDto.ResponseDto> deleteHub(@RequestHeader("X-User-Id") String userId,
                                                        @RequestHeader("X-Role") String role,
                                                        @PathVariable UUID hubId) {
        hubService.deleteHub(userId, role, hubId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
