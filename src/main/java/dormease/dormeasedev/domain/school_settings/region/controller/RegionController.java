package dormease.dormeasedev.domain.school_settings.region.controller;

import dormease.dormeasedev.domain.school_settings.region.service.RegionService;
import dormease.dormeasedev.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/regions")
public class RegionController implements RegionApi {

    private final RegionService regionService;

    @Override
    public ResponseEntity<ApiResponse> findBigRegions() {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(regionService.findBigRegions())
                .build();
        return ResponseEntity.ok(apiResponse);


    }

    @Override
    public ResponseEntity<ApiResponse> findSmallRegions(Long regionId) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(regionService.findSmallRegions(regionId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
