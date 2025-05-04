package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.ControllerMessage;

public record DTOResponseError(String mensaje) implements ControllerMessage {
}
