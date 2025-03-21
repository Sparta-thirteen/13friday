package com.sparta.deliveryservice.domain.service;


import com.sparta.deliveryservice.domain.model.DeliveryRoute;
import com.sparta.deliveryservice.domain.model.SearchDto;
import com.sparta.deliveryservice.infrastructure.repository.JpaDeliveryRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryRouteDomainService {

    private final JpaDeliveryRouteRepository jpaDeliveryRouteRepository;

    public Page<DeliveryRoute> searchDeliveryRoute(SearchDto searchDto) {

        int pageSize =
            (searchDto.getSize() == 10 || searchDto.getSize()== 30 || searchDto.getSize()== 50)
                ? searchDto.getSize(): 10;

        Sort sort =
            searchDto.getDirection().equalsIgnoreCase("asc") ? Sort.by(searchDto.getSortBy())
                .ascending() : Sort.by(searchDto.getSortBy()).descending();

        Pageable pageable = PageRequest.of(searchDto.getPage(), pageSize, sort);

        Page<DeliveryRoute> deliveryRoutePage;

        if (searchDto.getKeyword() != null && !searchDto.getKeyword().trim().isEmpty()) {
            // TODO: 메서드 리팩토링필요
            deliveryRoutePage = jpaDeliveryRouteRepository.findByShippingAddressContainingIgnoreCaseAndIsDeletedFalse(searchDto.getKeyword(),pageable);
        } else {
            deliveryRoutePage = jpaDeliveryRouteRepository.findByIsDeletedFalse(pageable);
        }
        return deliveryRoutePage;
    }

}
