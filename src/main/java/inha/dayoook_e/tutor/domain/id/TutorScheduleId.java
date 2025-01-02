package inha.dayoook_e.tutor.domain.id;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TutorScheduleId implements Serializable {

    private Integer userId;

    private Integer dayId;

    private Integer timeSlotId;
}
