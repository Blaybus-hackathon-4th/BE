package com.example.blaybus4th.domain.object.service;

import com.example.blaybus4th.domain.object.converter.ObjectConverter;
import com.example.blaybus4th.domain.object.dto.ObjectResponseDTO;
import com.example.blaybus4th.domain.object.entity.Model;
import com.example.blaybus4th.domain.object.entity.ModelComponents;
import com.example.blaybus4th.domain.object.entity.enums.ObjectCategory;
import com.example.blaybus4th.domain.object.repository.ModelRepository;
import com.example.blaybus4th.domain.object.repository.ObjectRepository;
import com.example.blaybus4th.global.apiPayload.code.GeneralErrorCode;
import com.example.blaybus4th.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.blaybus4th.domain.object.converter.ObjectConverter.toObjectCardResponseDTO;

@Service
@RequiredArgsConstructor
public class ObjectQueryService {

    private final ObjectRepository objectRepository;
    private final ModelRepository modelRepository;

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

    public ObjectResponseDTO.ObjectComponentResponseDTO getObjectComponentResponseDTO(Long id) {
        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.MODEL_NOT_FOUND));
        List<ModelComponents> components = model.getModelComponents();
        if (components == null) {
            components = List.of();
        }
        List<ObjectResponseDTO.ElementResponseDTO> elements = components.stream()
                .map(ObjectConverter::toElementResponseDTO)
                .toList();
        return ObjectConverter.toObjectComponentResponseDTO(model, elements);
    }

    public List<ObjectResponseDTO.ObjectCardResponseDTO> searchObjects(String keyword) {
        return objectRepository.findByObjectNameKrOrEn(keyword).stream()
                .map(o -> toObjectCardResponseDTO(
                        o,
                        o.getObjectTags().stream()
                                .map(ot -> ot.getTag().getTagName())
                                .toList()
                ))
                .toList();
    }
}