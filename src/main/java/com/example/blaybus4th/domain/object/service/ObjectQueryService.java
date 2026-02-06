package com.example.blaybus4th.domain.object.service;

import com.example.blaybus4th.domain.object.dto.ObjectResponseDTO;
import com.example.blaybus4th.domain.object.entity.enums.ObjectCategory;
import com.example.blaybus4th.domain.object.repository.ObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.blaybus4th.domain.object.converter.ObjectConverter.toObjectCardResponseDTO;

@Service
@RequiredArgsConstructor
public class ObjectQueryService {

    private final ObjectRepository objectRepository;

    public List<ObjectResponseDTO.ObjectCardResponseDTO> getObjectLists(ObjectCategory category) {
        return objectRepository.findAllWithTags(category).stream()
                .map(o -> toObjectCardResponseDTO(
                        o,
                        o.getObjectTags().stream()
                                .map(ot -> ot.getTag().getTagName())
                                .toList()
                ))
                .toList();
    }

    public List<ObjectResponseDTO.ObjectCardResponseDTO> getObjectsByIds(List<Long> ids) {
        return objectRepository.findAllWithTagsByIds(ids).stream()
                .map(o -> toObjectCardResponseDTO(
                        o,
                        o.getObjectTags().stream()
                                .map(ot -> ot.getTag().getTagName())
                                .toList()
                ))
                .toList();
    }
}