package com.example.blaybus4th.domain.aiChat.dto.response;

import com.example.blaybus4th.domain.aiChat.entity.ChatSession;
import com.example.blaybus4th.domain.aiChat.enums.CommandType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AiDocentResponse {

    private String thought;
    private String aiMessage;
    private List<ViewerCommander> commands;
    private Long chatSessionId;
    private String chatSessionTitle;

    public static AiDocentResponse from(AiDocentResponse response, ChatSession chatSession) {
        return new AiDocentResponse(
                response.getThought(),
                response.getAiMessage(),
                response.getCommands(),
                chatSession.getChatSessionId(),
                chatSession.getChatSessionTitle()
        );
    }

    @Getter
    public static class ViewerCommander{
        private CommandType type; //

        // SET_EXPLOSION 용
        private Double value;

        // SELECT_PART 용
        private String modelId;

        // HIGHLIGHT_PARTS 용
        private List<String> modelIds; // 부품 id 리스트

        // LOAD_SCENE 용
        private String objectId;
        private String followUpMessage;

    }


}
