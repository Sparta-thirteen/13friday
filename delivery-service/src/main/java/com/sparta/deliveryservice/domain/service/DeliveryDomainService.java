package com.sparta.deliveryservice.domain.service;

import com.sparta.deliveryservice.domain.model.Delivery;
import com.sparta.deliveryservice.domain.model.SearchDto;
import com.sparta.deliveryservice.infrastructure.repository.JpaDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryDomainService {

    private final JpaDeliveryRepository jpaDeliveryRepository;


    public Page<Delivery> searchDeliveries(int page, SearchDto searchDto) {

        int pageSize =
            (searchDto.getSize() == 10 || searchDto.getSize() == 30 || searchDto.getSize() == 50)
                ? searchDto.getSize() : 10;

        Sort sort =
            searchDto.getDirection().equalsIgnoreCase("asc") ? Sort.by(searchDto.getSortBy())
                .ascending() : Sort.by(searchDto.getSortBy()).descending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<Delivery> deliveryPage;

        if (searchDto.getKeyword() != null && !searchDto.getKeyword().trim().isEmpty()) {
            // TODO: 메서드 리팩토링필요
            deliveryPage = jpaDeliveryRepository.findByShippingAddressContainingIgnoreCaseAndIsDeletedFalse(
                searchDto.getKeyword(),
                pageable);
        } else {
            deliveryPage = jpaDeliveryRepository.findByIsDeletedFalse(pageable);
        }
        return deliveryPage;
    }

}
