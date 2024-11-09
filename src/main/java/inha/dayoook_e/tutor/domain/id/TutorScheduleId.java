package inha.dayoook_e.tutor.domain.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TutorScheduleId implements Serializable {

    private Integer userId;

    private Integer dayId;

    private Integer timeSlotId;
}
