package com.sparta.eureka.hub.presentation.controller;

import com.sparta.eureka.hub.application.dto.hub.HubDto;
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

    @PostMapping
    public ResponseEntity<HubDto.ResponseDto> createHub(@RequestHeader("X-Role") String role,
                                                        @RequestBody HubDto.CreateDto request) {
        HubDto.ResponseDto response = hubService.createHub(role, request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{hub_id}")
    public ResponseEntity<HubDto.ResponseDto> updateHub(@RequestHeader("X-Role") String role,
                                                        @PathVariable("hub_id") UUID hubId,
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

    @PatchMapping("/{hubId}/updateCoordinates")
    public ResponseEntity<Void> updateCoordinates(@RequestHeader("X-Role") String role,
                                                  @PathVariable UUID hubId) {
        hubService.updateHubCoordinates(role, hubId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/grant/{hubId}")
    public ResponseEntity<HubDto.ResponseDto> grantHub(@RequestHeader("X-Role") String role,
                                                       @PathVariable UUID hubId,
                                                       @RequestBody HubDto.UpdateUserDto request) {
        HubDto.ResponseDto response = hubService.addHubAuth(role, hubId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/{hubId}")
    public ResponseEntity<HubDto.ResponseDto> deleteHub(@RequestHeader("X-Role") String role,
                                                        @PathVariable UUID hubId) {
        hubService.deleteHub(role, hubId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
